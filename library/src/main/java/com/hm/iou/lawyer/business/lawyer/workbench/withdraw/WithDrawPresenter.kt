package com.hm.iou.lawyer.business.lawyer.workbench.withdraw

import android.content.Context
import com.hm.iou.base.comm.CommApi
import com.hm.iou.base.mvp.HMBasePresenter
import com.hm.iou.lawyer.api.LawyerApi
import com.hm.iou.lawyer.bean.req.WithDrawMoneyReqBean
import com.hm.iou.lawyer.business.NavigationHelper
import com.hm.iou.lawyer.dict.UpdateWalletBalanceEvent
import com.hm.iou.sharedata.UserManager
import com.hm.iou.sharedata.event.BindBankSuccessEvent
import com.hm.iou.sharedata.model.UserThirdPlatformInfo
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.StringBuilder
import java.text.DecimalFormat
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * @author : 借条管家-shilei
 * @version : 0.0.1
 * @Date : 2019-11-08 15:19
 * @E-Mail : afinalstone@foxmail.com
 */
class WithDrawPresenter(context: Context, view: WithDrawContract.View) :
    HMBasePresenter<WithDrawContract.View>(context, view),
    WithDrawContract.Presenter {

    private var mNeedRefresh: Boolean = false

    private var mIsBindBankCard: Boolean = false    //是否有绑定银行卡
    private var mWalletBalance: Int = 0             //当前钱包余额，单位分

    init {
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun init() {
        launch {
            try {
                getCurrentWalletBalance()
                getBankCardInfo(false)
            } catch (e: Exception) {
                handleException(e)
            }
        }
    }

    override fun onResume() {
        if (mNeedRefresh) {
            mNeedRefresh = false
            launch {
                try {
                    getBankCardInfo(true)
                } catch (e: Exception) {
                    handleException(e)
                }
            }
        }
    }

    override fun clickWithdrawAll() {
        val value = mWalletBalance / 100f
        if (value.toInt() < value) {
            mView.updateWithdrawMoney(value.toString())
        } else {
            mView.updateWithdrawMoney(value.toInt().toString())
        }
    }

    override fun calcWithDrawMoney(withDrawMoney: Float) {
        if (withDrawMoney * 100 > mWalletBalance) {
            mView.toastErrorMessage("提现金额不能大于钱包余额")
            return
        }

        launch {
            mView.showLoadingView()
            try {
                val result =
                    handleResponse(LawyerApi.calcLawyerWithDrawRate((withDrawMoney * 100).toInt()))
                mView.dismissLoadingView()
                result?.let {
                    mView.showWithDrawDialog(
                        it.actualWithdrawNum,
                        it.withdrawNum,
                        it.serviceFee,
                        it.feePercent
                    )
                }
            } catch (e: Exception) {
                handleException(e)
                mView.dismissLoadingView()
            }
        }
    }

    override fun withDrawMoney(withDrawMoney: Float) {
        launch {
            mView.showLoadingView()
            try {
                val result = handleResponse(LawyerApi.withDrawMoney(WithDrawMoneyReqBean((withDrawMoney * 100).toInt())))
                EventBus.getDefault().post(UpdateWalletBalanceEvent())
                mView.dismissLoadingView()
                mView.updateWithdrawMoney("")
                mView.toastMessage("提现成功")
                //刷新钱包余额信息
                getCurrentWalletBalance()

                result?.let {
                    NavigationHelper.toWithdrawDetailPage(mContext, it)
                }
            } catch (e: Exception) {
                handleException(e)
                mView.dismissLoadingView()
            }

        }
    }

    /**
     * 获取当前的钱包余额信息
     */
    private suspend fun getCurrentWalletBalance() {
        val result = handleResponse(LawyerApi.getLawyerWallet())
        result?.let {
            mWalletBalance = it.walletBalance
            mView.showWalletBalance("钱包余额¥${DecimalFormat("#0.00").format(mWalletBalance / 100f)}")
        }
    }

    /**
     * 获取银行卡信息
     */
    private suspend fun getBankCardInfo(refreshOnLine: Boolean) {
        val thirdPlfInfo = UserManager.getInstance(mContext).userExtendInfo.thirdPlatformInfo
        if (!refreshOnLine && 1 == thirdPlfInfo?.bankInfoResp?.isBinded) {
            //缓存里读取银行卡信息
            val bankCard = formatBankCardNo(thirdPlfInfo.bankInfoResp?.bankCard)
            val bankName = thirdPlfInfo.bankInfoResp?.bankName
            mView.showBankCard(bankCard, bankName)
            mIsBindBankCard = true
        } else {
            val thirdPlatformInfo: UserThirdPlatformInfo? =
                handleResponse(LawyerApi.getUserThirdPlatformInfo())
            //有绑定银行卡信息
            if (1 == thirdPlatformInfo?.bankInfoResp?.isBinded) {
                //存储绑定银行卡信息
                val extendInfo = UserManager.getInstance(mContext).userExtendInfo
                extendInfo.thirdPlatformInfo = thirdPlatformInfo
                UserManager.getInstance(mContext).updateOrSaveUserExtendInfo(extendInfo)
                val bankCard = formatBankCardNo(thirdPlatformInfo.bankInfoResp.bankCard)
                val bankName = thirdPlatformInfo.bankInfoResp.bankName
                mView.showBankCard(bankCard, bankName)
                mIsBindBankCard = true
            } else {
                //没有绑定银行卡信息
                mView.showBankCard("", "")
                mIsBindBankCard = false
            }
        }
    }

    /**
     * 显示前4位、后4位，其他显示为*，每4位一个空格
     */
    private fun formatBankCardNo(cardNo: String?): String? {
        if (cardNo.isNullOrEmpty())
            return ""
        if (cardNo.length < 8)
            return cardNo
        val sb = StringBuilder()
        sb.append(cardNo.substring(0, 4))
            .append(" **** **** ")
            .append(cardNo.substring(cardNo.length - 4))
        return sb.toString()
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventBindBankCardSuccess(event: BindBankSuccessEvent) {
        mNeedRefresh = true
    }
}
package com.hm.iou.lawyer.business.lawyer.workbench.withdraw

import android.content.Context
import com.hm.iou.base.mvp.HMBasePresenter
import com.hm.iou.lawyer.api.LawyerApi
import com.hm.iou.sharedata.UserManager
import com.hm.iou.sharedata.event.BindBankSuccessEvent
import com.hm.iou.sharedata.model.UserThirdPlatformInfo
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

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

    init {
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun init() {
        getBankCardInfo()
        mView.showRemainderMoney("10000.00")
    }

    override fun onResume() {
        if (mNeedRefresh) {
            getBankCardInfo()
        }
    }

    override fun calcWithDrawMoney(withDrawMoney: Float) {
        launch {
            mView.showLoadingView()
            delay(3000)
            mView.dismissLoadingView()
            mView.showWithDrawDialog(
                withDrawMoney.toString(),
                (withDrawMoney - 100).toString(),
                "1",
                "10%"
            )
        }
    }

    override fun withDrawMoney(withDrawMoney: Float) {
        launch {
            mView.showLoadingView()
            delay(3000)
            mView.dismissLoadingView()
            mView.toastMessage("提现成功")
        }
    }

    /**
     * 获取银行卡信息
     */
    private fun getBankCardInfo() {
        launch {
            val thirdPlfInfo =
                UserManager.getInstance(mContext).userExtendInfo.thirdPlatformInfo
            if (1 == thirdPlfInfo?.bankInfoResp?.isBinded) {
                val bankCard = thirdPlfInfo.bankInfoResp?.bankCard
                val bankName = thirdPlfInfo.bankInfoResp?.bankName
                mView.showBankcard(bankCard, bankName)
            } else {
                try {
                    mView.showLoadingView()
                    val thirdPlatformInfo: UserThirdPlatformInfo? =
                        handleResponse(LawyerApi.getUserThirdPlatformInfo())
                    thirdPlatformInfo?.let {
                        mView.dismissLoadingView()
                        //存储绑定银行卡信息
                        val extendInfo = UserManager.getInstance(mContext).userExtendInfo
                        extendInfo.thirdPlatformInfo = thirdPlatformInfo
                        UserManager.getInstance(mContext).updateOrSaveUserExtendInfo(extendInfo)
                        val bankCard = thirdPlatformInfo.bankInfoResp.bankCard
                        val bankName = thirdPlatformInfo.bankInfoResp.bankName
                        mView.showBankcard(bankCard, bankName)
                    }
                } catch (e: Exception) {
                    mView.dismissLoadingView()
                    e.printStackTrace()
                    handleException(e)
                }

            }
        }

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventBindBankCardSuccess(event: BindBankSuccessEvent) {
        mNeedRefresh = true
    }
}
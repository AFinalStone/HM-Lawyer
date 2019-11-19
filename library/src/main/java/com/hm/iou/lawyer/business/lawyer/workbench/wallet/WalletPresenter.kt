package com.hm.iou.lawyer.business.lawyer.workbench.wallet

import android.content.Context
import com.hm.iou.base.mvp.HMBasePresenter
import com.hm.iou.lawyer.api.LawyerApi
import com.hm.iou.lawyer.bean.res.GetLawyerWalletResBean
import com.hm.iou.lawyer.business.NavigationHelper
import com.hm.iou.lawyer.dict.UpdateWalletBalanceEvent
import com.hm.iou.lawyer.event.LawyerOrderStatusChangedEvent
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.DecimalFormat

/**
 * @author : 借条管家-shilei
 * @version : 0.0.1
 * @Date : 2019-11-08 15:19
 * @E-Mail : afinalstone@foxmail.com
 */
class WalletPresenter(context: Context, view: WalletContract.View) :
    HMBasePresenter<WalletContract.View>(context, view),
    WalletContract.Presenter {

    private var mWalletInfo: GetLawyerWalletResBean? = null
    private var mNeedRefresh = false

    init {
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }


    override fun refreshWalletInfo() {
        launch {
            try {
                val result = handleResponse(LawyerApi.getLawyerWallet())
                mWalletInfo = result
                mView.finishRefresh()
                mNeedRefresh = false
                result?.let {
                    val decimalFormat = DecimalFormat("#0.00")
                    mView.showWalletBalance(decimalFormat.format(it.walletBalance / 100f))
                    mView.showTotalProfit(decimalFormat.format(it.totalProfit / 100f))
                }
            } catch (e: Exception) {
                handleException(e)
                mView.finishRefresh()
            }
        }
    }

    override fun toWithdrawMoney() {
        mWalletInfo?.let {
            NavigationHelper.toWithdrawMoneyPage(mContext, it.walletBalance)
        }
    }

    override fun onResume() {
        if (mNeedRefresh) {
            refreshWalletInfo()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventUpdateWalletBalance(event: UpdateWalletBalanceEvent) {
        mNeedRefresh = true
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventUpdateWalletBalance(event: LawyerOrderStatusChangedEvent) {
        mNeedRefresh = true
    }


}
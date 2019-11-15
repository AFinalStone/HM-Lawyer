package com.hm.iou.lawyer.business.lawyer.workbench.wallet

import android.content.Context
import com.hm.iou.base.mvp.HMBasePresenter
import com.hm.iou.lawyer.api.LawyerApi
import com.hm.iou.lawyer.bean.res.GetLawyerWalletResBean
import com.hm.iou.lawyer.business.NavigationHelper
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
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

    override fun refreshWalletInfo() {
        launch {
            try {
                val result = handleResponse(LawyerApi.getLawyerWallet())
                mWalletInfo = result
                mView.finishRefresh()
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

}
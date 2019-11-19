package com.hm.iou.lawyer.business.lawyer.workbench.wallet

import com.hm.iou.base.mvp.BaseContract

/**
 * 我的钱包
 */
class WalletContract {

    interface View : BaseContract.BaseView {

        fun finishRefresh()

        /**
         * 钱包余额
         */
        fun showWalletBalance(money: String?)

        /**
         * 累计收入
         */
        fun showTotalProfit(money: String?)
    }

    interface Presenter : BaseContract.BasePresenter {
        /**
         * 初始化
         */
        fun refreshWalletInfo()

        fun onResume()

        fun toWithdrawMoney()
    }
}
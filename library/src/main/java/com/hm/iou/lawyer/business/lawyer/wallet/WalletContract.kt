package com.hm.iou.lawyer.business.lawyer.wallet

import com.hm.iou.base.mvp.BaseContract

/**
 * 我的钱包
 */
class WalletContract {

    interface View : BaseContract.BaseView {

        /**
         * 钱包余额
         */
        fun showRemainderMoney(money: String?)

        /**
         * 累计收入
         */
        fun showTotalMoney(money: String?)
    }

    interface Presenter : BaseContract.BasePresenter {
        /**
         * 初始化
         */
        fun init()
    }
}
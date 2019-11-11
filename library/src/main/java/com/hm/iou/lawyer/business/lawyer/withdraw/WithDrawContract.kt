package com.hm.iou.lawyer.business.lawyer.withdraw

import com.hm.iou.base.mvp.BaseContract

/**
 * 我的钱包
 */
class WithDrawContract {

    interface View : BaseContract.BaseView {

        /**
         * 显示银行卡
         */
        fun showBankcard(bankCard: String?, bankName: String?)

        /**
         * 钱包余额
         */
        fun showRemainderMoney(money: String?)

    }

    interface Presenter : BaseContract.BasePresenter {
        /**
         * 初始化
         */
        fun init()
    }
}
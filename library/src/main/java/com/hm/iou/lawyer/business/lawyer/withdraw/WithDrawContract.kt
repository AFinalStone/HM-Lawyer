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

        /**
         * 显示提现确认对话框
         */
        fun showWithDrawDialog(
            withDrawRealMoney: String?,
            withDrawTotalMoney: String?,
            serviceMoney: String?,
            serviceRate: String?
        )
    }

    interface Presenter : BaseContract.BasePresenter {
        /**
         * 初始化
         */
        fun init()

        fun onResume()

        /**
         * 计算实际提现金额
         */
        fun calcWithDrawMoney(withDrawMoney: Float)

        /**
         * 提现
         */
        fun withDrawMoney(withDrawMoney: Float)
    }
}
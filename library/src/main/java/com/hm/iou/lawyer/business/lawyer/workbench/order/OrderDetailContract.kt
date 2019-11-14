package com.hm.iou.lawyer.business.lawyer.workbench.order

import com.hm.iou.base.mvp.BaseContract

/**
 * 我的钱包
 */
class OrderDetailContract {

    interface View : BaseContract.BaseView {


    }

    interface Presenter : BaseContract.BasePresenter {
        /**
         * 初始化
         */
        fun init()
    }
}
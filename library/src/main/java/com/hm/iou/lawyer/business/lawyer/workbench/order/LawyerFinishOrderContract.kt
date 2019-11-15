package com.hm.iou.lawyer.business.lawyer.workbench.order

import com.hm.iou.base.mvp.BaseContract

/**
 * 律师首页前置页面
 */
class LawyerFinishOrderContract {

    interface View : BaseContract.BaseView {
        /**
         * 快递单列表
         */
        fun showMailList(list: ArrayList<String>)
    }

    interface Presenter : BaseContract.BasePresenter {

        /**
         * 完成订单
         */
        fun finishOrder(
            billId: String,
            expressName: String,
            expressNumber: String,
            finishImgs: List<String>
        )

        fun getMailList()
    }
}
package com.hm.iou.lawyer.business.lawyer.workbench.order

import com.hm.iou.base.mvp.BaseContract
import com.hm.iou.lawyer.bean.res.LawyerLetterDetailBean

/**
 * 我的钱包
 */
class OrderDetailContract {

    interface View : BaseContract.BaseView {


        fun showInitView()

        fun hideInitView()

        fun showInitFailed(msg: String)
        /**
         * 订单详情
         */
        fun showDetail(detail: LawyerLetterDetailBean)

    }

    interface Presenter : BaseContract.BasePresenter {

        fun init(orderId: String, relationId: Int?)

        fun onResume()

        /**
         * 获取订单详情
         */
        fun getOrderDetail()

        /**
         * 律师接受订单
         */
        fun acceptOrder()

        /**
         * 律师拒绝订单
         */
        fun resumeOrder()

        /**
         * 律师拒绝订单
         */
        fun cancelOrder()
    }
}
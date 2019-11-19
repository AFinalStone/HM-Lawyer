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

        /**
         * 提示用户及时更新年检信息，可以接单
         */
        fun showNeedUpdateYearCheckByCanAcceptOrder(msg: String?)

        /**
         * 提示用户需要更新年检信息，不可以接单
         */
        fun showNeedUpdateYearCheckByNotCanAcceptOrder(msg: String?)

        /**
         * 提示无法接单
         */
        fun showNotCanAcceptOrder(msg: String?)

    }

    interface Presenter : BaseContract.BasePresenter {

        fun init(orderId: String, relationId: Int?)

        fun onResume()

        /**
         * 获取订单详情
         */
        fun getOrderDetail()

        /**
         * 校验能否接单
         */
        fun checkCanAcceptOrder()

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
package com.hm.iou.lawyer.business.lawyer.workbench.order


import com.hm.iou.base.mvp.BaseContract
import com.hm.iou.lawyer.bean.res.LawyerConsultOrderDetailBean
import com.hm.iou.lawyer.business.comm.IAnswer

/**
 * Created by hjy on 2019/11/22
 *
 * 律师咨询订单详情
 */
interface ConsultDetailContract {

    interface View : BaseContract.BaseView {

        fun showInitView()

        fun hideInitView()

        fun showInitFailed(msg: String)
        /**
         * 显示详情
         */
        fun showDetail(detail: LawyerConsultOrderDetailBean)

        /**
         * 显示列表
         */
        fun showAnswerList(list: ArrayList<IAnswer>)

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

        /**
         * 初始化
         */
        fun init(orderId: String, relationId: Int?)


        fun onResume()

        /**
         * 刷新详情
         */
        fun getOrderDetail()

        /**
         * 刷新解答列表
         */
        fun getAnswerList()

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
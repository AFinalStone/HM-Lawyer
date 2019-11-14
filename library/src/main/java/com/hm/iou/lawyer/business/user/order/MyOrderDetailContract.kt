package com.hm.iou.lawyer.business.user.order

import com.hm.iou.base.mvp.BaseContract

/**
 * Created by hjy on 2019/11/12
 *
 * 我的订单详情页面
 */
interface MyOrderDetailContract {

    interface View : BaseContract.BaseView {

        fun showInitLoading(show: Boolean)

        fun showInitFail(error: String?)

        /**
         * 显示订单状态
         */
        fun showOrderStatus(statusStr: String?)

        /**
         * 显示订单报价
         */
        fun showOrderPrice(priceStr: String?)

        /**
         * 关于时间的选项是否显示或隐藏
         */
        fun showOrHideTimeView(show: Boolean)

        /**
         * 显示订单取消时间
         */
        fun showOrderCancelTime(time: String?, returnMoneyTips: String?)

        /**
         * 显示订单完成时间
         */
        fun showOrderCompleteTime(time: String?)

        /**
         * 是否隐藏或显示律师信息View
         */
        fun showOrHideLawyerInfoView(show: Boolean)

        /**
         * 显示律师信息
         */
        fun showLawyerInfo(avatar: String?, name: String?, ageLimit: String?, company: String?)

        /**
         * 显示律师函收件人信息
         */
        fun showLetterReceiverInfo(name: String?, mobile: String?, idNo: String?, address: String?)

        //显示订单描述
        fun showOrderDesc(desc: String?)

        /**
         * 显示律师函图片资料
         */
        fun showOrderImages(list: List<String>?)

        /**
         * 显示或隐藏快递信息
         */
        fun showOrHideExpressInfoView(show: Boolean)

        //快递名称
        fun showExpressName(name: String?)

        //快递单号
        fun showExpressNo(no: String?)

        //律师函快递图片
        fun showExpressImg(list: List<String>?)

        //是否显示服务评分
        fun showOrHideServiceRatingView(show: Boolean)

        fun showServiceAttitudeRating(rating: Int)

        fun showServiceProfessionalRating(rating: Int)

        fun showOrHideBottomBtn(show: Boolean)

        fun showBottomBtn(btnText: String, callback: () -> Unit)

        fun showTopBarMenu(menuText: String, callback: () -> Unit)

        fun showCommDialog(
            title: String?, msg: String?, posBtn: String? = "取消",
            negBtn: String? = "确定", callback: (pos: Boolean) -> Unit
        )
    }

    interface Presenter : BaseContract.BasePresenter {

        fun getOrderDetail(orderId: String)

        fun toSeeLawyerInfo()
    }

}
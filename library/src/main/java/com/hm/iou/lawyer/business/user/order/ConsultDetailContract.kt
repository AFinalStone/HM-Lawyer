package com.hm.iou.lawyer.business.user.order

import com.hm.iou.base.mvp.BaseContract
import com.hm.iou.lawyer.business.comm.IAnswer

/**
 * Created by hjy on 2019/11/22
 *
 * 律师咨询订单详情
 */
interface ConsultDetailContract {

    interface View : BaseContract.BaseView {

        fun showInitLoading(show: Boolean)

        fun showInitFail(error: String?)

        /**
         * 显示订单状态
         */
        fun showOrderStatus(statusStr: String?)

        /**
         * 显示订单取消后的提示信息
         */
        fun showOrderCancelTips(visibility: Int, returnMoneyTips: String?)


        /**
         * 显示订单时间
         *
         * @param timeLabel "订单时间"或者"取消时间"等
         * @param orderTime
         */
        fun showOrderTime(timeLabel: String?, orderTime: String?)

        /**
         * 显示订单报价
         */
        fun showOrderPrice(priceStr: String?)

        //显示订单描述
        fun showOrderDesc(desc: String?)

        /**
         * 显示律师函图片资料
         */
        fun showOrderImages(list: List<String>?)

        /**
         * 显示或隐藏问答部分View
         */
        fun showOrHideAnswerContentView(visibility: Int)

        /**
         * 显示是否等待律师接单状态
         */
        fun showLawyerStatus(status: String?)

        /**
         * 显示律师信息
         */
        fun showLawyerInfo(avatar: String?, name: String?, ageLimit: String?, company: String?)

        fun showLawyerAnswerLabel(visibility: Int)

        fun showLawyerAnswerList(list: List<IAnswer>?)

        //是否显示服务评分
        fun showOrHideServiceRatingView(show: Boolean)

        //显示服务态度评分
        fun showServiceAttitudeRating(rating: Int)

        //显示专业知识评分
        fun showServiceProfessionalRating(rating: Int)

        fun showOrHideMainBtn(show: Boolean)

        fun showBottomMainBtn(btnText: String, callback: () -> Unit)

        fun showSecondBtn(btnText: String, callback: () -> Unit)

        fun showCommDialog(
            title: String?, msg: String?, posBtn: String? = "取消",
            negBtn: String? = "确定", callback: (pos: Boolean) -> Unit
        )

        /**
         * 滚动到底部
         */
        fun scrollToBottom()

    }

    interface Presenter : BaseContract.BasePresenter {

        fun getOrderDetail(orderId: String)

        fun toSeeLawyerInfo()

    }

}
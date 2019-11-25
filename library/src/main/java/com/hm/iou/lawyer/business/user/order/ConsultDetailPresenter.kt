package com.hm.iou.lawyer.business.user.order

import android.content.Context
import android.view.View
import com.hm.iou.base.mvp.HMBasePresenter
import com.hm.iou.lawyer.business.NavigationHelper
import com.hm.iou.lawyer.business.comm.IAnswer
import com.hm.iou.lawyer.event.RatingLawyerSuccEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Created by hjy on 2019-11-22
 *
 * 律师咨询详情
 */
class ConsultDetailPresenter(context: Context, view: ConsultDetailContract.View) :
    HMBasePresenter<ConsultDetailContract.View>(context, view), ConsultDetailContract.Presenter {

    override fun getOrderDetail(orderId: String) {
        launch {
            mView.showInitLoading(true)
            delay(1000)

            mView.showInitLoading(false)

            mView.showOrderStatus("等待律师接单")
            mView.showOrderCancelTips(View.VISIBLE, "您的退款将在3个工作日内退回支付账户")
            mView.showOrderTime("订单时间", "2019.11.23 12:10:12")
            mView.showOrderPrice("￥100.00")
            mView.showOrderDesc("朋友准备向我借钱，但是不知道借条是否合法，想请律师查看。")

            val imgList = mutableListOf<String>()
            imgList.add("https://pics2.baidu.com/feed/a8014c086e061d9530dd23d5821ec6d460d9cab0.jpeg?token=dac51de6a3847ba35ce74f95da68d63f&s=DF0009C04816B9D452CC259B03009002")
            imgList.add("https://pics2.baidu.com/feed/a8014c086e061d9530dd23d5821ec6d460d9cab0.jpeg?token=dac51de6a3847ba35ce74f95da68d63f&s=DF0009C04816B9D452CC259B03009002")
            mView.showOrderImages(imgList)


            mView.showOrHideAnswerContentView(View.VISIBLE)
            mView.showLawyerStatus("等待接单")
            mView.showLawyerInfo(null, "张三律师", "执业7年", "杭州泰杭律师事务所")

            mView.showLawyerAnswerLabel(View.VISIBLE)
            val answerList = mutableListOf<IAnswer>()
            for (i in 0..4) {
                answerList.add(object : IAnswer {
                    override fun getAvatar(): String? {
                        return null
                    }

                    override fun getName(): String? {
                        return "张三律师"
                    }

                    override fun getTime(): String? {
                        return "2019.11.12 12:12:12"
                    }

                    override fun getAnswer(): String? {
                        return "律师回答的内容律师回答的内容律师回答的内容律师回答的内容律师回答的内容律师回答的内容"
                    }
                })
            }
            mView.showLawyerAnswerList(answerList)

            mView.showOrHideMainBtn(true)
            /*mView.showBottomMainBtn("评价律师") {
                NavigationHelper.toRatingLawyerPage(mContext, "")
            }*/
            mView.showBottomMainBtn("重新下单") {

            }

            mView.showSecondBtn("补充问题") {
                NavigationHelper.toAddConsultQuestion(mContext, "")
            }

            mView.showOrHideServiceRatingView(true)
            mView.showServiceAttitudeRating(4)
            mView.showServiceProfessionalRating(5)


            mView.scrollToBottom()
        }

    }

    override fun toSeeLawyerInfo() {
        /*mDetailInfo?.lawyerAbout?.lawyerId?.let {
            NavigationHelper.toLawyerDetailPage(mContext, it)
        }*/
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventRatingLawyerSucc(event: RatingLawyerSuccEvent) {
        /*if (mOrderId == event.billId) {
            getOrderDetail(mOrderId ?: "")
        }*/
    }

}
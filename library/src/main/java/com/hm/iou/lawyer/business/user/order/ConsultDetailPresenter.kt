package com.hm.iou.lawyer.business.user.order

import android.content.Context
import android.view.View
import com.hm.iou.base.mvp.HMBasePresenter
import com.hm.iou.lawyer.api.LawyerApi
import com.hm.iou.lawyer.bean.res.LawyerConsultDetailResBean
import com.hm.iou.lawyer.business.NavigationHelper
import com.hm.iou.lawyer.business.comm.IAnswer
import com.hm.iou.lawyer.dict.OrderStatus
import com.hm.iou.lawyer.event.ConsultAddSuccEvent
import com.hm.iou.lawyer.event.RatingLawyerSuccEvent
import com.hm.iou.lawyer.event.UserOrderStatusChangedEvent
import com.hm.iou.network.exception.ApiException
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.SimpleDateFormat

/**
 * Created by hjy on 2019-11-22
 *
 * 律师咨询详情
 */
class ConsultDetailPresenter(context: Context, view: ConsultDetailContract.View) :
    HMBasePresenter<ConsultDetailContract.View>(context, view), ConsultDetailContract.Presenter {

    private var mOrderId: String? = null
    private var mDetailInfo: LawyerConsultDetailResBean? = null

    init {
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun getOrderDetail(orderId: String) {
        mOrderId = orderId
        launch {
            mView.showInitLoading(true)
            try {
                val data = handleResponse(LawyerApi.getConsultDetail(orderId))
                mDetailInfo = data
                data ?: return@launch

                when (data.status) {
                    OrderStatus.WAIT.status -> {
                        mView.showOrderStatus("等待律师接单")
                        mView.showOrderCancelTips(View.GONE, null)
                        mView.showOrderTime("订单时间", data.doDate?.replace("-", "."))
                    }
                    OrderStatus.ONGOING.status -> {
                        mView.showOrderStatus("律师已接单")
                        mView.showOrderCancelTips(View.GONE, null)
                        mView.showOrderTime("订单时间", data.doDate?.replace("-", "."))
                    }
                    OrderStatus.COMPLETE.status -> {
                        mView.showOrderStatus("订单已完成")
                        mView.showOrderCancelTips(View.GONE, null)
                        mView.showOrderTime("完成时间", data.doDate?.replace("-", "."))
                    }
                    OrderStatus.CANCEL.status -> {
                        mView.showOrderStatus("订单已取消")
                        mView.showOrderCancelTips(View.VISIBLE, "您的退款将在3个工作日内退回支付账户")
                        mView.showOrderTime("取消时间", data.doDate?.replace("-", "."))
                    }
                    else -> {
                        mView.showOrderStatus("")
                        mView.showOrderCancelTips(View.GONE, null)
                        mView.showOrderTime("订单时间", data.doDate?.replace("-", "."))
                    }
                }

                mView.showOrderPrice("¥ ${data.price}")
                mView.showOrderDesc(data.caseDescription)

                val imgList = mutableListOf<String>()
                data.fileList?.run {
                    forEach { item ->
                        if (!(item.url).isNullOrEmpty())
                            imgList.add(item.url!!)
                    }
                }
                mView.showOrderImages(imgList)

                mView.showOrHideAnswerContentView(View.GONE)
                mView.showLawyerAnswerLabel(View.GONE)
                mView.showOrHideServiceRatingView(false)

                when (data.status) {
                    OrderStatus.WAIT.status -> {            //等待接单，可以取消
                        mView.showOrHideMainBtn(true)
                        mView.showBottomMainBtn("取消订单") {
                            showCancelOrderDialog()
                        }
                        data.lawyerAbout?.let {
                            //等待指定律师接单
                            mView.showOrHideAnswerContentView(View.VISIBLE)
                            mView.showLawyerStatus("等待接单")
                            mView.showLawyerInfo(
                                it.image,
                                "${it.name}律师",
                                "执业${it.lawYear}年",
                                it.lawFirm
                            )
                            mView.showLawyerAnswerLabel(View.GONE)
                        }
                    }
                    OrderStatus.ONGOING.status -> {     //进行中
                        mView.showOrHideAnswerContentView(View.VISIBLE)
                        mView.showLawyerStatus("接单律师")
                        data.lawyerAbout?.let {
                            mView.showLawyerInfo(
                                it.image,
                                "${it.name}律师",
                                "执业${it.lawYear}年",
                                it.lawFirm
                            )
                        }
                        mView.showLawyerAnswerLabel(View.VISIBLE)

                        if (data.canCancel) {
                            mView.showOrHideMainBtn(true)
                            mView.showBottomMainBtn("取消订单") {
                                showCancelOrderDialog()
                            }
                        } else {
                            mView.showOrHideMainBtn(false)
                        }
                        showWaitingLawyerAnswer()
                    }
                    OrderStatus.COMPLETE.status -> {    //已完成
                        mView.showOrHideAnswerContentView(View.VISIBLE)
                        mView.showLawyerStatus("接单律师")
                        data.lawyerAbout?.let {
                            mView.showLawyerInfo(
                                it.image,
                                "${it.name}律师",
                                "执业${it.lawYear}年",
                                it.lawFirm
                            )
                        }
                        mView.showLawyerAnswerLabel(View.VISIBLE)

                        val serviceRating = data.evaluation
                        if (serviceRating == null) {
                            mView.showOrHideServiceRatingView(false)
                            mView.showOrHideMainBtn(true)
                            mView.showBottomMainBtn("评价律师") {
                                NavigationHelper.toRatingLawyerPage(mContext, orderId)
                            }
                        } else {
                            mView.showOrHideServiceRatingView(true)
                            mView.showServiceAttitudeRating(serviceRating.attitudeScore)
                            mView.showServiceProfessionalRating(serviceRating.professionalScore)
                            mView.showOrHideMainBtn(false)
                        }

                        mView.showSecondBtn("补充问题") {
                            NavigationHelper.toAddConsultQuestion(mContext, mOrderId ?: "")
                        }

                        //获取反馈回复列表
                        getReplyList()
                    }
                    OrderStatus.CANCEL.status -> {      //已取消
                        mView.showOrHideMainBtn(true)
                        mView.showBottomMainBtn("重新下单") {
                            NavigationHelper.toCreateLawyerConsultPage(
                                mContext,
                                data.lawyerAbout?.lawyerId,
                                data.price,
                                data.caseDescription,
                                data.fileList
                            )
                        }
                        data.lawyerAbout?.let {
                            //等待指定律师接单
                            mView.showOrHideAnswerContentView(View.VISIBLE)
                            mView.showLawyerStatus("等待接单")
                            mView.showLawyerInfo(
                                it.image,
                                "${it.name}律师",
                                "执业${it.lawYear}年",
                                it.lawFirm
                            )
                            mView.showLawyerAnswerLabel(View.GONE)
                        }
                    }
                }
                mView.showInitLoading(false)
            } catch (e: Exception) {
                handleException(e, showBusinessError = false, showCommError = false)
                if (e is ApiException) {
                    mView.showInitFail(e.message)
                } else {
                    mView.showInitFail("数据加载失败，请重试")
                }
            }
        }

    }

    /**
     * 显示取消订单确认弹窗
     */
    private fun showCancelOrderDialog() {
        mView.showCommDialog(null, "是否取消该订单", "放弃取消", "确定取消") {
            if (!it) {
                launch {
                    mView.showLoadingView()
                    try {
                        handleResponse(LawyerApi.cancelCustLawyerLetter(mOrderId ?: ""))
                        mView.dismissLoadingView()
                        EventBus.getDefault().post(UserOrderStatusChangedEvent())
                        getOrderDetail(mOrderId ?: "")
                    } catch (e: Exception) {
                        mView.dismissLoadingView()
                        handleException(e)
                    }
                }
            }
        }
    }

    /**
     * 律师接单后，显示 '律师正在为您解答中'
     */
    private fun showWaitingLawyerAnswer() {
        val list = mutableListOf<IAnswer>()
        list.add(object : IAnswer {
            override fun getAvatar(): String? = mDetailInfo?.lawyerAbout?.image

            override fun getName(): String? = "${mDetailInfo?.lawyerAbout?.name}律师"

            override fun getTime(): String? = null

            override fun getAnswer(): String? = "等待律师为您解答..."
        })
        mView.showLawyerAnswerList(list)
    }

    /**
     * 获取反馈回复列表
     */
    private fun getReplyList() {
        launch {
            try {
                val result = handleResponse(LawyerApi.getConsultReplayList(mOrderId ?: ""))?.replies
                val list = mutableListOf<IAnswer>()
                val sdf1 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                val sdf2 = SimpleDateFormat("yyyy.MM.dd HH:mm")
                result?.forEach {
                    val item = object : IAnswer {

                        var formatStr: String? = null

                        override fun getAvatar(): String? {
                            return it.avatar
                        }

                        override fun getName(): String? {
                            return it.name
                        }

                        override fun getTime(): String? {
                            if (formatStr.isNullOrEmpty()) {
                                try {
                                    val date = sdf1.parse(it.createTime)
                                    formatStr = sdf2.format(date)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                            return formatStr
                        }

                        override fun getAnswer(): String? {
                            return it.msg
                        }
                    }
                    list.add(item)
                }
                mView.showLawyerAnswerList(list)

                //延迟之后，再滚动到底部
                delay(500)
                println("ok scroll to bottom")
                mView.scrollToBottom()
            } catch (e: Exception) {
                handleException(e)
                mView.toastMessage("律师解答获取失败")
            }
        }
    }

    override fun toSeeLawyerInfo() {
        mDetailInfo?.lawyerAbout?.lawyerId?.let {
            NavigationHelper.toLawyerDetailPage(mContext, it)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventRatingLawyerSucc(event: RatingLawyerSuccEvent) {
        if (mOrderId == event.billId) {
            getOrderDetail(mOrderId ?: "")
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventQuestionAddSucc(event: ConsultAddSuccEvent) {
        getReplyList()
    }

}
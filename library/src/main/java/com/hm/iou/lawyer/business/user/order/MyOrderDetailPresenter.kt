package com.hm.iou.lawyer.business.user.order

import android.content.Context
import com.hm.iou.base.mvp.HMBasePresenter
import com.hm.iou.lawyer.api.LawyerApi
import com.hm.iou.lawyer.bean.res.CustLetterDetailResBean
import com.hm.iou.lawyer.business.NavigationHelper
import com.hm.iou.lawyer.dict.OrderStatus
import com.hm.iou.lawyer.event.RatingLawyerSuccEvent
import com.hm.iou.lawyer.event.UserOrderStatusChangedEvent
import com.hm.iou.network.exception.ApiException
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Created by hjy on 2019/11/12
 *
 * 律师函订单详情
 */
class MyOrderDetailPresenter(context: Context, view: MyOrderDetailContract.View) :
    HMBasePresenter<MyOrderDetailContract.View>(context, view),
    MyOrderDetailContract.Presenter {

    private var mOrderId: String? = null
    private var mDetailInfo: CustLetterDetailResBean? = null

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
                val data = handleResponse(LawyerApi.getCustLawyerLetterDetail(orderId))
                mDetailInfo = data
                data ?: return@launch

                when(data.status) {
                    OrderStatus.WAIT.status -> {
                        mView.showOrderStatus("等待${data.lawyerAbout?.run { name } ?: ""}律师接单")
                        mView.showOrHideTimeView(false)
                    }
                    OrderStatus.ONGOING.status -> {
                        mView.showOrderStatus("律师已接单")
                        mView.showOrHideTimeView(false)
                    }
                    OrderStatus.COMPLETE.status -> {
                        mView.showOrderStatus("订单已完成")
                        mView.showOrHideTimeView(true)
                        mView.showOrderCompleteTime(data.doDate?.replace("-", "."))
                    }
                    OrderStatus.CANCEL.status -> {
                        mView.showOrderStatus("订单已取消")
                        mView.showOrHideTimeView(true)
                        mView.showOrderCancelTime(
                            data.doDate?.replace("-", "."),
                            "您的退款将在3个工作日内退回支付账户"
                        )
                    }
                    else -> {
                        mView.showOrderStatus("")
                    }
                }
                mView.showOrderPrice("¥ ${data.price}")
                if (data.lawyerAbout != null) {
                    mView.showOrHideLawyerInfoView(true)
                    data.lawyerAbout?.let {
                        mView.showLawyerInfo(it.image, "${it.name}律师","执业${it.lawYear}年", it.lawFirm)
                    }
                } else {
                    mView.showOrHideLawyerInfoView(false)
                }
                data.receiveInfo?.let {
                    mView.showLetterReceiverInfo(
                        it.receiverName,
                        it.receiverMobile,
                        it.receiverIdCardNum,
                        "${it.receiverCityDetail}${it.receiverDetailAddress}"
                    )
                }
                mView.showOrderDesc(data.caseDescription)
                val list = mutableListOf<String>()
                data.fileList?.run {
                    forEach { item ->
                        if (!(item.url).isNullOrEmpty())
                            list.add(item.url!!)
                    }
                }
                mView.showOrderImages(list)

                val expressInfo = data.letterFinishInfo
                if (expressInfo == null) {
                    mView.showOrHideExpressInfoView(false)
                } else {
                    mView.showOrHideExpressInfoView(true)
                    mView.showExpressName(expressInfo.expressName)
                    mView.showExpressNo(expressInfo.expressNumber)
                    mView.showExpressImg(expressInfo.finishImgs)
                }

                val serviceRating = data.evaluation
                if (serviceRating == null) {
                    mView.showOrHideServiceRatingView(false)
                } else {
                    mView.showOrHideServiceRatingView(true)
                    mView.showServiceAttitudeRating(serviceRating.attitudeScore)
                    mView.showServiceProfessionalRating(serviceRating.professionalScore)
                }

                when(data.status) {
                    OrderStatus.WAIT.status -> {
                        mView.showOrHideBottomBtn(false)
                        mView.showTopBarMenu("取消订单") {
                            //点击取消
                            mView.showCommDialog(null, "是否取消该订单") {
                                if (!it) {
                                    cancelOrder(mOrderId ?: "")
                                }
                            }
                        }
                    }
                    OrderStatus.COMPLETE.status -> {
                        mView.showTopBarMenu("") {}
                        if (serviceRating == null) {
                            mView.showOrHideBottomBtn(true)
                            mView.showBottomBtn("评价律师") {
                                NavigationHelper.toRatingLawyerPage(mContext, orderId)
                            }
                        } else {
                            mView.showOrHideBottomBtn(false)
                        }
                    }
                    OrderStatus.CANCEL.status -> {
                        mView.showOrHideBottomBtn(true)
                        mView.showTopBarMenu("") {}
                        mView.showBottomBtn("重新下单") {
                            mDetailInfo?.let {
                                NavigationHelper.toCreateLawyerLetter(mContext, it)
                            }
                        }
                    }
                    else -> {
                        mView.showTopBarMenu("") {}
                        mView.showOrHideBottomBtn(false)
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

    override fun toSeeLawyerInfo() {
        mDetailInfo?.lawyerAbout?.lawyerId?.let {
            NavigationHelper.toLawyerDetailPage(mContext, it)
        }
    }

    /**
     * 取消订单
     */
    private fun cancelOrder(orderId: String) {
        launch {
            mView.showLoadingView()
            try {
                handleResponse(LawyerApi.cancelCustLawyerLetter(orderId))
                mView.dismissLoadingView()
                EventBus.getDefault().post(UserOrderStatusChangedEvent())
                getOrderDetail(orderId)
            } catch (e: Exception) {
                mView.dismissLoadingView()
                handleException(e)
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventRatingLawyerSucc(event: RatingLawyerSuccEvent) {
        if (mOrderId == event.billId) {
            getOrderDetail(mOrderId ?: "")
        }
    }

}
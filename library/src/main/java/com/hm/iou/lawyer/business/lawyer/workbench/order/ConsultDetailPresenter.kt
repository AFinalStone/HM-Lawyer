package com.hm.iou.lawyer.business.lawyer.workbench.order

import android.content.Context
import com.hm.iou.base.mvp.HMBasePresenter
import com.hm.iou.lawyer.api.LawyerApi
import com.hm.iou.lawyer.bean.res.LawyerConsultOrderAnswerItemBean
import com.hm.iou.lawyer.business.comm.IAnswer
import com.hm.iou.lawyer.event.LawyerOrderStatusChangedEvent
import com.hm.iou.network.exception.ApiException
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 *
 * 律师咨询详情
 */
class ConsultDetailPresenter(context: Context, view: ConsultDetailContract.View) :
    HMBasePresenter<ConsultDetailContract.View>(context, view), ConsultDetailContract.Presenter {


    private var mOrderId: String = ""
    private var mRelationId: Int? = null
    private var mNeedRefresh = false

    init {
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }


    override fun init(orderId: String, relationId: Int?) {
        mOrderId = orderId
        mRelationId = relationId
        getOrderDetail()
    }

    override fun onResume() {
        if (mNeedRefresh) {
            getOrderDetail()
        }
    }

    override fun getOrderDetail() {
        launch {
            try {
                mView.showInitView()
                val result =
                    handleResponse(LawyerApi.getLawyerConsultOrderDetail(mOrderId, mRelationId))
                mView.hideInitView()
                if (result == null) {
                    mView.toastErrorMessage("发生异常")
                    mView.closeCurrPage()
                } else {
                    mView.showDetail(result)
                    mNeedRefresh = false
                }
            } catch (e: Exception) {
                if (e is ApiException) {
                    handleException(e, showCommError = false, showBusinessError = false)
                    val msg = e.message ?: "初始化失败"
                    mView.showInitFailed(msg)
                } else {
                    handleException(e)
                    mView.closeCurrPage()
                }
            }
        }
    }

    override fun getAnswerList() {
        launch {
            try {
                mView.showAnswerListLoadingView()
                val list = handleResponse(LawyerApi.getConsultReplayList(mOrderId))?.replies
                mView.hideAnswerListLoadingView()
                list?.let {
                    mView.showAnswerList(convertData(list))
                    mNeedRefresh = false
                }
            } catch (e: Exception) {
                val msg = e.message ?: "初始化失败"
                mView.showAnswerListFailed(msg)
            }
        }
    }

    override fun checkCanAcceptOrder() {
        launch {
            try {
                mView.showLoadingView()
                val result = handleResponse(LawyerApi.checkLawyerCanAcceptOrder(mOrderId))
                when (result?.result) {
                    0 -> {//0=可以正常接单
                        acceptOrder()
                    }
                    1 -> {//1=可以接单，提示用户及时更新年检信息
                        mView.dismissLoadingView()
                        mView.showNeedUpdateYearCheckByCanAcceptOrder(result.note)
                    }
                    2 -> {//2=不可以接单，提示原因
                        mView.dismissLoadingView()
                        mView.showNotCanAcceptOrder(result.note)
                    }
                    3 -> {//3不可接单，提示用户需要更新年检
                        mView.dismissLoadingView()
                        mView.showNeedUpdateYearCheckByNotCanAcceptOrder(result.note)
                    }
                    else -> {
                        mView.dismissLoadingView()
                    }
                }
            } catch (e: Exception) {
                mView.dismissLoadingView()
                handleException(e)
            }
        }
    }


    override fun acceptOrder() {
        launch {
            try {
                mView.showLoadingView()
                handleResponse(LawyerApi.lawyerAcceptOrder(mOrderId))
                EventBus.getDefault().post(LawyerOrderStatusChangedEvent())
                mView.dismissLoadingView()
                mView.toastMessage("接单成功")
                getOrderDetail()
            } catch (e: Exception) {
                mView.dismissLoadingView()
                handleException(e)
            }
        }
    }

    override fun resumeOrder() {
        launch {
            try {
                mView.showLoadingView()
                val relationId = handleResponse(LawyerApi.lawyerRefuseOrder(mOrderId))
                if (relationId != null) {
                    mRelationId = relationId
                }
                EventBus.getDefault().post(LawyerOrderStatusChangedEvent())
                mView.dismissLoadingView()
                mView.toastMessage("订单已拒绝")
                getOrderDetail()
            } catch (e: Exception) {
                mView.dismissLoadingView()
                handleException(e)
            }
        }
    }

    override fun cancelOrder() {
        launch {
            try {
                mView.showLoadingView()
                val relationId = handleResponse(LawyerApi.lawyerCancelOrder(mOrderId))
                if (relationId != null) {
                    mRelationId = relationId
                }
                EventBus.getDefault().post(LawyerOrderStatusChangedEvent())
                mView.dismissLoadingView()
                mView.toastMessage("取消成功")
                getOrderDetail()
            } catch (e: Exception) {
                mView.dismissLoadingView()
                handleException(e)
            }
        }
    }

    private fun convertData(list: List<LawyerConsultOrderAnswerItemBean>?): List<IAnswer> {
        val result = ArrayList<IAnswer>()
        if (!list.isNullOrEmpty()) {
            for (item in list) {
                result.add(object : IAnswer {
                    override fun getAvatar(): String? = item.avatar

                    override fun getName(): String? = item.name

                    override fun getTime(): String? = item.createTime?.replace("-", ".")

                    override fun getAnswer(): String? = item.msg
                })
            }
        }
        return result
    }

    //订单状态更改了之后，比如用户取消了该订单
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventOrderStatusChanged(event: LawyerOrderStatusChangedEvent) {
        mNeedRefresh = true
    }

}
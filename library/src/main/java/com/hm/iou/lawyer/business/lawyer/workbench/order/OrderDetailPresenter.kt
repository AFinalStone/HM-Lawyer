package com.hm.iou.lawyer.business.lawyer.workbench.order

import android.content.Context
import com.hm.iou.base.mvp.HMBasePresenter
import com.hm.iou.lawyer.api.LawyerApi
import com.hm.iou.lawyer.event.LawyerOrderStatusChangedEvent
import com.hm.iou.network.exception.ApiException
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @author : 借条管家-shilei
 * @version : 0.0.1
 * @Date : 2019-11-08 15:19
 * @E-Mail : afinalstone@foxmail.com
 */
class OrderDetailPresenter(context: Context, view: OrderDetailContract.View) :
    HMBasePresenter<OrderDetailContract.View>(context, view),
    OrderDetailContract.Presenter {


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
                val result = handleResponse(LawyerApi.getLawyerLetterDetail(mOrderId, mRelationId))
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

    override fun acceptOrder() {
        launch {
            try {
                mView.showLoadingView()
                handleResponse(LawyerApi.lawyerAcceptOrder(mOrderId))
                mView.dismissLoadingView()
                mView.toastMessage("操作成功")
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
                handleResponse(LawyerApi.lawyerRefuseOrder(mOrderId))
                mView.dismissLoadingView()
                mView.toastMessage("操作成功")
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
                handleResponse(LawyerApi.lawyerCancelOrder(mOrderId))
                mView.dismissLoadingView()
                mView.toastMessage("操作成功")
                getOrderDetail()
            } catch (e: Exception) {
                mView.dismissLoadingView()
                handleException(e)
            }
        }
    }

    //订单状态更改了之后，比如用户取消了该订单
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventOrderStatusChanged(event: LawyerOrderStatusChangedEvent) {
        mNeedRefresh = true
    }
}
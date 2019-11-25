package com.hm.iou.lawyer.business.lawyer.workbench.order

import android.content.Context
import com.hm.iou.base.mvp.HMBasePresenter
import com.hm.iou.lawyer.api.LawyerApi
import com.hm.iou.lawyer.event.LawyerOrderStatusChangedEvent
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Created by hjy on 2019-11-22
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

    }

    override fun getAnswerList() {

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

    //订单状态更改了之后，比如用户取消了该订单
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventOrderStatusChanged(event: LawyerOrderStatusChangedEvent) {
        mNeedRefresh = true
    }

}
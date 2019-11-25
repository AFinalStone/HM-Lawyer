package com.hm.iou.lawyer.business.user.order

import android.content.Context
import com.hm.iou.base.mvp.HMBasePresenter
import com.hm.iou.lawyer.R
import com.hm.iou.lawyer.api.LawyerApi
import com.hm.iou.lawyer.bean.res.CustOrderItemBean
import com.hm.iou.lawyer.dict.OrderStatus
import com.hm.iou.lawyer.dict.OrderType
import com.hm.iou.lawyer.event.AddLawyerLetterEvent
import com.hm.iou.lawyer.event.UserOrderStatusChangedEvent
import com.hm.iou.network.exception.ApiException
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.SimpleDateFormat

/**
 * Created by hjy on 2019/11/12
 *
 * 我的订单列表
 */
class MyOrderListPresenter(context: Context, view: MyOrderListContract.View) :
    HMBasePresenter<MyOrderListContract.View>(context, view), MyOrderListContract.Presenter {

    companion object {
        const val PAGE_SIZE = 10
    }

    private var mPageNo = 1
    private val mDataList: MutableList<IOrderItem> = mutableListOf()

    private var mNextPageJob: Job? = null

    init {
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun getFirstPage() {
        mNextPageJob?.cancel()
        launch {
            try {
                if (mDataList.isEmpty())
                    mView.showInitLoading(true)
                val result = handleResponse(LawyerApi.getCustOrderList(1, PAGE_SIZE))

                val dataList = result?.list
                mPageNo = 1
                mDataList.clear()
                val list = convertData(dataList)
                var currSize = list.size
                mDataList.addAll(list)
                mView.clearOrderList()
                mView.showOrderList(mDataList)
                mView.showInitLoading(false)
                mView.finishRefresh()
                if (mDataList.isEmpty()) {
                    mView.showDataEmpty(true)
                }
                if (currSize < PAGE_SIZE) {
                    mView.showLoadMoreEnd()
                } else {
                    mView.showLoadMoreComplete()
                }
            } catch (e: Exception) {
                handleException(e, showCommError = false, showBusinessError = false)
                mView.finishRefresh()
                if (mDataList.isEmpty()) {
                    mView.showInitError(e.message)
                } else {
                    mView.toastMessage(if (e is ApiException) e.message else "数据加载失败，请重试")
                }
            }
        }
    }

    override fun getNextPage() {
        mNextPageJob?.cancel()
        mNextPageJob = launch {
            try {
                val list = convertData(
                    handleResponse(
                        LawyerApi.getCustOrderList(
                            mPageNo + 1,
                            PAGE_SIZE
                        )
                    )?.list
                )
                var currSize = list.size
                mDataList.addAll(list)
                mView.showOrderList(mDataList)
                if (currSize < PAGE_SIZE) {
                    mView.showLoadMoreEnd()
                } else {
                    mView.showLoadMoreComplete()
                }
                mPageNo++
            } catch (e: Exception) {
                handleException(e, showCommError = false, showBusinessError = false)
                mView.showLoadMoreFail()
            }
        }
    }

    private fun convertData(list: List<CustOrderItemBean>?): List<IOrderItem> {
        val dataList = mutableListOf<IOrderItem>()
        list ?: return dataList
        val sdf1 = SimpleDateFormat("yyyy.MM.dd HH:mm")
        val sdf2 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        list.forEach { item ->
            dataList.add(object : IOrderItem {

                var formatTime: String? = null

                override fun getOrderId(): String? {
                    return item.billId
                }

                override fun getTime(): String? {
                    if (formatTime.isNullOrEmpty()) {
                        try {
                            formatTime = sdf1.format(sdf2.parse(item.createTime))
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    return formatTime
                }

                override fun getDesc(): String? {
                    return item.caseDescription
                }

                override fun getTypeStr(): String? {
                    return when(item.type) {
                        OrderType.Letter.type -> "律师函"
                        OrderType.Consult.type -> "律师咨询"
                        else -> ""
                    }
                }

                override fun getType(): Int {
                    return item.type
                }

                override fun getTypeBgResId(): Int {
                    return when(item.type) {
                        OrderType.Letter.type -> R.drawable.lawyer_bg_lawyer_order_list_item_type_letter
                        OrderType.Consult.type -> R.drawable.lawyer_bg_lawyer_order_list_item_type_consult
                        else -> 0
                    }
                }

                override fun getTypeTextColor(): Int {
                    return when(item.type) {
                        OrderType.Letter.type -> 0xFFBF9E6E.toInt()
                        OrderType.Consult.type -> 0xFF6398CA.toInt()
                        else -> 0
                    }
                }

                override fun getPrice(): String? {
                    return "报价：¥${item.price}"
                }

                //2待接单，3进行中，4已完成，5已取消
                override fun getStatusStr(): String? {
                    return when (item.status) {
                        OrderStatus.WAIT.status -> "待接单"
                        OrderStatus.ONGOING.status -> "已接单"
                        OrderStatus.COMPLETE.status -> "已完成"
                        OrderStatus.CANCEL.status -> "已取消"
                        else -> ""
                    }
                }
            })
        }
        return dataList
    }

    //订单状态更改了之后，比如用户取消了该订单
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventOrderStatusChanged(event: UserOrderStatusChangedEvent) {
        getFirstPage()
    }

    //创建成功之后，刷新页面
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventCreateLawyerLetter(event: AddLawyerLetterEvent) {
        getFirstPage()
    }

}
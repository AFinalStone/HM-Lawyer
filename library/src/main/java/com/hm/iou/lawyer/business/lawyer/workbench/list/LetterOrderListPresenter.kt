package com.hm.iou.lawyer.business.lawyer.workbench.list

import android.content.Context
import com.hm.iou.base.mvp.HMBasePresenter
import com.hm.iou.lawyer.api.LawyerApi
import com.hm.iou.lawyer.bean.res.LawyerOrderItem
import com.hm.iou.lawyer.dict.OrderStatus
import com.hm.iou.lawyer.event.LawyerOrderStatusChangedEvent
import com.hm.iou.network.exception.ApiException
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.SimpleDateFormat

/**
 * @author : 借条管家-shilei
 * @version : 0.0.1
 * @Date : 2019-11-08 15:19
 * @E-Mail : afinalstone@foxmail.com
 */
class LetterOrderListPresenter(context: Context, view: LetterOrderListContract.View) :
    HMBasePresenter<LetterOrderListContract.View>(context, view),
    LetterOrderListContract.Presenter {
    companion object {
        const val PAGE_SIZE = 10
    }

    private var mNeedRefresh = false
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

    override fun onResume() {
        if (mNeedRefresh) {
            getFirstPage()
        }
    }

    override fun getFirstPage() {
        mNextPageJob?.cancel()
        launch {
            try {
                if (mDataList.isEmpty())
                    mView.showInitLoading(true)
                val result = handleResponse(
                    LawyerApi.getLawyerLetterList(
                        1,
                        PAGE_SIZE
                    )
                )
                mNeedRefresh = false
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
                        LawyerApi.getLawyerLetterList(
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

    private fun convertData(list: List<LawyerOrderItem>?): List<IOrderItem> {
        val dataList = mutableListOf<IOrderItem>()
        list ?: return dataList
        val sdf1 = SimpleDateFormat("yyyy.MM.dd HH:mm")
        val sdf2 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        list.forEach { item ->
            dataList.add(object : IOrderItem {
                override fun getRelationId(): Int? {
                    return item.relationId
                }

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

                override fun getUserHeader(): String? {
                    return item.avatarUrl
                }

                override fun getUserName(): String? {
                    return item.name
                }

                override fun getDesc(): String? {
                    return item.caseDescription
                }

                override fun getTypeStr(): String? {
                    return if (1 == item.billType) "发律师函" else "律师咨询"
                }

                override fun getPrice(): String? {
                    return "¥${item.price}"
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
    fun onEventOrderStatusChanged(event: LawyerOrderStatusChangedEvent) {
        mNeedRefresh = true
    }


}
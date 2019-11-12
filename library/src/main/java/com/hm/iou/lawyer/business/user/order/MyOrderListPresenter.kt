package com.hm.iou.lawyer.business.user.order

import android.content.Context
import com.hm.iou.base.mvp.HMBasePresenter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

    override fun getFirstPage() {
        launch {
            try {
                if (mDataList.isEmpty())
                    mView.showInitLoading(true)

                delay(2000)

                mPageNo = 1
                mDataList.clear()
                val list = getData()
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
                if (mDataList.isEmpty())
                    mView.showInitError(e.message)
                mView.finishRefresh()
            }
        }
    }

    override fun getNextPage() {
        launch {
            try {
                delay(2000)

                val list = getData()
                var currSize = list.size
                mDataList.addAll(list)

                mView.showOrderList(mDataList)
                mView.finishRefresh()
                if (mDataList.isEmpty()) {
                    mView.showDataEmpty(true)
                }
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

    private fun getData(): List<IOrderItem> {
        val list = mutableListOf<IOrderItem>()
        for (index in 1..10) {
            list.add(object : IOrderItem {
                override fun getOrderId(): String? {
                    return "123"
                }

                override fun getTime(): String? {
                    return "2019.11.11 12:00"
                }

                override fun getDesc(): String? {
                    return "朋友准备向我借钱，但是我不知道该怎么写借条，所以请各位律师帮忙代写一份借条。"
                }

                override fun getTypeStr(): String? = "发律师函"

                override fun getPrice(): String? {
                    return "报价：￥100"
                }

                override fun getStatusStr(): String? {
                    return "已接单"
                }
            })
        }
        return list
    }

}
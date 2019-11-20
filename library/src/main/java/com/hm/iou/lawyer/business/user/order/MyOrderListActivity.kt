package com.hm.iou.lawyer.business.user.order

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.hm.iou.base.mvp.HMBaseActivity
import com.hm.iou.lawyer.R
import com.hm.iou.lawyer.business.NavigationHelper
import com.hm.iou.uikit.HMLoadMoreView
import kotlinx.android.synthetic.main.lawyer_activity_user_order_list.*

/**
 * Created by hjy on 2019/11/12
 *
 * 我的订单列表
 */
class MyOrderListActivity : HMBaseActivity<MyOrderListPresenter>(), MyOrderListContract.View {

    private val mOrderAdapter = MyOrderAdapter()

    override fun getLayoutId(): Int = R.layout.lawyer_activity_user_order_list

    override fun initPresenter(): MyOrderListPresenter = MyOrderListPresenter(this, this)

    override fun initEventAndData(savedInstanceState: Bundle?) {
        rv_order_list.layoutManager = LinearLayoutManager(this)
        mOrderAdapter.setLoadMoreView(HMLoadMoreView())
        mOrderAdapter.bindToRecyclerView(rv_order_list)
        rv_order_list.adapter = mOrderAdapter
        smartrl_order_list.setOnRefreshListener {
            mPresenter.getFirstPage()
        }
        mOrderAdapter.setOnLoadMoreListener({
            mPresenter.getNextPage()
        }, rv_order_list)
        mOrderAdapter.setOnItemClickListener { adapter, _, position ->
            val item = adapter.getItem(position) as IOrderItem?
            item?.let {
                NavigationHelper.toUserOrderDetailPage(this, it.getOrderId() ?: "")
            }
        }
        mPresenter.getFirstPage()
    }

    override fun showInitLoading(show: Boolean) {
        if (show) {
            loading_view.showDataLoading()
        } else {
            loading_view.stopLoadingAnim()
            loading_view.visibility = View.GONE
        }
    }

    override fun showDataEmpty(show: Boolean) {
        if (show) {
            loading_view.showDataEmpty("没有相关订单哦~")
        } else {
            loading_view.stopLoadingAnim()
            loading_view.visibility = View.GONE
        }
    }

    override fun showInitError(err: String?) {
        loading_view.showDataFail(err) {
            mPresenter.getFirstPage()
        }
    }

    override fun clearOrderList() {
        mOrderAdapter.setNewData(null)
    }

    override fun showOrderList(list: List<IOrderItem>?) {
        mOrderAdapter.setNewData(list)
    }

    override fun finishRefresh() {
        smartrl_order_list.finishRefresh()
    }

    override fun showLoadMoreFail() {
        mOrderAdapter.loadMoreFail()
    }

    override fun showLoadMoreEnd() {
        mOrderAdapter.loadMoreEnd()
    }

    override fun showLoadMoreComplete() {
        mOrderAdapter.loadMoreComplete()
    }
}
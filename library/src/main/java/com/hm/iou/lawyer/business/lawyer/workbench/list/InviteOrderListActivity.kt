package com.hm.iou.lawyer.business.lawyer.workbench.list

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.hm.iou.base.mvp.HMBaseActivity
import com.hm.iou.lawyer.R
import com.hm.iou.uikit.HMLoadMoreView
import kotlinx.android.synthetic.main.lawyer_activity_lawyer_invite_order_list.*

/**
 * 我的钱包
 */
class InviteOrderListActivity : HMBaseActivity<InviteOrderListPresenter>(),
    InviteOrderListContract.View {

    private var mOrderAdapter: OrderAdapter? = null

    override fun getLayoutId(): Int = R.layout.lawyer_activity_lawyer_invite_order_list

    override fun initPresenter(): InviteOrderListPresenter =
        InviteOrderListPresenter(this, this)

    override fun initEventAndData(savedInstanceState: Bundle?) {
        mOrderAdapter = OrderAdapter(mContext)
        rv_order_list.layoutManager = LinearLayoutManager(this)
        mOrderAdapter?.setLoadMoreView(HMLoadMoreView())
        mOrderAdapter?.bindToRecyclerView(rv_order_list)
        rv_order_list.adapter = mOrderAdapter
        smartrl_order_list.setOnRefreshListener {
            mPresenter.getFirstPage()
        }
        mOrderAdapter?.setOnLoadMoreListener({
            mPresenter.getNextPage()
        }, rv_order_list)
        mOrderAdapter?.setOnItemClickListener { adapter, _, position ->
            val item = adapter.getItem(position) as IOrderItem?
            item?.let {
//                NavigationHelper.toUserOrderDetailPage(this, it.getOrderId() ?: "")
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
            loading_view.showDataEmpty("订单数据为空")
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
        mOrderAdapter?.setNewData(null)
    }

    override fun showOrderList(list: List<IOrderItem>?) {
        mOrderAdapter?.setNewData(list)
    }

    override fun finishRefresh() {
        smartrl_order_list.finishRefresh()
    }

    override fun showLoadMoreFail() {
        mOrderAdapter?.loadMoreFail()
    }

    override fun showLoadMoreEnd() {
        mOrderAdapter?.loadMoreEnd()
    }

    override fun showLoadMoreComplete() {
        mOrderAdapter?.loadMoreComplete()
    }


}
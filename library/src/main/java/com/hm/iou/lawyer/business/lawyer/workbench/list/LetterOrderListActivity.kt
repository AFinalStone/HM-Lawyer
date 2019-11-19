package com.hm.iou.lawyer.business.lawyer.workbench.list

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.hm.iou.base.mvp.HMBaseActivity
import com.hm.iou.lawyer.R
import com.hm.iou.lawyer.business.NavigationHelper
import com.hm.iou.lawyer.business.lawyer.workbench.order.OrderDetailActivity
import com.hm.iou.uikit.HMLoadMoreView
import kotlinx.android.synthetic.main.lawyer_activity_lawyer_letter_order_list.*

/**
 * 我的钱包
 */
class LetterOrderListActivity : HMBaseActivity<LetterOrderListPresenter>(),
    LetterOrderListContract.View {

    private var mOrderAdapter: OrderAdapter? = null

    override fun getLayoutId(): Int = R.layout.lawyer_activity_lawyer_letter_order_list

    override fun initPresenter(): LetterOrderListPresenter =
        LetterOrderListPresenter(this, this)

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
                val intent = Intent(mContext, OrderDetailActivity::class.java)
                val orderId = item.getOrderId()
                orderId?.let {
                    intent.putExtra(OrderDetailActivity.EXTRA_KEY_ORDER_ID, it)
                }
                val relationId = item.getRelationId()
                relationId?.let {
                    intent.putExtra(OrderDetailActivity.EXTRA_KEY_RELATION_ID, it.toString())
                }
                startActivity(intent)
            }
        }
        mPresenter.getFirstPage()
    }

    override fun onResume() {
        super.onResume()
        mPresenter.onResume()
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
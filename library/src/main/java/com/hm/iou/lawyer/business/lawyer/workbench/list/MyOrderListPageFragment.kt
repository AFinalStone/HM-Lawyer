package com.hm.iou.lawyer.business.lawyer.workbench.list

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.hm.iou.base.mvp.HMBaseFragment
import com.hm.iou.lawyer.R
import com.hm.iou.lawyer.business.lawyer.workbench.order.OrderDetailActivity
import com.hm.iou.lawyer.dict.LawyerOrderTabStatus
import com.hm.iou.logger.Logger
import com.hm.iou.tools.kt.getValue
import com.hm.iou.tools.kt.putValue
import com.hm.iou.uikit.HMLoadMoreView
import com.hm.iou.uikit.HMLoadingView
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import kotlinx.android.synthetic.main.lawyer_activity_lawyer_my_order_page_list.*

/**
 * @author : 借条管家-shilei
 * @version : 0.0.1
 * @Date : 2019-11-14 20:32
 * @E-Mail : afinalstone@foxmail.com
 */
class MyOrderListPageFragment : HMBaseFragment<MyOrderListPagePresenter>(),
    MyOrderListPageContract.View {

    companion object {
        private const val KEY_ORDER_STATUS = "order_status"

        fun newInstance(orderStatus: LawyerOrderTabStatus): MyOrderListPageFragment {
            val fragment = MyOrderListPageFragment()
            val args = Bundle()
            args.putValue(KEY_ORDER_STATUS, orderStatus)
            fragment.arguments = args
            return fragment
        }

    }

    private var mOrderStatus: LawyerOrderTabStatus = LawyerOrderTabStatus.ALL

    private var mOrderAdapter: OrderAdapter? = null


    override fun getLayoutId(): Int = R.layout.lawyer_activity_lawyer_my_order_page_list

    override fun initPresenter(): MyOrderListPagePresenter =
        MyOrderListPagePresenter(mActivity!!, this)

    override fun initEventAndData(savedInstanceState: Bundle?) {
        mOrderStatus = arguments?.get(KEY_ORDER_STATUS) as LawyerOrderTabStatus
        if (savedInstanceState != null) {
            mOrderStatus = savedInstanceState.getValue(KEY_ORDER_STATUS) ?: LawyerOrderTabStatus.ALL
        }
        Logger.d("tag==${mOrderStatus.statusName}")
        mOrderAdapter = mActivity?.let { OrderAdapter(it) }
        val rvOrderList = mContentView?.findViewById<RecyclerView>(R.id.rv_order_list)
        rvOrderList?.layoutManager = LinearLayoutManager(mActivity)
        mOrderAdapter?.setLoadMoreView(HMLoadMoreView())
        mOrderAdapter?.bindToRecyclerView(rvOrderList)
        rvOrderList?.adapter = mOrderAdapter
        mContentView?.findViewById<SmartRefreshLayout>(R.id.smartrl_order_list)
            ?.setOnRefreshListener {
                mPresenter.getFirstPage()
            }
        mOrderAdapter?.setOnLoadMoreListener({
            mPresenter.getNextPage()
        }, rvOrderList)
        mOrderAdapter?.setOnItemClickListener { adapter, _, position ->
            val item = adapter.getItem(position) as IOrderItem?
            item?.let {
                val intent = Intent(mActivity, OrderDetailActivity::class.java)
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
        mPresenter.init(mOrderStatus)
    }

    override fun onResume() {
        super.onResume()
        mPresenter.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putValue(KEY_ORDER_STATUS, mOrderStatus)
    }


    override fun showInitLoading(show: Boolean) {
        if (show) {
            mContentView?.findViewById<HMLoadingView>(R.id.loading_view)?.showDataLoading()
        } else {
            mContentView?.findViewById<HMLoadingView>(R.id.loading_view)?.visibility = View.GONE
        }
    }

    override fun showDataEmpty(show: Boolean) {
        if (show) {
            mContentView?.findViewById<HMLoadingView>(R.id.loading_view)?.showDataEmpty("没有相关订单哦~")
        } else {
            mContentView?.findViewById<HMLoadingView>(R.id.loading_view)?.stopLoadingAnim()
            mContentView?.findViewById<HMLoadingView>(R.id.loading_view)?.visibility = View.GONE
        }
    }

    override fun showInitError(err: String?) {
        mContentView?.findViewById<HMLoadingView>(R.id.loading_view)?.showDataFail(err) {
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
        mContentView?.findViewById<SmartRefreshLayout>(R.id.smartrl_order_list)?.finishRefresh()
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
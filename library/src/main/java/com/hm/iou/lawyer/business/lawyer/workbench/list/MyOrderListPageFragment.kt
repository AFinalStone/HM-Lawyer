package com.hm.iou.lawyer.business.lawyer.workbench.list

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.hm.iou.base.mvp.HMBaseFragment
import com.hm.iou.lawyer.R
import com.hm.iou.lawyer.dict.LawyerOrderStatus
import com.hm.iou.tools.kt.getValue
import com.hm.iou.tools.kt.putValue
import com.hm.iou.uikit.HMLoadMoreView
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

        fun newInstance(orderStatus: LawyerOrderStatus): MyOrderListPageFragment {
            val fragment = MyOrderListPageFragment()
            val args = Bundle()
            args.putValue(KEY_ORDER_STATUS, orderStatus)
            fragment.arguments = args
            return fragment
        }

    }

    private var mOrderStatus: LawyerOrderStatus = LawyerOrderStatus.ALL

    private var mOrderAdapter: OrderAdapter? = null


    override fun getLayoutId(): Int = R.layout.lawyer_activity_lawyer_my_order_page_list

    override fun initPresenter(): MyOrderListPagePresenter =
        MyOrderListPagePresenter(mActivity!!, this)

    override fun initEventAndData(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            mOrderStatus = savedInstanceState.getValue(KEY_ORDER_STATUS) ?: LawyerOrderStatus.ALL
        }
        mOrderAdapter = mActivity?.let { OrderAdapter(it) }
        rv_order_list.layoutManager = LinearLayoutManager(mActivity)
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
                //TODO
                //                NavigationHelper.toUserOrderDetailPage(this, it.getOrderId() ?: "")
            }
        }
        mPresenter.getFirstPage()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putValue(KEY_ORDER_STATUS, mOrderStatus)
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
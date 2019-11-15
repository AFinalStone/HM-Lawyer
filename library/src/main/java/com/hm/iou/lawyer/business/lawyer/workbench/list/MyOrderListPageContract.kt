package com.hm.iou.lawyer.business.lawyer.workbench.list

import com.hm.iou.base.mvp.BaseContract
import com.hm.iou.lawyer.dict.LawyerOrderStatus

/**
 * 我的钱包
 */
class MyOrderListPageContract {

    interface View : BaseContract.BaseView {
        fun showInitLoading(show: Boolean)

        fun showDataEmpty(show: Boolean)

        fun showInitError(err: String?)

        fun clearOrderList()

        fun showOrderList(list: List<IOrderItem>?)

        fun finishRefresh()

        fun showLoadMoreFail()

        fun showLoadMoreEnd()

        fun showLoadMoreComplete()

    }

    interface Presenter : BaseContract.BasePresenter {

        fun init(orderStatus: LawyerOrderStatus)

        fun onResume()

        fun getFirstPage()

        fun getNextPage()
    }
}
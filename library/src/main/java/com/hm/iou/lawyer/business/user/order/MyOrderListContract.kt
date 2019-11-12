package com.hm.iou.lawyer.business.user.order

import com.hm.iou.base.mvp.BaseContract

/**
 * Created by hjy on 2019/11/12
 *
 * 我的订单
 */
interface MyOrderListContract {

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

        fun getFirstPage()

        fun getNextPage()

    }

}
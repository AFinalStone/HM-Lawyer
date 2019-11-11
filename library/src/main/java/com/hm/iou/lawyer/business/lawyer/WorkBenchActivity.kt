package com.hm.iou.lawyer.business.lawyer

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import com.hm.iou.base.mvp.HMBaseActivity
import com.hm.iou.lawyer.R
import kotlinx.android.synthetic.main.lawyer_activity_lawyer_workbench.*

/**
 * @author : 借条管家-shilei
 * @version : 0.0.1
 * @Date : 2019-11-08 15:21
 * @E-Mail : afinalstone@foxmail.com
 */
class WorkBenchActivity : HMBaseActivity<WorkBenchPresenter>(), WorkBenchContract.View {

    private val mAdapterWaiteTo = MenuAdapter()
    private val mAdapterLawyerOrder = MenuAdapter()

    override fun getLayoutId(): Int = R.layout.lawyer_activity_lawyer_workbench

    override fun initPresenter(): WorkBenchPresenter = WorkBenchPresenter(this, this)

    override fun initEventAndData(savedInstanceState: Bundle?) {
        rv_list_wait_to_do.layoutManager = GridLayoutManager(mContext, 3)
        rv_list_wait_to_do.adapter = mAdapterWaiteTo

        rv_list_lawyer_order.layoutManager = GridLayoutManager(mContext, 3)
        rv_list_lawyer_order.adapter = mAdapterLawyerOrder
        mPresenter.init()
    }

    override fun showWaiteToDoList(list: List<IMenuItem>) {
        mAdapterWaiteTo.setNewData(list)
    }

    override fun showLawyerOrderList(list: List<IMenuItem>) {
        mAdapterLawyerOrder.setNewData(list)
    }
}
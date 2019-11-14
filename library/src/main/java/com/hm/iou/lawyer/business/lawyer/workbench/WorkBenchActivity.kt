package com.hm.iou.lawyer.business.lawyer.workbench

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import com.hm.iou.base.mvp.HMBaseActivity
import com.hm.iou.lawyer.R
import com.hm.iou.lawyer.business.NavigationHelper
import com.hm.iou.lawyer.business.lawyer.workbench.list.InviteOrderListActivity
import com.hm.iou.lawyer.business.lawyer.workbench.list.LetterOrderListActivity
import com.hm.iou.lawyer.business.lawyer.workbench.list.MyOrderListActivity
import com.hm.iou.lawyer.dict.LawyerOrderStatus
import com.hm.iou.lawyer.dict.ModelType
import kotlinx.android.synthetic.main.lawyer_activity_lawyer_workbench.*

/**
 * @author : 借条管家-shilei
 * @version : 0.0.1
 * @Date : 2019-11-08 15:21
 * @E-Mail : afinalstone@foxmail.com
 */
class WorkBenchActivity : HMBaseActivity<WorkBenchPresenter>(),
    WorkBenchContract.View {

    private val mAdapterWaiteTo = MenuAdapter()
    private val mAdapterLawyerOrder = MenuAdapter()

    override fun getLayoutId(): Int = R.layout.lawyer_activity_lawyer_workbench

    override fun initPresenter(): WorkBenchPresenter =
        WorkBenchPresenter(this, this)

    override fun initEventAndData(savedInstanceState: Bundle?) {

        ll_wallet.setOnClickListener {
            NavigationHelper.toMyWallet(mContext)
        }
        mAdapterWaiteTo.setOnItemClickListener { _, _, position ->
            val item = mAdapterWaiteTo.getItem(position)
            item?.let {
                val intent = Intent(mContext, MyOrderListActivity::class.java)
                when (item.getIModel()) {
                    ModelType.WAIT_TO_LOADING -> {
                        intent.putExtra(
                            MyOrderListActivity.EXTRA_KEY_TAB_TYPE,
                            LawyerOrderStatus.LOADING.status.toString()
                        )
                    }
                    ModelType.WAIT_TO_FINISH
                    -> {
                        intent.putExtra(
                            MyOrderListActivity.EXTRA_KEY_TAB_TYPE,
                            LawyerOrderStatus.FINISH.status.toString()
                        )
                    }
                    ModelType.WAIT_TO_ALL_ORDER
                    -> {
                        intent.putExtra(
                            MyOrderListActivity.EXTRA_KEY_TAB_TYPE,
                            LawyerOrderStatus.ALL.status.toString()
                        )
                    }
                    else ->
                        intent.putExtra(
                            MyOrderListActivity.EXTRA_KEY_TAB_TYPE,
                            LawyerOrderStatus.ALL.status.toString()
                        )
                }
                startActivity(intent)
            }
        }
        mAdapterLawyerOrder.setOnItemClickListener { _, _, position ->
            val item = mAdapterLawyerOrder.getItem(position)
            item?.let {
                when (item.getIModel()) {
                    ModelType.LAWYER_ORDER_CONSULT -> {
                    }
                    ModelType.LAWYER_LETTER
                    -> {
                        startActivity(Intent(mContext, LetterOrderListActivity::class.java))
                    }
                    ModelType.LAWYER_INVITE_RECEIVE
                    -> {
                        startActivity(Intent(mContext, InviteOrderListActivity::class.java))
                    }
                    else -> {
                    }
                }
            }
        }
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
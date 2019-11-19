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
import com.hm.iou.lawyer.dict.LawyerOrderTabStatus
import com.hm.iou.lawyer.dict.ModelType
import com.hm.iou.lawyer.dict.UpdateWalletBalanceEvent
import com.hm.iou.uikit.HMTopBarView
import kotlinx.android.synthetic.main.lawyer_activity_lawyer_workbench.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @author : 借条管家-shilei
 * @version : 0.0.1
 * @Date : 2019-11-08 15:21
 * @E-Mail : afinalstone@foxmail.com
 */
class WorkBenchActivity : HMBaseActivity<WorkBenchPresenter>(),
    WorkBenchContract.View {

    private var mNeedRefresh = false

    private val mAdapterWaiteTo = MenuAdapter()
    private val mAdapterLawyerOrder = MenuAdapter()

    override fun getLayoutId(): Int = R.layout.lawyer_activity_lawyer_workbench

    override fun initPresenter(): WorkBenchPresenter =
        WorkBenchPresenter(this, this)

    override fun initEventAndData(savedInstanceState: Bundle?) {
        EventBus.getDefault().register(this)

        topbar.setOnMenuClickListener(object : HMTopBarView.OnTopBarMenuClickListener {
            override fun onClickTextMenu() {

            }

            override fun onClickImageMenu() {
                NavigationHelper.toLawyerHomePage(this@WorkBenchActivity)
            }
        })

        srl_workbench.setOnRefreshListener {
            mPresenter.refreshWorkbenchInfo()
        }

        ll_wallet_remaind_money.setOnClickListener {
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
                            LawyerOrderTabStatus.LOADING.status.toString()
                        )
                    }
                    ModelType.WAIT_TO_FINISH -> {
                        intent.putExtra(
                            MyOrderListActivity.EXTRA_KEY_TAB_TYPE,
                            LawyerOrderTabStatus.FINISH.status.toString()
                        )
                    }
                    ModelType.WAIT_TO_ALL_ORDER -> {
                        intent.putExtra(
                            MyOrderListActivity.EXTRA_KEY_TAB_TYPE,
                            LawyerOrderTabStatus.ALL.status.toString()
                        )
                    }
                    else ->
                        intent.putExtra(
                            MyOrderListActivity.EXTRA_KEY_TAB_TYPE,
                            LawyerOrderTabStatus.ALL.status.toString()
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
        mPresenter.refreshWorkbenchInfo()
    }

    override fun onResume() {
        super.onResume()
        if (mNeedRefresh) {
            mNeedRefresh = false
            mPresenter.refreshWorkbenchInfo()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun showWaitToDoList(list: List<IMenuItem>) {
        mAdapterWaiteTo.setNewData(list)
    }

    override fun showLawyerOrderList(list: List<IMenuItem>) {
        mAdapterLawyerOrder.setNewData(list)
    }

    override fun showWalletBalance(amount: String) {
        iv_workbench_total_money.text = amount
    }

    override fun showTodayIncome(income: String) {
        tv_today_income.text = income
    }

    override fun showTodayCompleteCount(count: String) {
        tv_today_finish.text = count
    }

    override fun showTodayOrderCount(count: String) {
        tv_receive_order.text = count
    }

    override fun finishRefresh() {
        srl_workbench.setRefreshing(false)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventUpdateWalletBalance(event: UpdateWalletBalanceEvent) {
        mNeedRefresh = true
    }
}
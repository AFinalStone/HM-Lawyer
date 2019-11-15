package com.hm.iou.lawyer.business.lawyer.workbench.wallet

import android.os.Bundle
import com.hm.iou.base.BaseBizAppLike
import com.hm.iou.base.mvp.HMBaseActivity
import com.hm.iou.base.utils.RouterUtil
import com.hm.iou.lawyer.R
import com.hm.iou.lawyer.business.NavigationHelper
import com.hm.iou.lawyer.dict.UpdateWalletBalanceEvent
import kotlinx.android.synthetic.main.lawyer_activity_lawyer_wallet.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 我的钱包
 */
class WalletActivity : HMBaseActivity<WalletPresenter>(),
    WalletContract.View {

    private var mNeedRefresh = false

    override fun getLayoutId(): Int = R.layout.lawyer_activity_lawyer_wallet

    override fun initPresenter(): WalletPresenter =
        WalletPresenter(this, this)

    override fun initEventAndData(savedInstanceState: Bundle?) {
        EventBus.getDefault().register(this)

        btn_withdraw_money.setOnClickListener {
            mPresenter.toWithdrawMoney()
        }

        bottom_bar.setOnTitleClickListener {
            //交易记录
            NavigationHelper.toTradeRecordListPage(this)
        }

        srl_wallet.setOnRefreshListener {
            mPresenter.refreshWalletInfo()
        }

        mPresenter.refreshWalletInfo()
    }

    override fun onResume() {
        super.onResume()
        if (mNeedRefresh) {
            mNeedRefresh = false
            mPresenter.refreshWalletInfo()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun finishRefresh() {
        srl_wallet.setRefreshing(false)
    }

    override fun showWalletBalance(money: String?) {
        tv_remainder_money.text = money ?: ""
    }

    override fun showTotalProfit(money: String?) {
        tv_total_receive_money.text = money ?: ""
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventUpdateWalletBalance(event: UpdateWalletBalanceEvent) {
        mNeedRefresh = true
    }

}
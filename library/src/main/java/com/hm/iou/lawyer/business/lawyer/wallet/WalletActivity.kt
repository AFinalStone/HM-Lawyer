package com.hm.iou.lawyer.business.lawyer.wallet

import android.content.Intent
import android.os.Bundle
import com.hm.iou.base.mvp.HMBaseActivity
import com.hm.iou.lawyer.R
import com.hm.iou.lawyer.business.lawyer.withdraw.WithDrawActivity
import kotlinx.android.synthetic.main.lawyer_activity_lawyer_wallet.*

/**
 * 我的钱包
 */
class WalletActivity : HMBaseActivity<WalletPresenter>(),
    WalletContract.View {

    override fun getLayoutId(): Int = R.layout.lawyer_activity_lawyer_wallet

    override fun initPresenter(): WalletPresenter =
        WalletPresenter(this, this)

    override fun initEventAndData(savedInstanceState: Bundle?) {
        btn_withdraw_money.setOnClickListener {
            val intent = Intent(mContext, WithDrawActivity::class.java)
            mContext.startActivity(intent)
        }
    }


    override fun showRemainderMoney(money: String?) {
        tv_remainder_money.text = money ?: ""
    }

    override fun showTotalMoney(money: String?) {
        tv_total_receive_money.text = money ?: ""
    }
}
package com.hm.iou.lawyer.business.lawyer.workbench.order

import android.content.Intent
import android.os.Bundle
import com.hm.iou.base.mvp.HMBaseActivity
import com.hm.iou.lawyer.R
import com.hm.iou.lawyer.business.lawyer.workbench.withdraw.WithDrawActivity
import kotlinx.android.synthetic.main.lawyer_activity_lawyer_wallet.*

/**
 * 我的钱包
 */
class OrderDetailActivity : HMBaseActivity<OrderDetailPresenter>(),
    OrderDetailContract.View {

    override fun getLayoutId(): Int = R.layout.lawyer_activity_lawyer_order_detail

    override fun initPresenter(): OrderDetailPresenter =
        OrderDetailPresenter(this, this)

    override fun initEventAndData(savedInstanceState: Bundle?) {
        btn_withdraw_money.setOnClickListener {
            val intent = Intent(mContext, WithDrawActivity::class.java)
            mContext.startActivity(intent)
        }
    }

}
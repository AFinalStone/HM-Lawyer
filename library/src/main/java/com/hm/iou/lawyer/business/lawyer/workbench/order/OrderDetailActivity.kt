package com.hm.iou.lawyer.business.lawyer.workbench.order

import android.content.Intent
import android.os.Bundle
import com.hm.iou.base.mvp.HMBaseActivity
import com.hm.iou.lawyer.R
import com.hm.iou.lawyer.business.lawyer.workbench.withdraw.WithDrawActivity
import com.hm.iou.tools.kt.extraDelegate
import com.hm.iou.tools.kt.getValue
import com.hm.iou.tools.kt.putValue
import kotlinx.android.synthetic.main.lawyer_activity_lawyer_wallet.*

/**
 * 我的钱包
 */
class OrderDetailActivity : HMBaseActivity<OrderDetailPresenter>(),
    OrderDetailContract.View {


    companion object {
        const val EXTRA_KEY_ORDER_ID = "order_id"
        const val EXTRA_KEY_RELATION_ID = "relation_id"
    }

    /**
     * 订单编号
     */
    private var mOrderId: Boolean? by extraDelegate(
        EXTRA_KEY_ORDER_ID,
        null
    )
    /**
     * 记录id
     */
    private var mRelationId: Boolean? by extraDelegate(
        EXTRA_KEY_RELATION_ID,
        null
    )

    override fun getLayoutId(): Int = R.layout.lawyer_activity_lawyer_order_detail

    override fun initPresenter(): OrderDetailPresenter =
        OrderDetailPresenter(this, this)

    override fun initEventAndData(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            mOrderId = savedInstanceState.getValue(EXTRA_KEY_ORDER_ID)
            mRelationId = savedInstanceState.getValue(EXTRA_KEY_RELATION_ID)
        }

    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putValue(EXTRA_KEY_ORDER_ID, mOrderId)
        outState?.putValue(EXTRA_KEY_RELATION_ID, mRelationId)
    }

}
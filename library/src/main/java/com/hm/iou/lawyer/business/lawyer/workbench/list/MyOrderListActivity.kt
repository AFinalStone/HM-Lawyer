package com.hm.iou.lawyer.business.lawyer.workbench.list

import android.os.Bundle
import com.hm.iou.base.mvp.BaseContract
import com.hm.iou.base.mvp.HMBaseActivity
import com.hm.iou.base.mvp.HMBasePresenter
import com.hm.iou.lawyer.R
import com.hm.iou.lawyer.dict.LawyerOrderTabStatus
import com.hm.iou.tools.kt.extraDelegate
import com.hm.iou.tools.kt.getValue
import com.hm.iou.tools.kt.putValue
import kotlinx.android.synthetic.main.lawyer_activity_lawyer_my_order_list.*

/**
 * 我的订单
 */
class MyOrderListActivity : HMBaseActivity<HMBasePresenter<BaseContract.BaseView>>(),
    BaseContract.BaseView {

    companion object {
        const val EXTRA_KEY_TAB_TYPE = "tab_type"
    }

    private var mOrderStatus: String? by extraDelegate(
        EXTRA_KEY_TAB_TYPE,
        LawyerOrderTabStatus.ALL.status.toString()
    )

    private val mPagerAdapter: MyOrderPagerAdapter = MyOrderPagerAdapter(supportFragmentManager)

    override fun getLayoutId(): Int = R.layout.lawyer_activity_lawyer_my_order_list

    override fun initPresenter(): HMBasePresenter<BaseContract.BaseView> =
        HMBasePresenter(this, this)

    override fun initEventAndData(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            mOrderStatus = savedInstanceState.getValue(EXTRA_KEY_TAB_TYPE)
        }

        vp_my_order.adapter = mPagerAdapter
        tab_my_order.setViewPager(vp_my_order)
        if (mOrderStatus != null) {
            try {
                val orderStatus = Integer.valueOf(mOrderStatus)
                val pos = mPagerAdapter.getPositionByType(orderStatus)
                vp_my_order.currentItem = pos
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putValue(EXTRA_KEY_TAB_TYPE, mOrderStatus)
    }

}
package com.hm.iou.lawyer.business.lawyer.home

import android.os.Bundle
import com.hm.iou.base.mvp.HMBaseActivity
import com.hm.iou.lawyer.R
import com.hm.iou.uikit.HMTopBarView
import kotlinx.android.synthetic.main.lawyer_activity_lawyer_home.*

/**
 * 律师首页前置页面
 */
class HomeActivity : HMBaseActivity<HomePresenter>(),
    HomeContract.View {

    override fun getLayoutId(): Int = R.layout.lawyer_activity_lawyer_home

    override fun initPresenter(): HomePresenter =
        HomePresenter(this, this)

    override fun initEventAndData(savedInstanceState: Bundle?) {
        initView()
        mPresenter.init()
    }

    private fun initView() {
        topbar.setOnMenuClickListener(object : HMTopBarView.OnTopBarMenuClickListener {
            override fun onClickTextMenu() {
            }

            override fun onClickImageMenu() {
            }

        })
    }
}
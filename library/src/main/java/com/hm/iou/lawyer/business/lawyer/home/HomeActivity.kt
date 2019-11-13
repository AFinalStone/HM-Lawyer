package com.hm.iou.lawyer.business.lawyer.home

import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import com.hm.iou.base.mvp.HMBaseActivity
import com.hm.iou.lawyer.R
import com.hm.iou.lawyer.bean.res.GetLawyerHomeDetailResBean
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

    override fun showDetail(detail: GetLawyerHomeDetailResBean) {
        tv_lawyer_name.text = detail.authName ?: ""
        tv_lawyer_age_limit.text = "执业%S年".format(detail.holdingYearCount)
        tv_lawyer_company.text = detail.lawFirm ?: ""
        tv_lawyer_location.text = detail.location ?: ""
        tv_lawyer_desc.text = detail.info ?: ""
    }

    override fun showInitView() {
        loading_view.visibility = VISIBLE
        loading_view.showDataLoading()
    }

    override fun hideInitView() {
        loading_view.visibility = INVISIBLE
        loading_view.stopLoadingAnim()
    }

    override fun showInitFailed(msg: String) {
        loading_view.visibility = VISIBLE
        loading_view.showDataFail(msg) {
            mPresenter.init()
        }
    }
}
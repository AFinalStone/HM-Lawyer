package com.hm.iou.lawyer.business.lawyer.home.prepare

import android.os.Bundle
import com.hm.iou.base.mvp.HMBaseActivity
import com.hm.iou.lawyer.business.NavigationHelper

/**
 * 律师首页前置页面
 */
class HomePrepareActivity : HMBaseActivity<HomePreparePresenter>(),
    HomePrepareContract.View {


    override fun getLayoutId(): Int = 0

    override fun initPresenter(): HomePreparePresenter =
        HomePreparePresenter(this, this)

    override fun initEventAndData(savedInstanceState: Bundle?) {
        mPresenter.init()
    }

    override fun toLawyerHomePage(updateLawFirmState: Int?, updateYearCheckState: Int?) {

    }

    override fun toAuthenticationPage() {
        NavigationHelper.toAuthentication(mContext)
    }

    override fun toAuthenProgressPage(isAuthenFailed: Boolean) {

    }

}
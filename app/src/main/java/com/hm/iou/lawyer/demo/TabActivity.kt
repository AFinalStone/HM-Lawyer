package com.hm.iou.lawyer.demo

import android.os.Bundle
import com.hm.iou.base.mvp.BaseContract
import com.hm.iou.base.mvp.HMBaseActivity
import com.hm.iou.base.mvp.HMBasePresenter
import com.hm.iou.lawyer.business.index.LawerTabIndexFragment

class TabActivity : HMBaseActivity<HMBasePresenter<BaseContract.BaseView>>() {

    override fun getLayoutId(): Int = com.hm.iou.lawyer.demo.R.layout.activity_tab_demo

    override fun initPresenter(): HMBasePresenter<BaseContract.BaseView> {
        return HMBasePresenter(this, this)
    }

    override fun initEventAndData(savedInstanceState: Bundle?) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, LawerTabIndexFragment())
            .commit()
    }

}
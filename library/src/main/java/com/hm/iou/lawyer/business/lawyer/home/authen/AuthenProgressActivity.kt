package com.hm.iou.lawyer.business.lawyer.home.authen

import android.os.Bundle
import com.hm.iou.base.mvp.BaseContract
import com.hm.iou.base.mvp.HMBaseActivity
import com.hm.iou.base.mvp.HMBasePresenter
import com.hm.iou.lawyer.R

/**
 * @author : 借条管家-shilei
 * @version : 0.0.1
 * @Date : 2019-11-12 13:50
 * @E-Mail : afinalstone@foxmail.com
 */
class AuthenProgressActivity : HMBaseActivity<HMBasePresenter<BaseContract.BaseView>>() {

    override fun getLayoutId(): Int = R.layout.lawyer_activity_lawyer_authentication_progress

    override fun initEventAndData(savedInstanceState: Bundle?) {
    }

    override fun initPresenter(): HMBasePresenter<BaseContract.BaseView> =
        HMBasePresenter(this, this)


}
package com.hm.iou.lawyer.business.user.create

import android.os.Bundle
import com.hm.iou.base.mvp.BaseContract
import com.hm.iou.base.mvp.HMBaseActivity
import com.hm.iou.base.mvp.HMBasePresenter
import com.hm.iou.lawyer.R
import com.hm.iou.lawyer.business.NavigationHelper
import kotlinx.android.synthetic.main.lawyer_activity_lawyer_letter_desc.*

/**
 * Created by hjy on 2019/11/12
 *
 * 律师函介绍页面
 */
class LawyerLetterDescActivity : HMBaseActivity<HMBasePresenter<BaseContract.BaseView>>() {

    override fun getLayoutId(): Int = R.layout.lawyer_activity_lawyer_letter_desc

    override fun initPresenter(): HMBasePresenter<BaseContract.BaseView> =
        HMBasePresenter(this, this)

    override fun initEventAndData(savedInstanceState: Bundle?) {
        bottom_bar.setOnTitleClickListener {
            NavigationHelper.toCreateLawyerLetter(this, null, null)
            finish()
        }
    }

}
package com.hm.iou.lawyer.business.user.find

import android.os.Bundle
import com.hm.iou.base.mvp.HMBaseActivity
import com.hm.iou.lawyer.R

/**
 * Created by hjy on 2019/11/11
 *
 * 查找律师
 *
 */
class FindLawyerActivity : HMBaseActivity<FindLawyerPresenter>(), FindLawyerContract.View {

    override fun getLayoutId(): Int = R.layout.lawyer_activity_find_lawyer

    override fun initPresenter(): FindLawyerPresenter = FindLawyerPresenter(this, this)

    override fun initEventAndData(savedInstanceState: Bundle?) {

    }

}
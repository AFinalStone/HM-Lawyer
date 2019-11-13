package com.hm.iou.lawyer.business.lawyer.home.edit

import android.os.Bundle
import com.hm.iou.base.mvp.HMBaseActivity
import com.hm.iou.lawyer.R

/**
 * 律师介绍
 */
class EditLawyerSelfIntroduceActivity : HMBaseActivity<EditLawyerSelfIntroducePresenter>(),
    EditLawyerSelfIntroduceContract.View {


    override fun getLayoutId(): Int = R.layout.lawyer_activity_edit_lawyer_self_introduction

    override fun initPresenter(): EditLawyerSelfIntroducePresenter =
        EditLawyerSelfIntroducePresenter(this, this)

    override fun initEventAndData(savedInstanceState: Bundle?) {
    }

}
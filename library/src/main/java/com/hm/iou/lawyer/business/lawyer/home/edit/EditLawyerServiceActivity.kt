package com.hm.iou.lawyer.business.lawyer.home.edit

import android.os.Bundle
import com.hm.iou.base.mvp.HMBaseActivity
import com.hm.iou.lawyer.R

/**
 * 服务费用
 */
class EditLawyerServiceActivity : HMBaseActivity<EditLawyerServicePresenter>(),
    EditLawyerServiceContract.View {

    override fun getLayoutId(): Int = R.layout.lawyer_activity_edit_lawyer_service_money

    override fun initPresenter(): EditLawyerServicePresenter =
        EditLawyerServicePresenter(this, this)

    override fun initEventAndData(savedInstanceState: Bundle?) {
    }


}
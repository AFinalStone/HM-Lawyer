package com.hm.iou.lawyer.business.lawyer.home.edit

import com.hm.iou.base.mvp.BaseContract

/**
 * 律师首页前置页面
 */
class EditLawyerSelfIntroduceContract {

    interface View : BaseContract.BaseView {
    }

    interface Presenter : BaseContract.BasePresenter {
        /**
         * 更新律师认证信息
         */
        fun updateLawyerAuthenticationInfo(
            selfIntroduction: String
        )
    }
}
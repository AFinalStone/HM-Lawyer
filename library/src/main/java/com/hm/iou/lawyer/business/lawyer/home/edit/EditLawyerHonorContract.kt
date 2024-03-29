package com.hm.iou.lawyer.business.lawyer.home.edit

import com.hm.iou.base.mvp.BaseContract
import com.hm.iou.database.table.IouData

/**
 * 律师首页前置页面
 */
class EditLawyerHonorContract {

    interface View : BaseContract.BaseView {
    }

    interface Presenter : BaseContract.BasePresenter {
        /**
         * 律师认证
         */
        fun updateLawyerAuthenticationInfo(
            listCertificateImagePath: MutableList<IouData.FileEntity>
        )
    }
}
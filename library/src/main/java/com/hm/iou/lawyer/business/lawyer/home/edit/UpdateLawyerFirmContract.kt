package com.hm.iou.lawyer.business.lawyer.home.edit

import com.hm.iou.base.mvp.BaseContract
import com.hm.iou.database.table.IouData

/**
 * 更换执业机构
 */
class UpdateLawyerFirmContract {

    interface View : BaseContract.BaseView {
    }

    interface Presenter : BaseContract.BasePresenter {
        /**
         * 更换执业律所
         */
        fun updateLawyerAuthenticationInfo(
            licenseNumber: String,
            lawFirm: String,
            listAuthenImagePath: List<String>
        )
    }
}
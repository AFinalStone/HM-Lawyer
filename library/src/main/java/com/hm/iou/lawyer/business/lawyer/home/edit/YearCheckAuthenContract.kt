package com.hm.iou.lawyer.business.lawyer.home.edit

import com.hm.iou.base.mvp.BaseContract
import com.hm.iou.database.table.IouData

/**
 * 年检认证
 */
class YearCheckAuthenContract {

    interface View : BaseContract.BaseView {
    }

    interface Presenter : BaseContract.BasePresenter {
        /**
         * 年检认证
         */
        fun updateLawyerAuthenticationInfo(
            listAuthenImagePath: List<String>
        )
    }
}
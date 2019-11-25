package com.hm.iou.lawyer.business.lawyer.workbench.order

import com.hm.iou.base.mvp.BaseContract

/**
 * 咨询解答
 */
class InputLawyerConsultAnswerContract {

    interface View : BaseContract.BaseView {
    }

    interface Presenter : BaseContract.BasePresenter {
        /**
         * 完成解答
         */
        fun finishAnswer(
            billId: String,
            desc: String
        )
    }
}
package com.hm.iou.lawyer.business.user.create

import com.hm.iou.base.mvp.BaseContract
import com.hm.iou.database.table.IouData
import com.hm.iou.lawyer.bean.LetterReceiverBean

/**
 * Created by hjy on 2019/11/22
 *
 * 创建律师咨询
 */
interface CreateLawyerConsultContract {

    interface View : BaseContract.BaseView {

    }

    interface Presenter : BaseContract.BasePresenter {

        /**
         * 开始创建订单
         */
        fun createLawyerConsult(
            lawyerId: String?, price: Int, caseDesc: String?,
            list: List<IouData.FileEntity>?, innerUser: Boolean
        )

        /**
         * 创建订单成功
         */
        fun createOrderSuccess()

    }

}
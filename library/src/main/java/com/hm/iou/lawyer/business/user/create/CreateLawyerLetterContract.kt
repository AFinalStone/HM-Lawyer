package com.hm.iou.lawyer.business.user.create

import com.hm.iou.base.mvp.BaseContract
import com.hm.iou.database.table.IouData
import com.hm.iou.lawyer.bean.LetterReceiverBean

/**
 * Created by hjy on 2019/11/12
 *
 */
interface CreateLawyerLetterContract {

    interface View : BaseContract.BaseView {

    }

    interface Presenter : BaseContract.BasePresenter {

        /**
         * 创建律师函来源，1-借条，2-app在线律师服务，默认情况下当"app在线律师服务处理"
         */
        fun initSource(sourceStr: String?)

        /**
         * 开始创建订单
         */
        fun createLawyerLetter(
            name: String?, mobile: String?, lawyerId: String?, price: Int,
            receiverInfo: LetterReceiverBean?, caseDesc: String?,
            list: List<IouData.FileEntity>?, innerUser: Boolean
        )

        /**
         * 创建订单成功
         */
        fun createOrderSuccess()

    }

}
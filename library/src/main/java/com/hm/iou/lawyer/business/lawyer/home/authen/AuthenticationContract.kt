package com.hm.iou.lawyer.business.lawyer.home.authen

import com.hm.iou.base.mvp.BaseContract
import com.hm.iou.database.table.IouData
import com.hm.iou.lawyer.bean.req.LawyerAuthenticationReqBean

/**
 * 律师认证
 */
class AuthenticationContract {

    interface View : BaseContract.BaseView {

        /**
         * 律师首页
         */
        fun toLawyerHomePage()

        /**
         * 去认证
         */
        fun toAuthenticationPage()

        /**
         * 进入认证进度页面
         */
        fun toAuthenProgressPage()
    }

    interface Presenter : BaseContract.BasePresenter {
        /**
         * 初始化
         */
        fun init()

        /**
         * 律师认证
         */
        fun lawyerAuthentication(
            certificateCode: String,
            lawyerFirmName: String,
            certificateStartTime: String,
            selfIntroduction: String,
            headerImagePath: String,
            listAuthenImage: List<String>,
            listCertificateImage: MutableList<IouData.FileEntity>?
        )
    }
}
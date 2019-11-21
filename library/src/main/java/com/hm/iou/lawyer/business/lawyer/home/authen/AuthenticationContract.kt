package com.hm.iou.lawyer.business.lawyer.home.authen

import com.hm.iou.base.mvp.BaseContract
import com.hm.iou.database.table.IouData
import com.hm.iou.lawyer.bean.req.LawyerAuthenticationReqBean
import com.hm.iou.lawyer.bean.res.ImageUrlFileIdBean
import com.hm.iou.lawyer.bean.res.LawyerAuthenticationResBean

/**
 * 律师认证
 */
class AuthenticationContract {

    interface View : BaseContract.BaseView {

        /**
         * 显示详情
         */
        fun showDetail(detail: LawyerAuthenticationResBean)

        /**
         * 设置返回并关闭页面
         */
        fun setResultOkAndClosePage()
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
            headerImageBean: ImageUrlFileIdBean?,
            authenImageFrontBean: ImageUrlFileIdBean?,
            authenImageBackBean: ImageUrlFileIdBean?,
            listCertificateImagePath: MutableList<IouData.FileEntity>?
        )
    }
}
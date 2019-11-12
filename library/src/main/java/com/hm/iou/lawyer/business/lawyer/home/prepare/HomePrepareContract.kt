package com.hm.iou.lawyer.business.lawyer.home.prepare

import com.hm.iou.base.mvp.BaseContract

/**
 * 律师首页前置页面
 */
class HomePrepareContract {

    interface View : BaseContract.BaseView {

        /**
         * 律师首页
         * @param updateLawFirmState 更新执业机构状态
         * @param updateYearCheckState 更新年检状态
         */
        fun toLawyerHomePage(updateLawFirmState: Int?, updateYearCheckState: Int?)

        /**
         * 去认证
         */
        fun toAuthenticationPage()

        /**
         * 进入认证进度页面
         */
        fun toAuthenProgressPage(isAuthenFailed: Boolean)
    }

    interface Presenter : BaseContract.BasePresenter {
        /**
         * 初始化
         */
        fun init()
    }
}
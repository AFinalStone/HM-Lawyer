package com.hm.iou.lawyer.business.lawyer.home

import com.hm.iou.base.mvp.BaseContract

/**
 * 律师首页前置页面
 */
class HomeContract {

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
    }
}
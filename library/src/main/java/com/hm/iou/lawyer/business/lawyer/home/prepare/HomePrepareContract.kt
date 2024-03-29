package com.hm.iou.lawyer.business.lawyer.home.prepare

import com.hm.iou.base.mvp.BaseContract

/**
 * 律师首页前置页面
 */
class HomePrepareContract {

    interface View : BaseContract.BaseView {
    }

    interface Presenter : BaseContract.BasePresenter {
        /**
         * 初始化
         */
        fun init()
    }
}
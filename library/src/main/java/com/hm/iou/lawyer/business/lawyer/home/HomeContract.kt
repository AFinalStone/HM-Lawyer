package com.hm.iou.lawyer.business.lawyer.home

import com.hm.iou.base.mvp.BaseContract
import com.hm.iou.lawyer.bean.res.GetLawyerHomeDetailResBean

/**
 * 律师首页前置页面
 */
class HomeContract {

    interface View : BaseContract.BaseView {

        fun showInitView()

        fun hideInitView()

        fun showInitFailed(msg: String)

        fun showDetail(detail: GetLawyerHomeDetailResBean)
    }

    interface Presenter : BaseContract.BasePresenter {
        /**
         * 初始化
         */
        fun init()


        fun onResume()
    }
}
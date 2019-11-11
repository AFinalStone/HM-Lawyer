package com.hm.iou.lawyer.business.index

import com.hm.iou.base.adver.AdBean
import com.hm.iou.base.mvp.BaseContract

/**
 * Created by hjy on 2019/11/11
 *
 * 律师 tab 首页
 */
interface LawerTabIndexContract {

    interface View : BaseContract.BaseView {

        /**
         * 设置导航栏头部红色标记的数量
         *
         * @param redFlagCount
         */
        fun updateRedFlagCount(redFlagCount: String)

        fun showBanner(list : List<AdBean>?)

        fun showTopLawyerWorkbench(visibility: Int)
    }

    interface Presenter : BaseContract.BasePresenter {

        fun init()

        fun updateRedFlagCount()

        /**
         * 获取banner
         */
        fun getTopBanner()
    }
}
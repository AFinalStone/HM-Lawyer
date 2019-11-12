package com.hm.iou.lawyer.business.user.find

import com.hm.iou.base.mvp.BaseContract

/**
 * Created by hjy on 2019/11/11
 *
 * 找律师
 */
interface FindLawyerContract {

    interface View : BaseContract.BaseView {

        /**
         * 是否通过年限来查找
         */
        fun showInitLoading(findByName: Boolean)

        fun hideInitLoading(findByName: Boolean)

        fun showLoadingError(msg: String, findByName: Boolean)

        fun showDataEmpty(findByName: Boolean)

        fun clearList(findByName: Boolean)

        fun showLawyers(list: List<ILawyerItem>, findByName: Boolean)

        fun showLoadMoreEnd(findByName: Boolean)

        fun showLoadMoreComplete(findByName: Boolean)

        fun showRecyclerView(visibility: Int, findByName: Boolean)
    }

    interface Presenter : BaseContract.BasePresenter {

        /**
         * 初始化"通过年限"查找条件
         */
        fun findByAgeLimit(ageLimitType: Int)

        fun getNextPageByAgeLimit()

        /**
         * 初始化"通过姓名"查找条件
         */
        fun findByLawyerName(name: String)

        fun getNextPageByLawyerName()
    }
}
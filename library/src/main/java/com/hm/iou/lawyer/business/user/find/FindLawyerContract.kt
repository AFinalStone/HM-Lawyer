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
        fun showInitLoading(isFindByAgeLimit: Boolean)

        fun hideInitLoading(isFindByAgeLimit: Boolean)

        fun showLoadingError(msg: String, isFindByAgeLimit: Boolean)

        fun showDataEmpty(isFindByAgeLimit: Boolean)

        fun clearList(isFindByAgeLimit: Boolean)

        fun showLawyers(list: List<ILawyerItem>, isFindByAgeLimit: Boolean)

        fun showLoadMoreEnd(isFindByAgeLimit: Boolean)

        fun showLoadMoreComplete(isFindByAgeLimit: Boolean)

        fun showRecyclerView(visibility: Int, isFindByAgeLimit: Boolean)
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
package com.hm.iou.lawyer.business.lawyer.workbench

import com.hm.iou.base.mvp.BaseContract

/**
 * @author : 借条管家-shilei
 * @version : 0.0.1
 * @Date : 2019-11-08 15:14
 * @E-Mail : afinalstone@foxmail.com
 */
class WorkBenchContract {

    interface View : BaseContract.BaseView {

        fun finishRefresh()

        /**
         * 显示待办事项
         */
        fun showWaitToDoList(list: List<IMenuItem>)

        /**
         * 显示律师订单
         */
        fun showLawyerOrderList(list: List<IMenuItem>)

        /**
         * 显示钱包余额
         */
        fun showWalletBalance(amount: String)

        /**
         * 显示今日收益
         */
        fun showTodayIncome(income: String)

        /**
         * 今日完成
         */
        fun showTodayCompleteCount(count: String)

        /**
         * 显示今日接单
         */
        fun showTodayOrderCount(count: String)

    }

    interface Presenter : BaseContract.BasePresenter {

        fun init()

        /**
         * 初始化
         */
        fun refreshWorkbenchInfo()
    }
}
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
        /**
         * 显示待办事项
         */
        fun showWaiteToDoList(list: List<IMenuItem>)

        /**
         * 显示律师订单
         */
        fun showLawyerOrderList(list: List<IMenuItem>)
    }

    interface Presenter : BaseContract.BasePresenter {
        /**
         * 初始化
         */
        fun init()
    }
}
package com.hm.iou.lawyer.business.lawyer

import android.content.Context
import com.hm.iou.base.mvp.HMBasePresenter

/**
 * @author : 借条管家-shilei
 * @version : 0.0.1
 * @Date : 2019-11-08 15:19
 * @E-Mail : afinalstone@foxmail.com
 */
class WorkBenchPresenter(context: Context, view: WorkBenchContract.View) :
    HMBasePresenter<WorkBenchContract.View>(context, view),
    WorkBenchContract.Presenter {

    private val mListDataWaiteToDo = ArrayList<IMenuItem>()
    private val mListDataLawyerOrder = ArrayList<IMenuItem>()

    override fun init() {

        mListDataWaiteToDo.add(object : IMenuItem {
            override fun getIIcon(): Int = -1

            override fun getIName(): String = "进行中"

            override fun getIRedDotNum(): String = "1"

            override fun ifShowRedDot(): Boolean = true
        })
        mListDataWaiteToDo.add(object : IMenuItem {
            override fun getIIcon(): Int = -1

            override fun getIName(): String = "已完成"

            override fun getIRedDotNum(): String = "1"

            override fun ifShowRedDot(): Boolean = false
        })
        mListDataWaiteToDo.add(object : IMenuItem {
            override fun getIIcon(): Int = -1

            override fun getIName(): String = "全部订单"

            override fun getIRedDotNum(): String = "1"

            override fun ifShowRedDot(): Boolean = false
        })
        mView.showWaiteToDoList(mListDataWaiteToDo)

        //显示订单
        mListDataLawyerOrder.add(object : IMenuItem {
            override fun getIIcon(): Int = -1

            override fun getIName(): String = "律师咨询"

            override fun getIRedDotNum(): String = "1"

            override fun ifShowRedDot(): Boolean = true
        })
        mListDataLawyerOrder.add(object : IMenuItem {
            override fun getIIcon(): Int = -1

            override fun getIName(): String = "律师函"

            override fun getIRedDotNum(): String = "1"

            override fun ifShowRedDot(): Boolean = false
        })
        mListDataLawyerOrder.add(object : IMenuItem {
            override fun getIIcon(): Int = -1

            override fun getIName(): String = "邀请接单"

            override fun getIRedDotNum(): String = "1"

            override fun ifShowRedDot(): Boolean = false
        })
        mView.showLawyerOrderList(mListDataLawyerOrder)
    }
}
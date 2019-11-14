package com.hm.iou.lawyer.business.lawyer.workbench

import android.content.Context
import com.hm.iou.base.mvp.HMBasePresenter
import com.hm.iou.lawyer.dict.ModelType

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

            override fun getIModel(): ModelType = ModelType.WAIT_TO_LOADING

            override fun getIIcon(): Int = -1

            override fun getIRedDotNum(): String = "1"

            override fun ifShowRedDot(): Boolean = true
        })
        mListDataWaiteToDo.add(object : IMenuItem {
            override fun getIIcon(): Int = -1

            override fun getIModel(): ModelType = ModelType.WAIT_TO_FINISH

            override fun getIRedDotNum(): String = "1"

            override fun ifShowRedDot(): Boolean = false
        })
        mListDataWaiteToDo.add(object : IMenuItem {
            override fun getIIcon(): Int = -1

            override fun getIModel(): ModelType = ModelType.WAIT_TO_ALL_ORDER

            override fun getIRedDotNum(): String = "1"

            override fun ifShowRedDot(): Boolean = false
        })
        mView.showWaiteToDoList(mListDataWaiteToDo)

        //显示订单
        mListDataLawyerOrder.add(object : IMenuItem {
            override fun getIIcon(): Int = -1

            override fun getIModel(): ModelType = ModelType.LAWYER_LETTER

            override fun getIRedDotNum(): String = "1"

            override fun ifShowRedDot(): Boolean = false
        })
        mListDataLawyerOrder.add(object : IMenuItem {
            override fun getIIcon(): Int = -1

            override fun getIModel(): ModelType = ModelType.LAWYER_INVITE_RECEIVE

            override fun getIRedDotNum(): String = "1"

            override fun ifShowRedDot(): Boolean = false
        })
        mView.showLawyerOrderList(mListDataLawyerOrder)
    }
}
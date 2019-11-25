package com.hm.iou.lawyer.business.lawyer.workbench

import android.content.Context
import com.hm.iou.base.mvp.HMBasePresenter
import com.hm.iou.lawyer.R
import com.hm.iou.lawyer.api.LawyerApi
import com.hm.iou.lawyer.dict.ModelType
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus

/**
 * @author : 借条管家-shilei
 * @version : 0.0.1
 * @Date : 2019-11-08 15:19
 * @E-Mail : afinalstone@foxmail.com
 */
class WorkBenchPresenter(context: Context, view: WorkBenchContract.View) :
    HMBasePresenter<WorkBenchContract.View>(context, view),
    WorkBenchContract.Presenter {

    //待办事项
    private val mListDataWaitToDo = ArrayList<IMenuItem>()
    //律师订单
    private val mListDataLawyerOrder = ArrayList<IMenuItem>()

    private var mRefreshJob: Job? = null

    override fun init() {
        mListDataWaitToDo.add(object : IMenuItem {
            override var redCount: Int = 0

            override fun getIModel(): ModelType = ModelType.WAIT_TO_LOADING

            override fun getIIcon(): Int = R.mipmap.lawyer_ic_workbench_ongoing

            override fun ifShowRedDot(): Boolean = redCount > 0

        })
        mListDataWaitToDo.add(object : IMenuItem {
            override var redCount: Int = 0

            override fun getIIcon(): Int = R.mipmap.lawyer_ic_workbench_complete

            override fun getIModel(): ModelType = ModelType.WAIT_TO_FINISH

            override fun ifShowRedDot(): Boolean = false
        })
        mListDataWaitToDo.add(object : IMenuItem {
            override var redCount: Int = 0
            override fun getIIcon(): Int = R.mipmap.lawyer_ic_workbench_all_order

            override fun getIModel(): ModelType = ModelType.WAIT_TO_ALL_ORDER

            override fun ifShowRedDot(): Boolean = false
        })
        mView.showWaitToDoList(mListDataWaitToDo)

        /**
         * 显示律师订单
         */
        mListDataLawyerOrder.add(object : IMenuItem {
            override var redCount: Int = 0
            override fun getIIcon(): Int = R.mipmap.lawyer_ic_workbench_lawyer_consult

            override fun getIModel(): ModelType = ModelType.LAWYER_ORDER_CONSULT

            override fun ifShowRedDot(): Boolean = false
        })
        mListDataLawyerOrder.add(object : IMenuItem {
            override var redCount: Int = 0
            override fun getIIcon(): Int = R.mipmap.lawyer_ic_workbench_letter

            override fun getIModel(): ModelType = ModelType.LAWYER_LETTER

            override fun ifShowRedDot(): Boolean = false
        })
        mListDataLawyerOrder.add(object : IMenuItem {
            override var redCount: Int = 0
            override fun getIIcon(): Int = R.mipmap.lawyer_ic_workbench_invited_order

            override fun getIModel(): ModelType = ModelType.LAWYER_INVITE_RECEIVE

            override fun ifShowRedDot(): Boolean = redCount > 0
        })
        mView.showLawyerOrderList(mListDataLawyerOrder)
    }

    override fun refreshWorkbenchInfo() {
        mRefreshJob?.cancel()
        mRefreshJob = launch {
            try {
                val result = handleResponse(LawyerApi.getLawyerWorkBench())
                mView.finishRefresh()
                result?.let {
                    mView.showWalletBalance(it.walletBalance)                       //钱包余额
                    mView.showTodayIncome(it.todayIncome)                           //今日收益
                    mView.showTodayCompleteCount(it.todayFinishNum.toString())      //今日完成
                    mView.showTodayOrderCount(it.todayAcceptNum.toString())         //进入接单

                    mListDataWaitToDo.forEach { item ->
                        if (item.getIModel() == ModelType.WAIT_TO_LOADING) {
                            item.updateRedCount(it.doingNum)
                            return@forEach
                        }
                    }

                    mListDataLawyerOrder.forEach { item ->
                        if (item.getIModel() == ModelType.LAWYER_INVITE_RECEIVE) {
                            item.updateRedCount(it.inviteLetterNum)
                            return@forEach
                        }
                    }

                    mView.showWaitToDoList(mListDataWaitToDo)
                    mView.showLawyerOrderList(mListDataLawyerOrder)
                }
            } catch (e: Exception) {
                handleException(e)
                mView.finishRefresh()
            }
        }
    }

}
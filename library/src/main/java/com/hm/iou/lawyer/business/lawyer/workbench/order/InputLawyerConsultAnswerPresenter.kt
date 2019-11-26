package com.hm.iou.lawyer.business.lawyer.workbench.order

import android.content.Context
import com.hm.iou.base.mvp.HMBasePresenter
import com.hm.iou.lawyer.api.LawyerApi
import com.hm.iou.lawyer.bean.req.UpdateLawyerAuthenticationInfReqBean
import com.hm.iou.lawyer.dict.UpdateLawyerAuthenInfoType
import com.hm.iou.lawyer.event.LawyerOrderStatusChangedEvent
import com.hm.iou.lawyer.event.UpdateAuthenInfoEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus

/**
 * 咨询解答
 */
class InputLawyerConsultAnswerPresenter(
    context: Context,
    view: InputLawyerConsultAnswerContract.View
) :
    HMBasePresenter<InputLawyerConsultAnswerContract.View>(context, view),
    InputLawyerConsultAnswerContract.Presenter {

    override fun finishAnswer(billId: String, desc: String) {
        launch {
            try {
                mView.showLoadingView()
                handleResponse(LawyerApi.lawyerAnswer(billId, desc))
                mView.sendMsg()
                mView.dismissLoadingView()
                mView.closeCurrPage()
            } catch (e: Exception) {
                mView.dismissLoadingView()
                handleException(e)
            }
        }

    }


}
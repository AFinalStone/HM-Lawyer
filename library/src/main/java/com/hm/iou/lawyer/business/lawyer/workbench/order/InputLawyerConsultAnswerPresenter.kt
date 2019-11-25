package com.hm.iou.lawyer.business.lawyer.workbench.order

import android.content.Context
import com.hm.iou.base.mvp.HMBasePresenter
import com.hm.iou.lawyer.api.LawyerApi
import com.hm.iou.lawyer.bean.req.UpdateLawyerAuthenticationInfReqBean
import com.hm.iou.lawyer.dict.UpdateLawyerAuthenInfoType
import com.hm.iou.lawyer.event.UpdateAuthenInfoEvent
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

    override fun finishAnswer(answer: String) {
        launch {
            try {
                mView.showLoadingView()
//                val req = UpdateLawyerAuthenticationInfReqBean()
//                req.info = finishAnswer
//                req.type = UpdateLawyerAuthenInfoType.SELF_INTRODUCE.type
//                val result =
//                    handleResponse(LawyerApi.updateLawyerAuthenticationInfo(req))
                mView.dismissLoadingView()
                EventBus.getDefault().post(UpdateAuthenInfoEvent())
                mView.closeCurrPage()
            } catch (e: Exception) {
                mView.dismissLoadingView()
                handleException(e)
            }
        }

    }


}
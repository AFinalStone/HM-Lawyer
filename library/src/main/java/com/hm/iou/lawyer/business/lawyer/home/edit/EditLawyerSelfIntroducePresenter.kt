package com.hm.iou.lawyer.business.lawyer.home.edit

import android.content.Context
import com.hm.iou.base.mvp.HMBasePresenter
import com.hm.iou.lawyer.api.LawyerApi
import com.hm.iou.lawyer.bean.req.UpdateLawyerAuthenticationInfReqBean
import com.hm.iou.lawyer.dict.UpdateLawyerAuthenInfoType
import com.hm.iou.lawyer.event.UpdateAuthenInfoEvent
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus

/**
 * @author : 借条管家-shilei
 * @version : 0.0.1
 * @Date : 2019-11-08 15:19
 * @E-Mail : afinalstone@foxmail.com
 */
class EditLawyerSelfIntroducePresenter(
    context: Context,
    view: EditLawyerSelfIntroduceContract.View
) :
    HMBasePresenter<EditLawyerSelfIntroduceContract.View>(context, view),
    EditLawyerSelfIntroduceContract.Presenter {

    override fun updateLawyerAuthenticationInfo(selfIntroduction: String) {
        launch {
            try {
                mView.showLoadingView()
                val req = UpdateLawyerAuthenticationInfReqBean()
                req.info = selfIntroduction
                req.type = UpdateLawyerAuthenInfoType.SELF_INTRODUCE.type
                val result =
                    handleResponse(LawyerApi.updateLawyerAuthenticationInfo(req))
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
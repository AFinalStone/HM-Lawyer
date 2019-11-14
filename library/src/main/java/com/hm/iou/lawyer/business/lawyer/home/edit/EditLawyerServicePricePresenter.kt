package com.hm.iou.lawyer.business.lawyer.home.edit

import android.content.Context
import com.hm.iou.base.mvp.HMBasePresenter
import com.hm.iou.lawyer.api.LawyerApi
import com.hm.iou.lawyer.event.UpdateAuthenInfoEvent
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus

/**
 * @author : 借条管家-shilei
 * @version : 0.0.1
 * @Date : 2019-11-08 15:19
 * @E-Mail : afinalstone@foxmail.com
 */
class EditLawyerServicePricePresenter(context: Context, view: EditLawyerServicePriceContract.View) :
    HMBasePresenter<EditLawyerServicePriceContract.View>(context, view),
    EditLawyerServicePriceContract.Presenter {

    override fun updateLawyerServicePrice(price: Int, serviceId: Int) {
        launch {
            try {
                mView.showLoadingView()
                val result = handleResponse(LawyerApi.updateLawyerServicePrice(price, serviceId))
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
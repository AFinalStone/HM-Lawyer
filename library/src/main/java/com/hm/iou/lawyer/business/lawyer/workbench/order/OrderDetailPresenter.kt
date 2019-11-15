package com.hm.iou.lawyer.business.lawyer.workbench.order

import android.content.Context
import com.hm.iou.base.mvp.HMBasePresenter
import com.hm.iou.lawyer.api.LawyerApi
import kotlinx.coroutines.launch

/**
 * @author : 借条管家-shilei
 * @version : 0.0.1
 * @Date : 2019-11-08 15:19
 * @E-Mail : afinalstone@foxmail.com
 */
class OrderDetailPresenter(context: Context, view: OrderDetailContract.View) :
    HMBasePresenter<OrderDetailContract.View>(context, view),
    OrderDetailContract.Presenter {

    override fun getOrderDetail(orderId: String, relationId: Int?) {
        launch {
            try {
                val result = LawyerApi.getLawyerLetterDetail(orderId, relationId)

            } catch (e: Exception) {
                handleException(e)
            }
        }
    }

}
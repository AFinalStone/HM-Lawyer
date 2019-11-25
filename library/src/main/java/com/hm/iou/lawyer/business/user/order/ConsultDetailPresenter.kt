package com.hm.iou.lawyer.business.user.order

import android.content.Context
import com.hm.iou.base.mvp.HMBasePresenter

/**
 * Created by hjy on 2019-11-22
 *
 * 律师咨询详情
 */
class ConsultDetailPresenter(context: Context, view: ConsultDetailContract.View) :
    HMBasePresenter<ConsultDetailContract.View>(context, view), ConsultDetailContract.Presenter {

}
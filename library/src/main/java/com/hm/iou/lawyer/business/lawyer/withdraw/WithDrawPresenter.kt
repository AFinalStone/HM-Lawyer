package com.hm.iou.lawyer.business.lawyer.withdraw

import android.content.Context
import com.hm.iou.base.mvp.HMBasePresenter

/**
 * @author : 借条管家-shilei
 * @version : 0.0.1
 * @Date : 2019-11-08 15:19
 * @E-Mail : afinalstone@foxmail.com
 */
class WithDrawPresenter(context: Context, view: WithDrawContract.View) :
    HMBasePresenter<WithDrawContract.View>(context, view),
    WithDrawContract.Presenter {


    override fun init() {
    }
}
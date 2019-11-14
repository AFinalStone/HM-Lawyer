package com.hm.iou.lawyer.business.lawyer.workbench.list

import android.content.Context
import com.hm.iou.base.mvp.HMBasePresenter

/**
 * @author : 借条管家-shilei
 * @version : 0.0.1
 * @Date : 2019-11-08 15:19
 * @E-Mail : afinalstone@foxmail.com
 */
class InviteOrderListPresenter(context: Context, view: InviteOrderListContract.View) :
    HMBasePresenter<InviteOrderListContract.View>(context, view),
    InviteOrderListContract.Presenter {
    override fun getNextPage() {
    }

    override fun getFirstPage() {
    }

}
package com.hm.iou.lawyer.business.lawyer.workbench.list

import android.content.Context
import com.hm.iou.base.mvp.HMBasePresenter

/**
 * @author : 借条管家-shilei
 * @version : 0.0.1
 * @Date : 2019-11-08 15:19
 * @E-Mail : afinalstone@foxmail.com
 */
class LetterOrderListPresenter(context: Context, view: LetterOrderListContract.View) :
    HMBasePresenter<LetterOrderListContract.View>(context, view),
    LetterOrderListContract.Presenter {

    override fun getFirstPage() {
    }

    override fun getNextPage() {
    }


}
package com.hm.iou.lawyer.business.lawyer.wallet

import android.content.Context
import com.hm.iou.base.mvp.HMBasePresenter

/**
 * @author : 借条管家-shilei
 * @version : 0.0.1
 * @Date : 2019-11-08 15:19
 * @E-Mail : afinalstone@foxmail.com
 */
class WalletPresenter(context: Context, view: WalletContract.View) :
    HMBasePresenter<WalletContract.View>(context, view),
    WalletContract.Presenter {


    override fun init() {
    }
}
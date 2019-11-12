package com.hm.iou.lawyer.business.lawyer.home

import android.content.Context
import com.hm.iou.base.mvp.HMBasePresenter

/**
 * @author : 借条管家-shilei
 * @version : 0.0.1
 * @Date : 2019-11-08 15:19
 * @E-Mail : afinalstone@foxmail.com
 */
class HomePresenter(context: Context, view: HomeContract.View) :
    HMBasePresenter<HomeContract.View>(context, view),
    HomeContract.Presenter {


    override fun init() {

    }


}
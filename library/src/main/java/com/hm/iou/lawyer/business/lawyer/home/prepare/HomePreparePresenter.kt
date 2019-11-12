package com.hm.iou.lawyer.business.lawyer.home.prepare

import android.content.Context
import com.hm.iou.base.mvp.HMBasePresenter
import com.hm.iou.lawyer.api.LawyerApi
import com.hm.iou.lawyer.bean.res.GetLawyerHomeStatusResBean
import kotlinx.coroutines.launch

/**
 * @author : 借条管家-shilei
 * @version : 0.0.1
 * @Date : 2019-11-08 15:19
 * @E-Mail : afinalstone@foxmail.com
 */
class HomePreparePresenter(context: Context, view: HomePrepareContract.View) :
    HMBasePresenter<HomePrepareContract.View>(context, view),
    HomePrepareContract.Presenter {


    override fun init() {
        launch {
            try {
                mView.showLoadingView()

                val result = handleResponse(LawyerApi.getLawyerHomeStatus())
                result?.let {
                    when (result.firstAuthState) {
                        0 -> {
                            mView.toAuthenticationPage()
                        }
                        2 -> mView.toAuthenProgressPage(false)
                        3 -> {
                            mView.toAuthenticationPage()
                        }
                        4 -> mView.toAuthenProgressPage(true)
                    }
                }
                mView.dismissLoadingView()
                mView.closeCurrPage()
            } catch (e: Exception) {
                e.printStackTrace()
                handleException(e)
                mView.dismissLoadingView()
                mView.closeCurrPage()
            }
        }
    }


}
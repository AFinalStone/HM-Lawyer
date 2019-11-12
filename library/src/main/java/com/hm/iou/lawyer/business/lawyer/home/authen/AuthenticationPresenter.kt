package com.hm.iou.lawyer.business.lawyer.home.authen

import android.content.Context
import com.hm.iou.base.mvp.HMBasePresenter
import com.hm.iou.lawyer.api.LawyerApi
import com.hm.iou.lawyer.bean.req.LawyerAuthenticationReqBean
import kotlinx.coroutines.launch

/**
 * @author : 借条管家-shilei
 * @version : 0.0.1
 * @Date : 2019-11-08 15:19
 * @E-Mail : afinalstone@foxmail.com
 */
class AuthenticationPresenter(context: Context, view: AuthenticationContract.View) :
    HMBasePresenter<AuthenticationContract.View>(context, view),
    AuthenticationContract.Presenter {

    override fun init() {

    }

    override fun lawyerAuthentication(req: LawyerAuthenticationReqBean) {
        launch {
            try {
                mView.showLoadingView()
                val result = LawyerApi.LawyerAuthentication(req)
                result?.let {

                    mView.dismissLoadingView()
                }

            } catch (e: Exception) {
                e.printStackTrace()
                handleException(e)
                mView.dismissLoadingView()
            }
        }
    }


}
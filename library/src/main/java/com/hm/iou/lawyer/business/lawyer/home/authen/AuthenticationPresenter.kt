package com.hm.iou.lawyer.business.lawyer.home.authen

import android.content.Context
import com.hm.iou.base.mvp.HMBasePresenter
import com.hm.iou.database.table.IouData
import com.hm.iou.lawyer.api.LawyerApi
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

    override fun lawyerAuthentication(
        certificateCode: String,
        lawyerFirmName: String,
        certificateStartTime: String,
        selfIntroduction: String,
        headerImagePath: String,
        listAuthenImage: List<String>,
        listCertificateImage: MutableList<IouData.FileEntity>?
    ) {

    }

    private fun uploadDataToService() {
        launch {
            try {
                mView.showLoadingView()

//                val result = LawyerApi.LawyerAuthentication()
//                result?.let {
//
//                    mView.dismissLoadingView()
//                }

            } catch (e: Exception) {
                e.printStackTrace()
                handleException(e)
                mView.dismissLoadingView()
            }
        }
    }


}
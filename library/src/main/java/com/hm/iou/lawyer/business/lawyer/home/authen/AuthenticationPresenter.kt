package com.hm.iou.lawyer.business.lawyer.home.authen

import android.content.Context
import com.hm.iou.base.file.FileApi
import com.hm.iou.base.file.FileBizType
import com.hm.iou.base.mvp.HMBasePresenter
import com.hm.iou.database.table.IouData
import kotlinx.coroutines.launch
import java.io.File

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
        launch {
            try {
                mView.showLoadingView()
                var file = File(headerImagePath)
                val resultHeader = FileApi.uploadImageByCoroutine(file, FileBizType.Lawyer)
//                val result = LawyerApi.LawyerAuthentication()
                resultHeader.let {

                    mView.dismissLoadingView()
                }
//                for (authenImage in listAuthenImage) {
//                    file = File(authenImage)
//                    val resultAuthenImage = FileApi.uploadImageByCoroutine(file, FileBizType.Lawyer)
//
//                }
//                val result = LawyerApi.LawyerAuthentication()
                resultHeader.let {

                    mView.dismissLoadingView()
                }

            } catch (e: Exception) {
                e.printStackTrace()
                handleException(e)
                mView.dismissLoadingView()
            }
        }
    }

    private fun uploadDataToService() {

    }


}
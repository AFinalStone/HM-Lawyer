package com.hm.iou.lawyer.business.lawyer.home.authen

import android.content.Context
import com.hm.iou.base.file.FileApi
import com.hm.iou.base.file.FileBizType
import com.hm.iou.base.mvp.HMBasePresenter
import com.hm.iou.database.table.IouData
import com.hm.iou.lawyer.api.LawyerApi
import com.hm.iou.lawyer.bean.req.LawyerAuthenticationReqBean
import com.hm.iou.lawyer.business.NavigationHelper
import com.hm.iou.logger.Logger
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
        listAuthenImagePath: List<String>,
        listCertificateImagePath: MutableList<IouData.FileEntity>?
    ) {
        launch {
            try {
                mView.showLoadingView("上传个人证件...")
                //头像
                var headerImageFileId: String = ""
                var file = File(headerImagePath)
                var result =
                    handleResponse(FileApi.uploadImageByCoroutine(file, FileBizType.Lawyer))
                result?.let {
                    Logger.d("头像图片id" + result?.fileId)
                    Logger.d("头像图片链接" + result?.fileUrl)
                    headerImageFileId = result?.fileId ?: ""
                }
                //律师职业照片
                mView.showLoadingView("上传执业证...")
                val listAuthenImageFileId = ArrayList<String>()
                for (authenImage in listAuthenImagePath) {

                    file = File(authenImage)
                    result =
                        handleResponse(FileApi.uploadImageByCoroutine(file, FileBizType.Lawyer))
                    listAuthenImageFileId.add(result?.fileId ?: "")
                }
                //荣誉证书
                val listCertificateImageFileId = ArrayList<String>()
                listCertificateImagePath?.let {
                    mView.showLoadingView("上传荣誉证书...")
                    val it = listCertificateImagePath.iterator()
                    while (it.hasNext()) {
                        file = File(it.next().value.replace("file://", ""))
                        result =
                            handleResponse(FileApi.uploadImageByCoroutine(file, FileBizType.Lawyer))
                        listCertificateImageFileId.add(result?.fileId ?: "")
                    }
                }
                //开始认证
                mView.showLoadingView("律师认证...")
                val req = LawyerAuthenticationReqBean(
                    certificateCode, lawyerFirmName, certificateStartTime, selfIntroduction
                    , headerImageFileId, listAuthenImageFileId, listCertificateImageFileId
                )
                val lawyerAuthenResult = handleResponse(LawyerApi.lawyerAuthentication(req))
                lawyerAuthenResult?.let {
                    NavigationHelper.toAuthenticationProgress(mContext, false)
                }

            } catch (e: Exception) {
                e.printStackTrace()
                handleException(e)
                mView.dismissLoadingView()
            }
        }
    }


}
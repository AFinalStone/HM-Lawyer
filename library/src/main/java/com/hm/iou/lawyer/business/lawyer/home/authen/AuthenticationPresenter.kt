package com.hm.iou.lawyer.business.lawyer.home.authen

import android.content.Context
import android.text.TextUtils
import com.hm.iou.base.file.FileApi
import com.hm.iou.base.file.FileBizType
import com.hm.iou.base.mvp.HMBasePresenter
import com.hm.iou.database.table.IouData
import com.hm.iou.lawyer.api.LawyerApi
import com.hm.iou.lawyer.bean.req.LawyerAuthenticationReqBean
import com.hm.iou.lawyer.bean.res.ImageUrlFileIdBean
import com.hm.iou.lawyer.business.NavigationHelper
import com.hm.iou.logger.Logger
import com.hm.iou.tools.kt.startActivity
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
        launch {
            try {
                val result = handleResponse(LawyerApi.getLawyerAuthenticationFailedInfo())
                result?.let {
                    mView.showDetail(it)
                }
            } catch (e: Exception) {
                handleException(e, showCommError = false, showBusinessError = false)
            }
        }
    }


    override fun lawyerAuthentication(
        certificateCode: String,
        lawyerFirmName: String,
        certificateStartTime: String,
        selfIntroduction: String,
        headerImageBean: ImageUrlFileIdBean?,
        authenImageFrontBean: ImageUrlFileIdBean?,
        authenImageBackBean: ImageUrlFileIdBean?,
        listCertificateImagePath: MutableList<IouData.FileEntity>?
    ) {
        launch {
            try {
                mView.showLoadingView("上传个人证件...")
                //头像
                var headerImageFileId: String = headerImageBean?.picId ?: ""
                if (headerImageFileId.isEmpty()) {
                    val file = File(headerImageBean?.url?.replace("file://", ""))
                    val result = handleResponse(
                        FileApi.uploadImageByCoroutine(
                            file,
                            FileBizType.Lawyer
                        )
                    )
                    headerImageFileId = result?.fileId ?: ""
                }

                //律师职业照片正面
                mView.showLoadingView("上传执业证...")
                val listAuthenImageFileId = ArrayList<String>()
                var authenImageFrontBeanFileId: String = authenImageFrontBean?.picId ?: ""
                if (authenImageFrontBeanFileId.isEmpty()) {
                    val file = File(authenImageFrontBean?.url?.replace("file://", ""))
                    val result = handleResponse(
                        FileApi.uploadImageByCoroutine(
                            file,
                            FileBizType.Lawyer
                        )
                    )
                    authenImageFrontBeanFileId = result?.fileId ?: ""
                }
                listAuthenImageFileId.add(authenImageFrontBeanFileId)
                //律师职业照片正面反面
                var authenImageBackBeanFileId: String = authenImageBackBean?.picId ?: ""
                if (authenImageBackBeanFileId.isEmpty()) {
                    val file = File(authenImageBackBean?.url?.replace("file://", ""))
                    val result = handleResponse(
                        FileApi.uploadImageByCoroutine(
                            file,
                            FileBizType.Lawyer
                        )
                    )
                    authenImageBackBeanFileId = result?.fileId ?: ""
                }
                listAuthenImageFileId.add(authenImageBackBeanFileId)
                //荣誉证书
                val listCertificateImageFileId = ArrayList<String>()
                if (!listCertificateImagePath.isNullOrEmpty()) {
                    val iterator = listCertificateImagePath.iterator()
                    while (iterator.hasNext()) {
                        val next = iterator.next()
                        val filedId = next.id ?: ""
                        val filedUrl = next.value ?: ""
                        Logger.d("filedId==${filedId}  filedUrl==${filedUrl}")
                        if (TextUtils.isEmpty(filedId)) {
                            val file = File(next.value.replace("file://", ""))
                            val result = handleResponse(
                                FileApi.uploadImageByCoroutine(
                                    file,
                                    FileBizType.Lawyer
                                )
                            )
                            listCertificateImageFileId.add(result?.fileId ?: "")
                        } else {
                            listCertificateImageFileId.add(filedId)
                        }
                    }
                }
                //开始认证
                mView.showLoadingView("律师认证...")
                val req = LawyerAuthenticationReqBean(
                    certificateCode, lawyerFirmName, certificateStartTime, selfIntroduction
                    , headerImageFileId, listAuthenImageFileId, listCertificateImageFileId
                )
                val lawyerAuthenResult = handleResponse(LawyerApi.lawyerAuthentication(req))
                mView.dismissLoadingView()
                lawyerAuthenResult?.let {
                    mContext.startActivity<AuthenProgressActivity>(
                        AuthenProgressActivity.EXTRA_KEY_IF_AUTHENTICATION_FAILED to false
                    )
                    mView.closeCurrPage()
                }

            } catch (e: Exception) {
                e.printStackTrace()
                mView.dismissLoadingView()
                handleException(e)
            }
        }

    }

}
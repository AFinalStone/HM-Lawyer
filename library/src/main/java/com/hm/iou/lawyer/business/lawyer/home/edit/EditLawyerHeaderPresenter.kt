package com.hm.iou.lawyer.business.lawyer.home.edit

import android.content.Context
import com.hm.iou.base.file.FileApi
import com.hm.iou.base.file.FileBizType
import com.hm.iou.base.mvp.HMBasePresenter
import com.hm.iou.lawyer.api.LawyerApi
import com.hm.iou.lawyer.bean.req.UpdateLawyerAuthenticationInfReqBean
import com.hm.iou.lawyer.dict.UpdateLawyerAuthenInfoType
import com.hm.iou.logger.Logger
import kotlinx.coroutines.launch
import java.io.File

/**
 * @author : 借条管家-shilei
 * @version : 0.0.1
 * @Date : 2019-11-08 15:19
 * @E-Mail : afinalstone@foxmail.com
 */
class EditLawyerHeaderPresenter(context: Context, view: EditLawyerHeaderContract.View) :
    HMBasePresenter<EditLawyerHeaderContract.View>(context, view),
    EditLawyerHeaderContract.Presenter {
    override fun uploadHeader(file: File) {
        launch {
            try {
                mView.showLoadingView("上传头像")
                //头像
                var result =
                    handleResponse(FileApi.uploadImageByCoroutine(file, FileBizType.Lawyer))
                result?.let {
                    Logger.d("头像图片id" + result.fileId)
                    Logger.d("头像图片链接" + result.fileUrl)
                    val headerImageFileId = result.fileId ?: ""
                    val headerImageUrl = result.fileUrl ?: ""
                    mView.showLoadingView("设置头像")
                    val req = UpdateLawyerAuthenticationInfReqBean()
                    req.image = headerImageFileId
                    req.type = UpdateLawyerAuthenInfoType.HEADER.type
                    handleResponse(LawyerApi.updateLawyerAuthenticationInfo(req))
                    mView.dismissLoadingView()
                    mView.showUserAvatar(headerImageUrl)
                }
            } catch (e: Exception) {
                mView.dismissLoadingView()
                handleException(e)
            }

        }
    }


}
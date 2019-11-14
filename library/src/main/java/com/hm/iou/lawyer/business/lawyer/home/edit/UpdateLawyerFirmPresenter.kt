package com.hm.iou.lawyer.business.lawyer.home.edit

import android.content.Context
import com.hm.iou.base.file.FileApi
import com.hm.iou.base.file.FileBizType
import com.hm.iou.base.mvp.HMBasePresenter
import com.hm.iou.lawyer.api.LawyerApi
import com.hm.iou.lawyer.bean.req.UpdateLawyerAuthenticationInfReqBean
import com.hm.iou.lawyer.business.lawyer.home.prepare.HomePrepareActivity
import com.hm.iou.tools.kt.startActivity
import kotlinx.coroutines.launch
import java.io.File

/**
 * @author : 借条管家-shilei
 * @version : 0.0.1
 * @Date : 2019-11-08 15:19
 * @E-Mail : afinalstone@foxmail.com
 */
class UpdateLawyerFirmPresenter(context: Context, view: UpdateLawyerFirmContract.View) :
    HMBasePresenter<UpdateLawyerFirmContract.View>(context, view),
    UpdateLawyerFirmContract.Presenter {

    override fun updateLawyerAuthenticationInfo(
        licenseNumber: String,
        lawFirm: String,
        listAuthenImagePath: List<String>
    ) {
        launch {
            try {
                //律师职业照片
                mView.showLoadingView("上传执业证...")
                val listAuthenImageFileId = ArrayList<String>()
                for (authenImage in listAuthenImagePath) {

                    val file = File(authenImage)
                    val result =
                        handleResponse(FileApi.uploadImageByCoroutine(file, FileBizType.Lawyer))
                    listAuthenImageFileId.add(result?.fileId ?: "")
                }
                //开始认证
                mView.showLoadingView("年检认证...")
                val req = UpdateLawyerAuthenticationInfReqBean()
                req.licenseNumber = licenseNumber
                req.lawFirm = lawFirm
                req.authCerts = listAuthenImageFileId
                val lawyerAuthenResult =
                    handleResponse(LawyerApi.updateLawyerAuthenticationInfo(req))
                mView.dismissLoadingView()
                mContext.startActivity<HomePrepareActivity>()
                mView.closeCurrPage()

            } catch (e: Exception) {
                e.printStackTrace()
                mView.dismissLoadingView()
                handleException(e)
            }
        }
    }


}
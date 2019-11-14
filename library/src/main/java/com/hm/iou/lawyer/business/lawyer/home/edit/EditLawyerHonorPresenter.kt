package com.hm.iou.lawyer.business.lawyer.home.edit

import android.content.Context
import android.text.TextUtils
import com.hm.iou.base.file.FileApi
import com.hm.iou.base.file.FileBizType
import com.hm.iou.base.mvp.HMBasePresenter
import com.hm.iou.database.table.IouData
import com.hm.iou.lawyer.api.LawyerApi
import com.hm.iou.lawyer.bean.req.UpdateLawyerAuthenticationInfReqBean
import com.hm.iou.lawyer.dict.UpdateLawyerAuthenInfoType
import com.hm.iou.lawyer.event.UpdateAuthenInfoEvent
import com.hm.iou.logger.Logger
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import java.io.File

/**
 * @author : 借条管家-shilei
 * @version : 0.0.1
 * @Date : 2019-11-08 15:19
 * @E-Mail : afinalstone@foxmail.com
 */
class EditLawyerHonorPresenter(context: Context, view: EditLawyerHonorContract.View) :
    HMBasePresenter<EditLawyerHonorContract.View>(context, view),
    EditLawyerHonorContract.Presenter {

    override fun updateLawyerAuthenticationInfo(listCertificateImagePath: MutableList<IouData.FileEntity>) {
        launch {
            try {
                Logger.d("长度" + listCertificateImagePath.size)
                mView.showLoadingView()
                //荣誉证书
                val listCertificateImageFileId = ArrayList<String>()
                mView.showLoadingView("上传荣誉证书...")
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
                val req = UpdateLawyerAuthenticationInfReqBean()
                req.honors = listCertificateImageFileId
                req.type = UpdateLawyerAuthenInfoType.LAWYER_HONOR.type
                val result = handleResponse(LawyerApi.updateLawyerAuthenticationInfo(req))
                mView.dismissLoadingView()
                EventBus.getDefault().post(UpdateAuthenInfoEvent())
                mView.closeCurrPage()
            } catch (e: Exception) {
                mView.dismissLoadingView()
                handleException(e)
            }
        }
    }


}
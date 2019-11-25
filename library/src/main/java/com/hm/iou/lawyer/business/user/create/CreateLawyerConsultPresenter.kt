package com.hm.iou.lawyer.business.user.create

import android.content.Context
import com.hm.iou.base.file.FileApi
import com.hm.iou.base.file.FileBizType
import com.hm.iou.base.mvp.HMBasePresenter
import com.hm.iou.database.table.IouData
import com.hm.iou.lawyer.api.LawyerApi
import com.hm.iou.lawyer.business.NavigationHelper
import kotlinx.coroutines.launch
import java.io.File

/**
 * Created by hjy on 2019/11/22
 *
 * 创建律师咨询
 */
class CreateLawyerConsultPresenter(context: Context, view: CreateLawyerConsultContract.View) :
    HMBasePresenter<CreateLawyerConsultContract.View>(context, view),
    CreateLawyerConsultContract.Presenter {

    override fun createLawyerConsult(
        lawyerId: String?,
        price: Int,
        caseDesc: String?,
        list: List<IouData.FileEntity>?,
        innerUser: Boolean
    ) {
        if (price <= 0 || caseDesc.isNullOrEmpty()) {
            mView.toastMessage("请填写完整的信息")
            return
        }

        launch {
            mView.showLoadingView()
            try {
                val fileIdList = mutableListOf<String>()
                if (!list.isNullOrEmpty()) {
                    list.forEach { fileEntity ->
                        if (fileEntity.id.isNullOrEmpty()) {
                            val filePath = fileEntity.value
                            if (filePath != null && filePath.startsWith("file://")) {
                                val uploadResult = handleResponse(
                                    FileApi.uploadImageByCoroutine(
                                        File(filePath.replace("file://", "")), FileBizType.Lawyer))
                                if (uploadResult != null) {
                                    fileIdList.add(uploadResult.fileId)
                                }
                            }
                        } else {
                            fileIdList.add(fileEntity.id)
                        }
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
                handleException(e)
                mView.dismissLoadingView()
            }
        }

    }

    override fun createOrderSuccess() {

    }

}
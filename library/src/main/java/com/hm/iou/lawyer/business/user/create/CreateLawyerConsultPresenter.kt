package com.hm.iou.lawyer.business.user.create

import android.content.Context
import com.hm.iou.base.file.FileApi
import com.hm.iou.base.file.FileBizType
import com.hm.iou.base.mvp.HMBasePresenter
import com.hm.iou.database.table.IouData
import com.hm.iou.lawyer.api.LawyerApi
import com.hm.iou.lawyer.bean.req.CreateLawyerConsultReqBean
import com.hm.iou.lawyer.business.NavigationHelper
import com.hm.iou.lawyer.event.AddLawyerConsultEvent
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import java.io.File

/**
 * Created by hjy on 2019/11/22
 *
 * 创建律师咨询
 */
class CreateLawyerConsultPresenter(context: Context, view: CreateLawyerConsultContract.View) :
    HMBasePresenter<CreateLawyerConsultContract.View>(context, view),
    CreateLawyerConsultContract.Presenter {

    private var mBillId: String? = null

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

                val reqBean = CreateLawyerConsultReqBean()
                reqBean.fileIds = fileIdList
                if (lawyerId.isNullOrEmpty()) {
                    reqBean.appoint = 0
                } else {
                    reqBean.appoint = 1
                    reqBean.lawyerId = lawyerId
                }
                reqBean.caseDescription = caseDesc
                reqBean.price = price

                val result = handleResponse(LawyerApi.createLawyerConsult(reqBean))
                mView.dismissLoadingView()
                val billId = result?.billId
                billId?.let {
                    mBillId = billId
                    NavigationHelper.toPayLawyerLetter(mContext, innerUser, it, price.toString(),
                        CreateLawyerConsultActivity.REQ_PAY_LAWYER_CONSULT
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                handleException(e)
                mView.dismissLoadingView()
            }
        }

    }

    override fun createOrderSuccess() {
        EventBus.getDefault().post(AddLawyerConsultEvent())
        NavigationHelper.toLawyerConsultDetailPage(mContext, mBillId ?: "")
        mView.closeCurrPage()
    }

}
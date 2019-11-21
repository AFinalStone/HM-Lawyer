package com.hm.iou.lawyer.business.lawyer.workbench.order

import android.content.Context
import com.hm.iou.base.file.FileApi
import com.hm.iou.base.file.FileBizType
import com.hm.iou.base.mvp.HMBasePresenter
import com.hm.iou.lawyer.api.LawyerApi
import com.hm.iou.lawyer.event.LawyerOrderStatusChangedEvent
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import java.io.File

/**
 * @author : 借条管家-shilei
 * @version : 0.0.1
 * @Date : 2019-11-08 15:19
 * @E-Mail : afinalstone@foxmail.com
 */
class LawyerFinishOrderPresenter(
    context: Context,
    view: LawyerFinishOrderContract.View
) :
    HMBasePresenter<LawyerFinishOrderContract.View>(context, view),
    LawyerFinishOrderContract.Presenter {

    override fun finishOrder(
        billId: String,
        expressName: String,
        expressNumber: String,
        finishImgsPath: List<String>
    ) {
        launch {
            try {
                mView.showLoadingView()
                val fileIdList = ArrayList<String>()
                for (path in finishImgsPath) {
                    val file = File(path.replace("file://", ""))
                    val result = handleResponse(
                        FileApi.uploadImageByCoroutine(
                            file,
                            FileBizType.Lawyer
                        )
                    )
                    fileIdList.add(result?.fileId ?: "")
                }
                handleResponse(
                    LawyerApi.lawyerFinishOrder(
                        billId,
                        expressName,
                        expressNumber,
                        fileIdList
                    )
                )
                mView.toastMessage("订单已完成")
                EventBus.getDefault().post(LawyerOrderStatusChangedEvent())
                mView.closeCurrPage()
            } catch (e: Exception) {
                mView.dismissLoadingView()
                handleException(e)
            }
        }
    }

    override fun getMailList() {
        launch {
            try {
                mView.showLoadingView()
                val result = handleResponse(LawyerApi.getMailList())
                mView.dismissLoadingView()
                result?.let {
                    mView.showMailList(it)
                }
            } catch (e: Exception) {
                mView.dismissLoadingView()
                handleException(e)
            }
        }
    }

}
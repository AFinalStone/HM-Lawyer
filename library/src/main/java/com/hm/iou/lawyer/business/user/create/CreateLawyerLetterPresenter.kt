package com.hm.iou.lawyer.business.user.create

import android.content.Context
import com.hm.iou.base.constants.HMConstants
import com.hm.iou.base.file.FileApi
import com.hm.iou.base.file.FileBizType
import com.hm.iou.base.mvp.BaseContract
import com.hm.iou.base.mvp.HMBasePresenter
import com.hm.iou.database.table.IouData
import com.hm.iou.lawyer.api.LawyerApi
import com.hm.iou.lawyer.bean.LetterReceiverBean
import com.hm.iou.lawyer.bean.req.CreateLawyerLetterReqBean
import com.hm.iou.lawyer.business.NavigationHelper
import com.hm.iou.lawyer.business.user.create.CreateLawyerLetterActivity.Companion.REQ_PAY_LAWYER_LETTER
import com.hm.iou.lawyer.dict.LawyerLetterSource
import com.hm.iou.router.Router
import com.hm.iou.tools.StringUtil
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import java.io.File

/**
 * Created by hjy on 2019/11/12
 *
 * 创建律师函
 */
class CreateLawyerLetterPresenter(context: Context, view: CreateLawyerLetterContract.View) :
    HMBasePresenter<CreateLawyerLetterContract.View>(context, view),
    CreateLawyerLetterContract.Presenter {

    private var mSource: LawyerLetterSource? = null

    private var mBillId: String? = null

    override fun initSource(sourceStr: String?) {
        if (sourceStr == LawyerLetterSource.SOURCE_IOU.source.toString()) {
            mSource = LawyerLetterSource.SOURCE_IOU
        } else {
            mSource = LawyerLetterSource.SOURCE_ONLINE
        }
    }

    override fun createLawyerLetter(
        name: String?,
        mobile: String?,
        lawyerId: String?,
        price: Int,
        receiverInfo: LetterReceiverBean?,
        caseDesc: String?,
        list: List<IouData.FileEntity>?,
        innerUser: Boolean
    ) {
        if (name.isNullOrEmpty() || mobile.isNullOrEmpty() || price <= 0 || caseDesc.isNullOrEmpty() || receiverInfo == null) {
            mView.toastMessage("请填写完整的信息")
            return
        }
        if (!StringUtil.matchRegex(mobile, HMConstants.REG_MOBILE)) {
            mView.toastMessage("请输入正确的手机号")
            return
        }
        val reqBean = CreateLawyerLetterReqBean()
        if (lawyerId.isNullOrEmpty()) {
            reqBean.appoint = 0
        } else {
            reqBean.appoint = 1
            reqBean.lawyerId = lawyerId
        }
        reqBean.name = name
        reqBean.mobile = mobile
        reqBean.price = price
        reqBean.caseDescription = caseDesc
        reqBean.receiverInfoReq = receiverInfo
        reqBean.source = (mSource ?: LawyerLetterSource.SOURCE_ONLINE).source

        launch {
            mView.showLoadingView()
            try {
                val fileIdList = mutableListOf<String>()
                if (!list.isNullOrEmpty()) {
                    list.forEach { fileEntity ->
                        if (fileEntity.id.isNullOrEmpty()) {
                            val filePath = fileEntity.value
                            if (filePath != null && filePath.startsWith("file://")) {
                                val uploadResult = handleResponse(FileApi.uploadImageByCoroutine(File(filePath.replace("file://", "")), FileBizType.Lawyer))
                                if (uploadResult != null) {
                                    fileIdList.add(uploadResult.fileId)
                                }
                            }
                        } else {
                            fileIdList.add(fileEntity.id)
                        }
                    }
                }
                reqBean.fileIds = fileIdList

                val result = handleResponse(LawyerApi.createLawyerLetter(reqBean))
                mView.dismissLoadingView()
                val billId = result?.billId
                billId?.let {
                    mBillId = billId
                    NavigationHelper.toPayLawyerLetter(mContext, innerUser, it, price.toString(), REQ_PAY_LAWYER_LETTER)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                handleException(e)
                mView.dismissLoadingView()
            }
        }
    }

    override fun createOrderSuccess() {
        NavigationHelper.toUserOrderDetailPage(mContext, mBillId ?: "")
        mView.closeCurrPage()
    }
}
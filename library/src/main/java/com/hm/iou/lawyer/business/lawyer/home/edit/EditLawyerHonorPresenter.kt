package com.hm.iou.lawyer.business.lawyer.home.edit

import android.content.Context
import com.hm.iou.base.mvp.HMBasePresenter
import com.hm.iou.lawyer.api.LawyerApi
import com.hm.iou.lawyer.business.NavigationHelper
import com.hm.iou.lawyer.business.lawyer.home.HomeActivity
import com.hm.iou.lawyer.business.lawyer.home.authen.AuthenProgressActivity
import com.hm.iou.lawyer.dict.UpdateLawFirmStatusType
import com.hm.iou.lawyer.dict.UpdateYeaCheckStatusType
import com.hm.iou.tools.kt.startActivity
import kotlinx.coroutines.launch

/**
 * @author : 借条管家-shilei
 * @version : 0.0.1
 * @Date : 2019-11-08 15:19
 * @E-Mail : afinalstone@foxmail.com
 */
class EditLawyerHonorPresenter(context: Context, view: EditLawyerHonorContract.View) :
    HMBasePresenter<EditLawyerHonorContract.View>(context, view),
    EditLawyerHonorContract.Presenter {


}
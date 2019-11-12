package com.hm.iou.lawyer.business.user.create

import android.content.Context
import com.hm.iou.base.mvp.BaseContract
import com.hm.iou.base.mvp.HMBasePresenter

/**
 * Created by hjy on 2019/11/12
 *
 * 创建律师函
 */
class CreateLawyerLetterPresenter(context: Context, view: CreateLawyerLetterContract.View) :
    HMBasePresenter<CreateLawyerLetterContract.View>(context, view),
    CreateLawyerLetterContract.Presenter {


}
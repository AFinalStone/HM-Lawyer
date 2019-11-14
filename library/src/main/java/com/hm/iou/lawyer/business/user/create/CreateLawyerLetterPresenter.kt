package com.hm.iou.lawyer.business.user.create

import android.content.Context
import com.hm.iou.base.mvp.BaseContract
import com.hm.iou.base.mvp.HMBasePresenter
import com.hm.iou.router.Router

/**
 * Created by hjy on 2019/11/12
 *
 * 创建律师函
 */
class CreateLawyerLetterPresenter(context: Context, view: CreateLawyerLetterContract.View) :
    HMBasePresenter<CreateLawyerLetterContract.View>(context, view),
    CreateLawyerLetterContract.Presenter {


    private fun toPay(innerUser: Boolean, billId: String, money: String) {
        Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/pay/lawyer_letter_pay")
            .withString("package_title", "律师函服务费")
            .withString("package_money", money)
            .withString("package_content", "文案待定")
            .withString("bill_id", billId)
            .withString("inner_user", if (innerUser) "1" else "")
            .navigation(mContext)
    }

}
package com.hm.iou.lawyer.business.user.lawyer

import android.content.Context
import com.hm.iou.base.mvp.HMBasePresenter
import com.hm.iou.lawyer.business.NavigationHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception

/**
 * Created by hjy on 2019/11/12
 *
 * 律师详情
 */
class LawyerDetailPresenter(context: Context, view: LawyerDetailContract.View) :
    HMBasePresenter<LawyerDetailContract.View>(context, view), LawyerDetailContract.Presenter {


    override fun getLawyerDetailInfo(lawyerId: String) {
        launch {
            mView.showInitLoading(true)
            mView.showLawyerDetailView(false)

            try {
                delay(2000)

                mView.showAvatar("http://b-ssl.duitang.com/uploads/item/201707/10/20170710070015_XjiQM.jpeg")

                mView.showLawyerName("张三律师")
                mView.showLawyerAgeLimit("执业10年")
                mView.showLawyerCompany("杭州泰杭律师事务所")
                mView.showLawyerLocation("浙江省杭州市余杭区")
                mView.showLawyerLetterDesc("这是律师介绍文案")
                mView.showLawyerLetterPrice("￥100/份")
                mView.showLawyerDesc("律师介绍律师介绍律师介绍律师介绍律师介绍律师介绍律师介绍律师介绍律师介绍律师介绍律师介绍律师介绍律师介绍律师介绍律师介绍律师介绍律师介绍律师介绍")

                val list = listOf(
                    "http://b-ssl.duitang.com/uploads/item/201707/10/20170710070015_XjiQM.jpeg",
                    "http://b-ssl.duitang.com/uploads/item/201707/10/20170710070015_XjiQM.jpeg",
                    "http://b-ssl.duitang.com/uploads/item/201707/10/20170710070015_XjiQM.jpeg"
                )
                mView.showLawyerHonorImage(list)

                mView.showInitLoading(false)
                mView.showLawyerDetailView(true)
            } catch (e: Exception) {
                mView.showLoadError("error")
                mView.showLawyerDetailView(false)
            }
        }
    }

    override fun toCreateLawyerLetter() {
        NavigationHelper.toCreateLawyerLetter(mContext, "123", 300)
    }
}
package com.hm.iou.lawyer.business.user.lawyer

import android.content.Context
import com.hm.iou.base.mvp.HMBasePresenter
import com.hm.iou.lawyer.R
import com.hm.iou.lawyer.api.LawyerApi
import com.hm.iou.lawyer.bean.res.GetLawyerHomeDetailResBean
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

    private var mDetailInfo: GetLawyerHomeDetailResBean? = null

    override fun getLawyerDetailInfo(lawyerId: String) {
        launch {
            mView.showInitLoading(true)
            mView.showLawyerDetailView(false)
            try {
                mDetailInfo = handleResponse(LawyerApi.getLawyerHomeDetail(lawyerId))
                mDetailInfo?.let {
                    mView.showAvatar(it.image)
                    mView.showLawyerName("${it.authName}律师")
                    mView.showLawyerAgeLimit("执业${it.holdingYearCount}年")
                    mView.showLawyerCompany(it.lawFirm)
                    mView.showLawyerLocation(it.location)
                    mView.showLawyerService(it.services)
                    mView.showLawyerDesc(it.info)
                    mView.showLawyerHonorImage(it.honors)
                }
                mView.showInitLoading(false)
                mView.showLawyerDetailView(true)
            } catch (e: Exception) {
                handleException(e, showBusinessError = false, showCommError = false)
                mView.showLoadError("数据获取失败，请重试")
                mView.showLawyerDetailView(false)
            }
        }
    }

}
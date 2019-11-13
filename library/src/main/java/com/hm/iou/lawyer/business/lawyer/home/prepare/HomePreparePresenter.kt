package com.hm.iou.lawyer.business.lawyer.home.prepare

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
class HomePreparePresenter(context: Context, view: HomePrepareContract.View) :
    HMBasePresenter<HomePrepareContract.View>(context, view),
    HomePrepareContract.Presenter {


    override fun init() {
        launch {
            try {
                mView.showLoadingView()

                val result = handleResponse(LawyerApi.getLawyerHomeStatus())
                result?.let {
                    when (result.firstAuthState) {
                        0 -> {
                            NavigationHelper.toAuthentication(mContext)
                        }
                        2 -> {
                            mContext.startActivity<AuthenProgressActivity>(
                                AuthenProgressActivity.EXTRA_KEY_IF_AUTHENTICATION_FAILED to false
                            )
                        }
                        3 -> {
                            val lawFirmState =
                                result.updateLawFirmState ?: UpdateLawFirmStatusType.SUCCESS.status
                            val yearCheckState =
                                result.updateYearCheckState
                                    ?: UpdateYeaCheckStatusType.SUCCESS.status
                            mContext.startActivity<HomeActivity>(
                                HomeActivity.EXTRA_KEY_UPDATE_LAW_FIRM_STATE to lawFirmState,
                                HomeActivity.EXTRA_KEY_UPDATE_YEAR_CHECK_STATE to yearCheckState
                            )
                        }
                        4 -> {
                            val authFailMsg = result.authFailMsg ?: "很抱歉，您的认证审核未通过"
                            mContext.startActivity<AuthenProgressActivity>(
                                AuthenProgressActivity.EXTRA_KEY_IF_AUTHENTICATION_FAILED to true,
                                AuthenProgressActivity.EXTRA_KEY_IF_AUTHENTICATION_FAILED_DESC to authFailMsg
                            )
                        }
                    }
                }
                mView.dismissLoadingView()
                mView.closeCurrPage()
            } catch (e: Exception) {
                e.printStackTrace()
                handleException(e)
                mView.dismissLoadingView()
                mView.closeCurrPage()
            }
        }
    }


}
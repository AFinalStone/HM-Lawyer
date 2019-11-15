package com.hm.iou.lawyer.business.index

import android.content.Context
import android.view.View
import com.hm.iou.base.adver.AdApi
import com.hm.iou.base.adver.AdService
import com.hm.iou.base.comm.CommApi
import com.hm.iou.base.mvp.HMBaseFragmentPresenter
import com.hm.iou.base.utils.RxUtil
import com.hm.iou.lawyer.LawyerAppLike
import com.hm.iou.network.HttpReqManager
import com.hm.iou.sharedata.UserManager
import com.hm.iou.sharedata.event.CommBizEvent
import com.hm.iou.sharedata.model.BaseResponse
import com.hm.iou.sharedata.model.PersonalCenterInfo
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Created by hjy on 2019/11/11
 *
 * 律师 tab 首页
 */
class LawerTabIndexPresenter(context: Context, view: LawerTabIndexContract.View) :
    HMBaseFragmentPresenter<LawerTabIndexContract.View>(context, view),
    LawerTabIndexContract.Presenter {

    companion object {
        const val BANNER_AD_POSITION = "banner004"
    }

    override fun onViewCreated() {
        super.onViewCreated()
        EventBus.getDefault().register(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.getDefault().unregister(this)
    }

    override fun init() {
        updateRedFlagCount()
        getTopBanner()
        judgeIsAuthLawyer()
    }

    override fun updateRedFlagCount() {
        //设置导航栏头部红点
        val redFlagCount = LawyerAppLike.getInstance().getTopHeadRedFlagCount()
        mView.updateRedFlagCount(redFlagCount ?: "")
    }

    override fun getTopBanner() {
        launch {
            try {
                val response = HttpReqManager.getInstance().getService(AdService::class.java)
                    .getAdvertiseByCoroutine(BANNER_AD_POSITION)
                val result = handleResponse(response)
                mView.showBanner(result)
            } catch (e: Exception) {
                handleException(e, showBusinessError = false, showCommError = false)
            }
        }
    }

    override fun judgeIsAuthLawyer() {
        val isAuth = UserManager.getInstance(mContext).isAuthLawyer()
        if (isAuth) {
            mView.showTopLawyerWorkbench(View.VISIBLE)
        } else {
            launch {
                try {
                    val isLawyer = isAuthLawyer()
                    mView.showTopLawyerWorkbench(if (isLawyer) View.VISIBLE else View.GONE)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private suspend fun isAuthLawyer(): Boolean = suspendCancellableCoroutine { continuation ->
        val disposable = CommApi.getPersonalCenter()
            .subscribe({resp ->
                val data = handleResponse(resp)
                data?.let {
                    UserManager.getInstance(mContext).setAuthLawyer(data.isLawyer)
                    continuation.resume(data.isLawyer)
                    return@subscribe
                }
                continuation.resume(false)
            }, {
                continuation.resumeWithException(it)
            })
        continuation.invokeOnCancellation {
            disposable.dispose()
        }
    }

    /**
     * 顶部头像未读消息数量
     *
     * @param commBizEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvenRedFlagCount(commBizEvent: CommBizEvent) {
        if ("userInfo_homeLeftMenu_redFlagCount" == commBizEvent.key) {
            val redFlagCount = commBizEvent.content
            mView.updateRedFlagCount(redFlagCount ?: "")
        }
    }

}
package com.hm.iou.lawyer.business.lawyer.home

import android.content.Context
import com.hm.iou.base.mvp.HMBasePresenter
import com.hm.iou.lawyer.api.LawyerApi
import com.hm.iou.lawyer.event.UpdateAuthenInfoEvent
import com.hm.iou.network.exception.ApiException
import com.hm.iou.sharedata.UserManager
import com.hm.iou.sharedata.event.BindBankSuccessEvent
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @author : 借条管家-shilei
 * @version : 0.0.1
 * @Date : 2019-11-08 15:19
 * @E-Mail : afinalstone@foxmail.com
 */
class HomePresenter(context: Context, view: HomeContract.View) :
    HMBasePresenter<HomeContract.View>(context, view),
    HomeContract.Presenter {


    private var mNeedRefresh = false

    init {
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun init() {
        launch {
            try {
                mView.showInitView()
                val userId = UserManager.getInstance(mContext).userId
                val result = handleResponse(LawyerApi.getLawyerHomeDetail(userId))
                mView.hideInitView()
                if (result == null) {
                    mView.toastErrorMessage("发生异常")
                    mView.closeCurrPage()
                } else {
                    mView.showDetail(result)
                }
            } catch (e: Exception) {
                if (e is ApiException) {
                    val msg = e.message ?: "初始化失败"
                    mView.showInitFailed(msg)
                } else {
                    handleException(e)
                    mView.closeCurrPage()
                }
            }
        }
    }

    override fun onResume() {
        if (mNeedRefresh) {
            init()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventUpdateAuthenInfoEventSuccess(event: UpdateAuthenInfoEvent) {
        mNeedRefresh = true
    }

}
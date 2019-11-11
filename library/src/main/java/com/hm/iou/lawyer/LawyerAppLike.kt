package com.hm.iou.lawyer

import android.content.Context
import com.hm.iou.sharedata.event.CommBizEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Created by hjy on 2019/11/11
 *
 * 律师模块 Application
 */
class LawyerAppLike {

    companion object {

        private lateinit var INSTANCE: LawyerAppLike

        fun getInstance(): LawyerAppLike {
            return INSTANCE
        }

    }

    private var mContext: Context? = null
    private var mTopHeadRedFlagCount: String? = null    //导航栏上红点标记数字

    fun onCreate(context: Context) {
        INSTANCE = this
        mContext = context
        EventBus.getDefault().register(this)
    }

    fun getTopHeadRedFlagCount(): String? {
        return mTopHeadRedFlagCount
    }

    /**
     * 成功获取个人中心红色标记数量
     *
     * @param commBizEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvenBusUserInfoHomeLeftMenuRedFlagCount(commBizEvent: CommBizEvent) {
        if ("userInfo_homeLeftMenu_redFlagCount" == commBizEvent.key) {
            mTopHeadRedFlagCount = commBizEvent.content
        }
    }

}
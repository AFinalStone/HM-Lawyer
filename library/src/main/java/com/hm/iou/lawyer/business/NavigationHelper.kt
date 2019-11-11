package com.hm.iou.lawyer.business

import android.app.Activity
import android.content.Intent
import com.hm.iou.lawyer.business.lawyer.withdraw.WithDrawActivity

/**
 * @author : 借条管家-shilei
 * @version : 0.0.1
 * @Date : 2019-11-11 11:45
 * @E-Mail : afinalstone@foxmail.com
 */
object NavigationHelper {

    /**
     * 我的钱包
     */
    fun toMyWallet(activity: Activity) {
        val intent = Intent(activity, WithDrawActivity::class.java)
        activity.startActivity(intent)
    }

    /**
     * 进入我的银行卡详情页面
     */
    fun toBankCardDetail(activity: Activity) {
    }
}
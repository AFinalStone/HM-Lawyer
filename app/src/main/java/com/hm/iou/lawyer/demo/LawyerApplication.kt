package com.hm.iou.lawyer.demo

import android.app.Application
import com.hm.iou.base.BaseBizAppLike
import com.hm.iou.lawyer.LawyerAppLike
import com.hm.iou.logger.Logger
import com.hm.iou.router.Router

class LawyerApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Router.init(this)
        Logger.init(this, true)

        val baseBiz = BaseBizAppLike()
        baseBiz.isDebug = true
        baseBiz.onCreate(this)
        baseBiz.initServer(
            "http://dev.54jietiao.com", "http://dev.54jietiao.com",
            "http://dev.54jietiao.com"
        )
        val lawyerApp = LawyerAppLike()
        lawyerApp.onCreate(this)

    }

}
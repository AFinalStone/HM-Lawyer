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
//        baseBiz.initServer(
//            "http://dev.54jietiao.com", "http://dev.54jietiao.com",
//            "http://dev.54jietiao.com"
//        )
//        baseBiz.initServer(
//            "http://re.54jietiao.com", "http://re.54jietiao.com",
//            "http://re.54jietiao.com"
//        )
//        baseBiz.initServer(
//            "http://branch.54jietiao.com", "http://branch.54jietiao.com",
//            "http://branch.54jietiao.com"
//        )
        baseBiz.initServer(
            "http://192.168.1.107:3000", "http://192.168.1.107:3000",
            "http://192.168.1.107:3000"
        )
//        baseBiz.initServer(
//            "https://api.54jietiao.com", "http://upload.54jietiao.com",
//            "http://h5.54jietiao.com"
//        )
        val lawyerApp = LawyerAppLike()
        lawyerApp.onCreate(this)

    }

}
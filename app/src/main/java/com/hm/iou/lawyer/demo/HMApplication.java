package com.hm.iou.lawyer.demo;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.hm.iou.base.BaseBizAppLike;
import com.hm.iou.logger.Logger;
import com.hm.iou.router.Router;
import com.hm.iou.socialshare.SocialShareAppLike;

/**
 * Created by hjy on 18/5/11.<br>
 */

public class HMApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Router.init(this);

        Logger.init(this, true);
        BaseBizAppLike appLike = new BaseBizAppLike();
        appLike.onCreate(this);
//        appLike.initServer("http://192.168.1.107:3000", "http://192.168.1.107:3000",
//                "http://192.168.1.107:3000");
        appLike.initServer("http://branch.54jietiao.com", "http://dev.54jietiao.com",
                "http://dev.54jietiao.com");
        appLike.setDebug(BuildConfig.DEBUG);

        SocialShareAppLike shareAppLike = new SocialShareAppLike();
        shareAppLike.onCreate(this);

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
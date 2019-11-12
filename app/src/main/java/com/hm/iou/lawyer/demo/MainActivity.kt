package com.hm.iou.lawyer.demo

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.hm.iou.lawyer.business.lawyer.workbench.WorkBenchActivity
import com.hm.iou.network.HttpReqManager
import com.hm.iou.sharedata.UserManager
import com.hm.iou.sharedata.model.BaseResponse
import com.hm.iou.sharedata.model.UserInfo
import com.hm.iou.tools.ToastUtil
import com.sina.weibo.sdk.utils.MD5
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_login.setOnClickListener {
            login()
        }
        btn_lawyer.setOnClickListener {
            startActivity(Intent(this@MainActivity, WorkBenchActivity::class.java))
        }

        btn_lawyer_index.setOnClickListener {
            //            startActivity<TabActivity>()
        }

    }

    @SuppressLint("CheckResult")
    private fun login() {
        val pwd = MD5.hexdigest("123456".toByteArray())
        val reqBean = MobileLoginReqBean()
        //        reqBean.setMobile("13186975702");
        //        reqBean.setMobile("15967132742");
        reqBean.mobile = "15267163669"
        //        reqBean.setMobile("18337150117");
        //        reqBean.setMobile("17681832816");

        reqBean.queryPswd = pwd
        HttpReqManager.getInstance().getService<LoginService>(LoginService::class.java!!)
            .mobileLogin(reqBean)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(Consumer<BaseResponse<UserInfo>> { userInfoBaseResponse ->
                if (userInfoBaseResponse.errorCode != 0) {
                    ToastUtil.showMessage(this@MainActivity, userInfoBaseResponse.message)
                    return@Consumer
                }
                ToastUtil.showMessage(this@MainActivity, "登录成功")
                val userInfo = userInfoBaseResponse.data
                UserManager.getInstance(this@MainActivity).updateOrSaveUserInfo(userInfo)
                HttpReqManager.getInstance().setUserId(userInfo.userId)
                HttpReqManager.getInstance().setToken(userInfo.token)
            }, Consumer<Throwable> { t -> t.printStackTrace() })
    }

}

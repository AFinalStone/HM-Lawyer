package com.hm.iou.lawyer.business.lawyer.home.authen

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.hm.iou.base.mvp.BaseContract
import com.hm.iou.base.mvp.HMBaseActivity
import com.hm.iou.base.mvp.HMBasePresenter
import com.hm.iou.lawyer.R
import com.hm.iou.lawyer.business.NavigationHelper
import com.hm.iou.lawyer.event.UpdateAuthenInfoEvent
import com.hm.iou.tools.kt.*
import kotlinx.android.synthetic.main.lawyer_activity_lawyer_authentication_progress.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @author : 借条管家-shilei
 * @version : 0.0.1
 * @Date : 2019-11-12 13:50
 * @E-Mail : afinalstone@foxmail.com
 */
class AuthenProgressActivity : HMBaseActivity<HMBasePresenter<BaseContract.BaseView>>() {


    companion object {
        const val EXTRA_KEY_IF_AUTHENTICATION_FAILED = "if_authentication_failed"
        const val EXTRA_KEY_IF_AUTHENTICATION_FAILED_DESC = "if_authentication_failed_desc"
        private const val REQ_CODE_RESTART_AUTHEN = 100
    }

    /**
     * 认证审核是否失败
     */
    private var mIfAuthenticationFailed: Boolean? by extraDelegate(
        EXTRA_KEY_IF_AUTHENTICATION_FAILED,
        null
    )

    private var mIfAuthenticationFailedDesc: String? by extraDelegate(
        EXTRA_KEY_IF_AUTHENTICATION_FAILED_DESC,
        null
    )


    override fun getLayoutId(): Int = R.layout.lawyer_activity_lawyer_authentication_progress

    override fun initEventAndData(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            mIfAuthenticationFailed =
                savedInstanceState.getValue(EXTRA_KEY_IF_AUTHENTICATION_FAILED)
        }
        bottom_bar.setOnTitleClickListener {
            startActivityForResult<AuthenticationActivity>(REQ_CODE_RESTART_AUTHEN)
        }
        if (false == mIfAuthenticationFailed) {
            tv_status.text = "认证审核中"
            tv_desc.text = "您的认证申请正在审核中，我们将尽快为您审核"
            iv_status.setImageResource(R.mipmap.uikit_data_search)
            bottom_bar.setTitleVisible(false)
        } else {
            tv_status.text = "认证审核失败"
            tv_desc.text = mIfAuthenticationFailedDesc ?: "很抱歉，您的认证审核未通过"
            iv_status.setImageResource(R.mipmap.uikit_data_fail_cry)
            bottom_bar.setTitleVisible(true)
        }
    }

    override fun initPresenter(): HMBasePresenter<BaseContract.BaseView> =
        HMBasePresenter(this, this)


    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putValue(EXTRA_KEY_IF_AUTHENTICATION_FAILED, mIfAuthenticationFailed)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (REQ_CODE_RESTART_AUTHEN == requestCode && resultCode == Activity.RESULT_OK) {
            finish()
        }
    }
}
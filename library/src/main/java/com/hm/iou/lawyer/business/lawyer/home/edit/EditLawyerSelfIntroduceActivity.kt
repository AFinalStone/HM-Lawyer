package com.hm.iou.lawyer.business.lawyer.home.edit

import android.os.Bundle
import com.hm.iou.base.comm.HMTextChangeListener
import com.hm.iou.base.mvp.HMBaseActivity
import com.hm.iou.lawyer.R
import com.hm.iou.tools.kt.extraDelegate
import kotlinx.android.synthetic.main.lawyer_activity_edit_lawyer_self_introduction.*

/**
 * 律师介绍
 */
class EditLawyerSelfIntroduceActivity : HMBaseActivity<EditLawyerSelfIntroducePresenter>(),
    EditLawyerSelfIntroduceContract.View {

    companion object {
        const val EXTRA_KEY_SELF_INTRODUCE = "self_introduce"
    }

    private var mSelfIntroduce: String? by extraDelegate(EXTRA_KEY_SELF_INTRODUCE, null)


    override fun getLayoutId(): Int = R.layout.lawyer_activity_edit_lawyer_self_introduction

    override fun initPresenter(): EditLawyerSelfIntroducePresenter =
        EditLawyerSelfIntroducePresenter(this, this)


    override fun initEventAndData(savedInstanceState: Bundle?) {
        et_self_introduction.addTextChangedListener(object : HMTextChangeListener() {
            override fun onTextChanged(
                charSequence: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                mSelfIntroduce = charSequence.toString()
                val length = (mSelfIntroduce ?: "").length
                tv_self_introduction_word_count.text =
                    String.format("%d/200", length)
                if (length < 30) {
                    bottom_bar.setTitleBackgournd(R.drawable.uikit_selector_btn_minor_small)
                    bottom_bar.setTitleTextColor(R.color.uikit_text_auxiliary)
                    return
                }
                bottom_bar.setTitleBackgournd(R.drawable.uikit_shape_common_btn_normal)
                bottom_bar.setTitleTextColor(R.color.uikit_text_main_content)
            }
        })
        //提交认证
        bottom_bar.setOnTitleClickListener {
            val length = (mSelfIntroduce ?: "").length
            if (length < 30) {
                toastErrorMessage("律师介绍必须在30-200个字以内")
                return@setOnTitleClickListener
            }
            mSelfIntroduce?.let {
                mPresenter.updateLawyerAuthenticationInfo(it)
            }
        }
        et_self_introduction.setText(mSelfIntroduce ?: "")
        et_self_introduction.setSelection(et_self_introduction.length())
        et_self_introduction.requestFocus()
        showSoftKeyboard()
    }

}
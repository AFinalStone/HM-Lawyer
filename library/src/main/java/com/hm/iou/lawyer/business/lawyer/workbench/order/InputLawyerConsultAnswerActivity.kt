package com.hm.iou.lawyer.business.lawyer.workbench.order

import android.os.Bundle
import com.hm.iou.base.comm.HMTextChangeListener
import com.hm.iou.base.mvp.HMBaseActivity
import com.hm.iou.lawyer.R
import com.hm.iou.tools.kt.extraDelegate
import kotlinx.android.synthetic.main.lawyer_activity_edit_lawyer_self_introduction.*

/**
 * 咨询解答
 */
class InputLawyerConsultAnswerActivity : HMBaseActivity<InputLawyerConsultAnswerPresenter>(),
    InputLawyerConsultAnswerContract.View {

    companion object {
        const val EXTRA_KEY_SELF_INTRODUCE = "answer"
    }

    private var mAnswer: String? by extraDelegate(EXTRA_KEY_SELF_INTRODUCE, null)

    override fun getLayoutId(): Int = R.layout.lawyer_activity_input_lawyer_answer

    override fun initPresenter(): InputLawyerConsultAnswerPresenter =
        InputLawyerConsultAnswerPresenter(this, this)


    override fun initEventAndData(savedInstanceState: Bundle?) {
        et_self_introduction.addTextChangedListener(object : HMTextChangeListener() {
            override fun onTextChanged(
                charSequence: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                mAnswer = charSequence.toString()
                val length = (mAnswer ?: "").length
                tv_self_introduction_word_count.text =
                    String.format("%d/500", length)
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
            val length = (mAnswer ?: "").length
            if (length < 30) {
                toastErrorMessage("律师介绍必须在30-500个字以内")
                return@setOnTitleClickListener
            }
            mAnswer?.let {
                mPresenter.finishAnswer(it)
            }
        }
        et_self_introduction.setText(mAnswer ?: "")
        et_self_introduction.setSelection(et_self_introduction.length())
        et_self_introduction.requestFocus()
        showSoftKeyboard()
    }

}
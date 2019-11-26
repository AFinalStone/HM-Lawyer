package com.hm.iou.lawyer.business.lawyer.workbench.order

import android.os.Bundle
import com.hm.iou.base.comm.HMTextChangeListener
import com.hm.iou.base.mvp.HMBaseActivity
import com.hm.iou.lawyer.R
import com.hm.iou.lawyer.event.AnswerListChangedEvent
import com.hm.iou.lawyer.event.LawyerOrderStatusChangedEvent
import com.hm.iou.tools.kt.extraDelegate
import com.hm.iou.tools.kt.getValue
import com.hm.iou.tools.kt.putValue
import kotlinx.android.synthetic.main.lawyer_activity_input_lawyer_answer.*
import org.greenrobot.eventbus.EventBus

/**
 * 咨询解答
 */
class InputLawyerConsultAnswerActivity : HMBaseActivity<InputLawyerConsultAnswerPresenter>(),
    InputLawyerConsultAnswerContract.View {


    companion object {
        const val EXTRA_KEY_ORDER_ID = "order_id"
        const val EXTRA_KEY_MIN_LENGTH = "min_length"
    }

    /**
     * 订单编号
     */
    private var mOrderId: String? by extraDelegate(
        EXTRA_KEY_ORDER_ID,
        null
    )

    /**
     * 是否是第一次
     */
    private var mMinAnswerLength: Int? by extraDelegate(
        EXTRA_KEY_MIN_LENGTH,
        2
    )

    private var mAnswer: String? = null

    override fun getLayoutId(): Int = R.layout.lawyer_activity_input_lawyer_answer

    override fun initPresenter(): InputLawyerConsultAnswerPresenter =
        InputLawyerConsultAnswerPresenter(this, this)


    override fun initEventAndData(bundle: Bundle?) {
        if (bundle != null) {
            mOrderId = bundle.getValue(EXTRA_KEY_ORDER_ID)
            mMinAnswerLength = bundle.getValue(EXTRA_KEY_MIN_LENGTH)
        }
        et_answer.addTextChangedListener(object : HMTextChangeListener() {
            override fun onTextChanged(
                charSequence: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                mAnswer = charSequence.toString()
                val length = (mAnswer ?: "").length
                tv_answer_word_count.text =
                    String.format("%d/200", length)
                if (length < mMinAnswerLength ?: 2) {
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
            if (length < mMinAnswerLength ?: 2) {
                toastErrorMessage("律师介绍必须在%S-200个字以内".format(mMinAnswerLength ?: 2))
                return@setOnTitleClickListener
            }
            mOrderId?.let {
                mPresenter.finishAnswer(it, mAnswer ?: "")
            }
        }
        et_answer.setText(mAnswer ?: "")
        et_answer.setSelection(et_answer.length())
        et_answer.requestFocus()
        showSoftKeyboard()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putValue(EXTRA_KEY_ORDER_ID, mOrderId)
        outState?.putValue(EXTRA_KEY_MIN_LENGTH, mMinAnswerLength)
    }

    override fun sendMsg() {
        if (mMinAnswerLength ?: 2 > 2) {
            EventBus.getDefault().post(LawyerOrderStatusChangedEvent())
        } else {
            EventBus.getDefault().post(AnswerListChangedEvent())
        }
    }

}
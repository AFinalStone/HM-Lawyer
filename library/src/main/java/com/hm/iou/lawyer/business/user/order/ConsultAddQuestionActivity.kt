package com.hm.iou.lawyer.business.user.order

import android.os.Bundle
import com.hm.iou.base.comm.HMTextChangeListener
import com.hm.iou.base.mvp.BaseContract
import com.hm.iou.base.mvp.HMBaseActivity
import com.hm.iou.base.mvp.HMBasePresenter
import com.hm.iou.lawyer.R
import com.hm.iou.tools.kt.extraDelegate
import kotlinx.android.synthetic.main.lawyer_activity_create_lawyer_consult.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Created by hjy on 2019-11-25
 *
 * 问题补充页面
 */
class ConsultAddQuestionActivity : HMBaseActivity<HMBasePresenter<BaseContract.BaseView>>() {

    companion object {
        const val EXTRA_KEY_ORDER_ID = "order_id"
    }

    private var mOrderId: String? by extraDelegate(EXTRA_KEY_ORDER_ID, null)

    override fun initPresenter(): HMBasePresenter<BaseContract.BaseView> =
        HMBasePresenter(this, this)

    override fun getLayoutId(): Int = R.layout.lawyer_activity_consult_add_question

    override fun initEventAndData(savedInstanceState: Bundle?) {
        savedInstanceState?.let {
            mOrderId = it.getString(mOrderId)
        }

        et_consult_desc.addTextChangedListener(object : HMTextChangeListener() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tv_consult_char_count.text = "${et_consult_desc.text.trim().length}/500"
                checkInputValues()
            }
        })

        bottom_bar.setOnTitleClickListener {
            val question = et_consult_desc.text.trim().toString()
            submitQuestion(question)
        }

        et_consult_desc.requestFocus()

    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putString(EXTRA_KEY_ORDER_ID, mOrderId)
    }

    private fun checkInputValues() {
        if (et_consult_desc.text.trim().isEmpty()) {
            bottom_bar.setTitleBtnBackground(R.drawable.uikit_selector_btn_minor_small)
            bottom_bar.setTitleBtnTextColor(resources.getColor(R.color.uikit_text_auxiliary))
            return
        }
        bottom_bar.setTitleBtnBackground(R.drawable.uikit_selector_btn_main_small)
        bottom_bar.setTitleBtnTextColor(resources.getColor(R.color.uikit_text_main_content))
    }

    /**
     * 补充问题
     */
    private fun submitQuestion(question: String) {
        if (question.isEmpty()) {
            toastMessage("请输入您要补充的问题内容")
            return
        }

        showLoadingView()
        launch {
            try {
                delay(1000)
                dismissLoadingView()
                closeCurrPage()
            } catch (e: Exception) {
                dismissLoadingView()
                mPresenter.handleException(e)
            }
        }
    }


}
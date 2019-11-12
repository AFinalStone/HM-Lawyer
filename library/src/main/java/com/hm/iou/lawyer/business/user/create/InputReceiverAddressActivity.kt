package com.hm.iou.lawyer.business.user.create

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.hm.iou.base.comm.HMTextChangeListener
import com.hm.iou.base.mvp.BaseContract
import com.hm.iou.base.mvp.HMBaseActivity
import com.hm.iou.base.mvp.HMBasePresenter
import com.hm.iou.lawyer.R
import com.hm.iou.lawyer.bean.LetterReceiverBean
import com.hm.iou.tools.kt.clickWithDuration
import com.hm.iou.uikit.datepicker.CityPickerDialog
import kotlinx.android.synthetic.main.lawyer_activity_letter_address.*
import kotlinx.android.synthetic.main.lawyer_activity_letter_address.et_letter_name

/**
 * Created by hjy on 2019/11/12
 *
 * 输入收件人地址页面
 */
class InputReceiverAddressActivity : HMBaseActivity<HMBasePresenter<BaseContract.BaseView>>() {

    private var mCityPicker: CityPickerDialog? = null

    override fun getLayoutId(): Int = R.layout.lawyer_activity_letter_address

    override fun initPresenter(): HMBasePresenter<BaseContract.BaseView> =
        HMBasePresenter(this, this)

    override fun initEventAndData(savedInstanceState: Bundle?) {
        initViews()
    }

    private fun initViews() {
        ll_letter_city.clickWithDuration {
            if (mCityPicker == null) {
                mCityPicker = CityPickerDialog.Builder(this)
                    .setTitle("城市选择")
                    .setOnCityConfirmListener { s, s2, s3 ->
                        tv_letter_city.text = "$s$s2$s3"
                    }
                    .create()
            }
            mCityPicker!!.show()
        }

        et_letter_name.addTextChangedListener(object : HMTextChangeListener() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkInputValues()
            }
        })
        et_letter_idno.addTextChangedListener(object : HMTextChangeListener() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkInputValues()
            }
        })
        et_letter_mobile.addTextChangedListener(object : HMTextChangeListener() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkInputValues()
            }
        })
        tv_letter_city.addTextChangedListener(object : HMTextChangeListener() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkInputValues()
            }
        })
        et_letter_addr.addTextChangedListener(object : HMTextChangeListener() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkInputValues()
            }
        })

        val data = intent.getParcelableExtra<LetterReceiverBean>("receiver")
        data?.let {
            et_letter_name.setText(data.receiverName)
            et_letter_name.setSelection(et_letter_name.length())
            et_letter_idno.setText(data.receiverIdCardNum)
            et_letter_mobile.setText(data.receiverMobile)
            tv_letter_city.text = data.receiverCityDetail
            et_letter_addr.setText(data.receiverDetailAddress)
        }

        bottomBar.setOnTitleClickListener {
            val data = LetterReceiverBean()
            data.receiverName = et_letter_name.text.trim().toString()
            data.receiverIdCardNum = et_letter_idno.text.trim().toString()
            data.receiverMobile = et_letter_mobile.text.trim().toString()
            data.receiverCityDetail = tv_letter_city.text.trim().toString()
            data.receiverDetailAddress = et_letter_addr.text.trim().toString()
            val intent = Intent()
            intent.putExtra("receiver", data)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

    }

    private fun checkInputValues() {
        if (et_letter_name.text.trim().isEmpty() ||
            et_letter_idno.text.trim().isEmpty() ||
            et_letter_mobile.text.trim().isEmpty() ||
            tv_letter_city.text.trim().isEmpty() ||
            et_letter_addr.text.trim().isEmpty()
        ) {
            bottomBar.isEnabled = false
            return
        }
        bottomBar.isEnabled = true
    }

}
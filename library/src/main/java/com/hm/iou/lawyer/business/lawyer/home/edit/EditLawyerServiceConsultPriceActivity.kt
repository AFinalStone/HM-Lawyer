package com.hm.iou.lawyer.business.lawyer.home.edit

import android.os.Bundle
import com.hm.iou.base.comm.HMTextChangeListener
import com.hm.iou.base.mvp.HMBaseActivity
import com.hm.iou.lawyer.R
import com.hm.iou.tools.kt.extraDelegate
import com.hm.iou.tools.kt.getValue
import com.hm.iou.tools.kt.putValue
import kotlinx.android.synthetic.main.lawyer_activity_edit_lawyer_consult_service_price.*

/**
 * 服务费用EditLawyerServicePricePresenter
 */
class EditLawyerServiceConsultPriceActivity :
    HMBaseActivity<EditLawyerServicePricePresenter>(),
    EditLawyerServicePriceContract.View {

    companion object {
        const val EXTRA_KEY_SERVICE_PRICE = "service_money"
        const val EXTRA_KEY_SERVICE_ID = "service_id"
        private const val MIN_NUM = 10
        private const val MAX_NUM = 1000
    }

    private var mServicePrice: Int? by extraDelegate(EXTRA_KEY_SERVICE_PRICE, null)
    private var mServiceId: Int? by extraDelegate(EXTRA_KEY_SERVICE_ID, null)

    override fun getLayoutId(): Int = R.layout.lawyer_activity_edit_lawyer_consult_service_price

    override fun initPresenter(): EditLawyerServicePricePresenter =
        EditLawyerServicePricePresenter(this, this)

    override fun initEventAndData(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            mServicePrice = savedInstanceState.getValue(EXTRA_KEY_SERVICE_PRICE)
            mServiceId = savedInstanceState.getValue(EXTRA_KEY_SERVICE_ID)
        }
        et_service_money.addTextChangedListener(object : HMTextChangeListener() {
            override fun onTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val money = try {
                    Integer.parseInt(charSequence.toString())
                } catch (e: Exception) {
                    0
                }
                mServicePrice = money
                if (money > MAX_NUM) {
                    et_service_money.setText(MAX_NUM.toString())
                    et_service_money.setSelection(et_service_money.length())
                    return
                }
                if (money in MIN_NUM..MAX_NUM) {
                    bottom_bar.setTitleBackgournd(R.drawable.uikit_shape_common_btn_normal)
                    bottom_bar.setTitleTextColor(R.color.uikit_text_main_content)
                } else {
                    bottom_bar.setTitleBackgournd(R.drawable.uikit_selector_btn_minor_small)
                    bottom_bar.setTitleTextColor(R.color.uikit_text_auxiliary)
                }
            }
        })
        bottom_bar.setOnTitleClickListener {
            val money = mServicePrice ?: 0
            if (money in MIN_NUM..MAX_NUM) {
                val servicePrice = mServicePrice
                val serviceId = mServiceId
                if (servicePrice != null && serviceId != null) {
                    mPresenter.updateLawyerServicePrice(servicePrice, serviceId)
                }
            } else {
                toastErrorMessage("律师咨询费必须在%S到%S之间".format(MIN_NUM, MAX_NUM))
            }
        }
        et_service_money.setText((mServicePrice ?: "").toString())
        et_service_money.setSelection(et_service_money.length())
        et_service_money.requestFocus()
        showSoftKeyboard()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putValue(EXTRA_KEY_SERVICE_PRICE, mServicePrice)
        outState?.putValue(EXTRA_KEY_SERVICE_ID, mServiceId)
    }

}
package com.hm.iou.lawyer.business.lawyer.withdraw

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import com.hm.iou.base.comm.HMTextChangeListener
import com.hm.iou.base.mvp.HMBaseActivity
import com.hm.iou.lawyer.R
import com.hm.iou.router.Router
import com.hm.iou.uikit.dialog.HMAlertDialog
import kotlinx.android.synthetic.main.lawyer_activity_lawyer_withdraw.*

/**
 * 提现
 */
class WithDrawActivity : HMBaseActivity<WithDrawPresenter>(),
    WithDrawContract.View {

    companion object {
        private const val MIN_MONEY = 100
    }

    private var mWithDrawTotalMoney: Float = 0f
    private var mRemainderMoney: String? = null


    override fun getLayoutId(): Int = R.layout.lawyer_activity_lawyer_withdraw

    override fun initPresenter(): WithDrawPresenter =
        WithDrawPresenter(this, this)

    override fun initEventAndData(savedInstanceState: Bundle?) {

        iv_to_bank_detail.setOnClickListener {
            Router.getInstance()
                .buildWithUrl("hmiou://m.54jietiao.com/pay/user_bind_bank?source=lawyer")
                .navigation(mContext)
        }
        tv_withdraw_all_money.setOnClickListener {
            et_withdraw_money.setText(mRemainderMoney ?: "")
            et_withdraw_money.setSelection(et_withdraw_money.length())
        }
        tv_withdraw_record.setOnClickListener {

        }
        tv_common_question.setOnClickListener {

        }
        bottom_bar.setOnTitleClickListener {
            if (tv_bank_card.length() == 0) {
                toastErrorMessage("请绑定银行卡")
                return@setOnTitleClickListener
            }
            if (mWithDrawTotalMoney < MIN_MONEY) {
                toastErrorMessage("提现金额最低为100元")
                return@setOnTitleClickListener
            }
            val remainderMoney = try {
                mRemainderMoney?.toFloat() ?: 0f
            } catch (e: Exception) {
                0f
            }
            if (mWithDrawTotalMoney > remainderMoney) {
                toastErrorMessage("提现金额不能大于钱包余额")
                return@setOnTitleClickListener
            }
            mPresenter.calcWithDrawMoney(mWithDrawTotalMoney)

        }
        et_withdraw_money.addTextChangedListener(object : HMTextChangeListener() {
            override fun onTextChanged(
                charSequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                mWithDrawTotalMoney = 0f
                try {
                    mWithDrawTotalMoney = charSequence.toString().toFloat()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                checkValue()
            }
        })

        mPresenter.init()
    }

    override fun onResume() {
        super.onResume()
        mPresenter.onResume()
    }


    override fun showRemainderMoney(money: String?) {
        tv_remainder_money.text = "钱包余额¥%S".format(money ?: "")
        mRemainderMoney = money
    }

    override fun showBankcard(bankCard: String?, bankName: String?) {
        tv_bank_card.text = bankCard ?: ""
        tv_bank_name.text = bankName ?: ""
    }

    override fun showWithDrawDialog(
        withDrawRealMoney: String?,
        withDrawTotalMoney: String?,
        serviceMoney: String?,
        serviceRate: String?
    ) {
        val view = LayoutInflater.from(mContext)
            .inflate(R.layout.lawyer_dialog_lawyer_withdraw_query, null, false)
        val alert = HMAlertDialog.Builder(mContext)
            .setCustomView(view)
            .create()
        view.findViewById<TextView>(R.id.tv_withdraw_real_money).text = withDrawRealMoney
        view.findViewById<TextView>(R.id.tv_withdraw_total_money).text = withDrawTotalMoney
        view.findViewById<TextView>(R.id.tv_service_money).text = serviceMoney
        view.findViewById<TextView>(R.id.tv_service_rate).text = serviceRate
        view.findViewById<Button>(R.id.btn_ok).setOnClickListener {
            mPresenter.withDrawMoney(mWithDrawTotalMoney)
            alert.dismiss()
        }
        alert.show()
    }

    private fun checkValue() {
        if (tv_bank_card.length() == 0) {
            bottom_bar.setTitleBackgournd(R.drawable.uikit_selector_btn_minor_small)
            bottom_bar.setTitleTextColor(R.color.uikit_text_auxiliary)
            return
        }
        if (mWithDrawTotalMoney < MIN_MONEY) {
            bottom_bar.setTitleBackgournd(R.drawable.uikit_selector_btn_minor_small)
            bottom_bar.setTitleTextColor(R.color.uikit_text_auxiliary)
            return
        }

        bottom_bar.setTitleBackgournd(R.drawable.uikit_shape_common_btn_normal)
        bottom_bar.setTitleTextColor(R.color.uikit_text_main_content)
    }

}
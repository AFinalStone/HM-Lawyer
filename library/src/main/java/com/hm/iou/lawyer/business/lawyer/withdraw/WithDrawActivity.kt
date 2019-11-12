package com.hm.iou.lawyer.business.lawyer.withdraw

import android.os.Bundle
import android.view.LayoutInflater
import com.hm.iou.base.comm.HMTextChangeListener
import com.hm.iou.base.mvp.HMBaseActivity
import com.hm.iou.lawyer.R
import com.hm.iou.lawyer.business.NavigationHelper
import com.hm.iou.router.Router
import com.hm.iou.uikit.dialog.HMAlertDialog
import kotlinx.android.synthetic.main.lawyer_activity_lawyer_withdraw.*

/**
 * 提现
 */
class WithDrawActivity : HMBaseActivity<WithDrawPresenter>(),
    WithDrawContract.View {

    override fun getLayoutId(): Int = R.layout.lawyer_activity_lawyer_withdraw


    override fun initPresenter(): WithDrawPresenter =
        WithDrawPresenter(this, this)

    override fun initEventAndData(savedInstanceState: Bundle?) {

        iv_to_bank_detail.setOnClickListener {
            Router.getInstance()
                .buildWithUrl("hmiou://m.54jietiao.com/pay/user_bind_bank?source=usercenter")
                .navigation(mContext)
        }
        tv_withdraw_all_money.setOnClickListener {

        }
        tv_withdraw_record.setOnClickListener {

        }
        tv_common_question.setOnClickListener {

        }
        bottomBar.setOnTitleClickListener {
            showWithDrawQueryDialog()
        }
        et_withdraw_money.addTextChangedListener(object : HMTextChangeListener() {
            override fun onTextChanged(
                charSequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                bottomBar.isEnabled = !charSequence.isNullOrEmpty()
            }
        })
    }


    override fun showRemainderMoney(money: String?) {
        tv_remainder_money.text = money ?: ""
    }

    override fun showBankcard(bankCard: String?, bankName: String?) {
        tv_bank_card.text = bankCard ?: ""
        tv_bank_name.text = bankName ?: ""
    }

    private fun showWithDrawQueryDialog() {
        val viewContent = LayoutInflater.from(mContext)
            .inflate(R.layout.lawyer_dialog_lawyer_withdraw_query, null, false)
        HMAlertDialog.Builder(mContext)
            .setCustomView(viewContent)
            .create()
            .show()
    }

}
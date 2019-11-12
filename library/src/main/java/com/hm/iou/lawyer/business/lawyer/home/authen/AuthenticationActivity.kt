package com.hm.iou.lawyer.business.lawyer.home.authen

import android.app.Dialog
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hm.iou.base.comm.HMTextChangeListener
import com.hm.iou.base.mvp.HMBaseActivity
import com.hm.iou.lawyer.R
import com.hm.iou.router.Router
import com.hm.iou.tools.kt.clickWithDuration
import com.hm.iou.tools.kt.dp2px
import com.hm.iou.uikit.datepicker.TimePickerDialog
import kotlinx.android.synthetic.main.lawyer_activity_lawyer_authentication.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * 律师认证
 */
class AuthenticationActivity : HMBaseActivity<AuthenticationPresenter>(),
    AuthenticationContract.View, View.OnClickListener {

    companion object {
        private const val REQ_CODE_SELECT_HEADER = 100
    }

    private var mHeaderPath: String? = null//头像地址
    private var mAuthenPhotoFrontPath: String? = null//证件正面
    private var mAuthenPhotoBackPath: String? = null//证件反面
    private var mListPhotoPath: List<String>? = null//荣誉照片列表

    private var mDatePicker: Dialog? = null

    override fun getLayoutId(): Int = R.layout.lawyer_activity_lawyer_authentication

    override fun initPresenter(): AuthenticationPresenter =
        AuthenticationPresenter(this, this)

    override fun initEventAndData(savedInstanceState: Bundle?) {
        initView()
    }

    private fun initView() {
        et_certificate_code.addTextChangedListener(object : HMTextChangeListener() {
            override fun onTextChanged(
                charSequence: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                checkValue()
            }
        })
        et_lawyer_firm.addTextChangedListener(object : HMTextChangeListener() {
            override fun onTextChanged(
                charSequence: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                checkValue()
            }
        })
        et_self_introduction.addTextChangedListener(object : HMTextChangeListener() {
            override fun onTextChanged(
                charSequence: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                tv_self_introduction_word_count.text = String.format("%d/200", charSequence.length)
                checkValue()
            }
        })
        tv_certificate_start_time.setOnClickListener(this)
        //个人证件
        iv_header.setOnClickListener(this)
        iv_add_header.setOnClickListener(this)
        //律师正面
        iv_authen_photo_front.setOnClickListener(this)
        iv_add_authen_photo_front.setOnClickListener(this)
        //律师反面
        iv_authen_photo_back.setOnClickListener(this)
        iv_add_authen_photo_back.setOnClickListener(this)
        //荣誉资质照片
        iv_certificate_of_honor.setOnClickListener(this)
        iv_add_certificate_of_honor.setOnClickListener(this)
        //提交认证
        bottom_bar.setOnTitleClickListener {

        }
    }

    override fun onClick(view: View?) {
        when (view) {
            tv_certificate_start_time -> {
                showDatePicker()
            }
            iv_header, iv_add_header -> {
                Router.getInstance()
                    .buildWithUrl("hmiou://m.54jietiao.com/select_pic/index")
                    .withString("enable_select_max_num", "1")
                    .navigation(mContext, REQ_CODE_SELECT_HEADER)
            }
            iv_authen_photo_front, iv_add_authen_photo_front -> {

            }
            iv_authen_photo_back, iv_add_authen_photo_back -> {

            }
            iv_certificate_of_honor, iv_add_certificate_of_honor -> {

            }
        }
    }

    override fun toLawyerHomePage() {

    }

    override fun toAuthenticationPage() {

    }

    override fun toAuthenProgressPage() {

    }

    /**
     * 校验结果
     */
    private fun checkValue() {
        //执业证号
        val certificateCode = et_certificate_code.text
        //执业律所
        val lawyerFirmName = et_lawyer_firm.text
        //持证日期
        val certificateStartTime = tv_certificate_start_time.text
        //个人简介
        val selfIntroduction = et_self_introduction.text
        //个人证件

    }

    private fun showDatePicker() {
        if (mDatePicker == null) {
            val now = System.currentTimeMillis()
            val calendar = Calendar.getInstance()
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

            calendar.timeInMillis = now
            val todayTime = SimpleDateFormat("yyyy年 MM月dd日").format(calendar.time)

            calendar.add(Calendar.YEAR, -30)
            val startTime = sdf.format(calendar.time)
            val endTime = sdf.format(now)

            val tvToday = TextView(mContext)
            tvToday.text = "今日时间    $todayTime"
            tvToday.layoutParams =
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mContext.dp2px(50))
            tvToday.gravity = Gravity.CENTER

            val builder = TimePickerDialog.Builder(this)
                .setTitle("借款到期")
                .setHeaderView(tvToday)
                .setScrollType(TimePickerDialog.SCROLL_TYPE.DAY)
                .setTimeRange(startTime, endTime)
                .setOnPickerConfirmListener { year, month, day, i3, i4 ->
                    var month = month
                    month++
                    val appTime = String.format("%d年%s月%s日", year, patchZero(month), patchZero(day))
                    tv_certificate_start_time.text = appTime
                }
            mDatePicker = builder.create()
        }
        mDatePicker?.show()
    }

    private fun patchZero(m: Int): String {
        return if (m < 10) "0$m" else "" + m
    }

}
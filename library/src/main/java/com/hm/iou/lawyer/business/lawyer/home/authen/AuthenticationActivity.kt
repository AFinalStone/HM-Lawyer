package com.hm.iou.lawyer.business.lawyer.home.authen

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.View.INVISIBLE
import android.view.ViewGroup
import android.widget.TextView
import com.hm.iou.base.ImageGalleryActivity
import com.hm.iou.base.comm.HMTextChangeListener
import com.hm.iou.base.mvp.HMBaseActivity
import com.hm.iou.base.photo.CompressPictureUtil
import com.hm.iou.database.table.IouData
import com.hm.iou.lawyer.R
import com.hm.iou.lawyer.bean.req.LawyerAuthenticationReqBean
import com.hm.iou.lawyer.business.NavigationHelper
import com.hm.iou.lawyer.business.comm.IouImageUploadAdapter
import com.hm.iou.logger.Logger
import com.hm.iou.router.Router
import com.hm.iou.tools.ImageLoader
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
        private const val REQ_CODE_SELECT_HEADER_IMAGE = 100
        private const val REQ_CODE_SELECT_AUTHEN_IMAGE_FRONT = 101
        private const val REQ_CODE_SELECT_AUTHEN_IMAGE_BACK = 102
        private const val REQ_CODE_SELECT_CERTIFICATE_IMAGE = 103
        private const val REQ_CODE_CERTIFICATE_IMAGE_GALLERY = 104

        private const val MAX_CERTIFICATE_OF_HONOR_IMAGE_COUNT = 3
    }

    private var mHeaderImagePath: String? = null//头像地址
    private var mAuthenImageFrontPath: String? = null//证件正面
    private var mAuthenImageBackPath: String? = null//证件反面
    private var mListPhotoPath: MutableList<IouData.FileEntity>? = null
    private val mCertificateImageAdapter =
        IouImageUploadAdapter(this, MAX_CERTIFICATE_OF_HONOR_IMAGE_COUNT)//荣誉证书
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
        iv_header_image.setOnClickListener(this)
        iv_add_header_image.setOnClickListener(this)
        //律师正面
        iv_authen_photo_front.setOnClickListener(this)
        iv_add_authen_photo_front.setOnClickListener(this)
        //律师反面
        iv_authen_photo_back.setOnClickListener(this)
        iv_add_authen_photo_back.setOnClickListener(this)
        //荣誉资质照片
        rv_certificate_of_honor.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rv_certificate_of_honor.adapter = mCertificateImageAdapter
        mCertificateImageAdapter.setOnItemClickListener { adapter, _, position ->
            val viewType = adapter.getItemViewType(position)
            if (viewType == IouImageUploadAdapter.ITEM_TYPE_ADD) {
                Router.getInstance()
                    .buildWithUrl("hmiou://m.54jietiao.com/select_pic/index")
                    .withString(
                        "enable_select_max_num",
                        (MAX_CERTIFICATE_OF_HONOR_IMAGE_COUNT - mCertificateImageAdapter.getRealImageSize()).toString()
                    )
                    .navigation(mContext, REQ_CODE_SELECT_CERTIFICATE_IMAGE)
            } else if (viewType == IouImageUploadAdapter.ITEM_TYPE_IMAGE) {
                val urls = mCertificateImageAdapter.getImageUrls()
                NavigationHelper.toEditGalleryPage(
                    mContext,
                    urls,
                    position,
                    REQ_CODE_CERTIFICATE_IMAGE_GALLERY
                )
            }
        }
        //提交认证
        bottomBar.setOnTitleClickListener {
            doSubmit()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //个人证件照片
        if (REQ_CODE_SELECT_HEADER_IMAGE == requestCode) {
            if (Activity.RESULT_OK == resultCode) {
                data?.let {
                    iv_add_header_image.visibility = INVISIBLE
                    val path = data.getStringExtra("extra_result_selection_path_first")
                    Logger.d("头像camera path: $mHeaderImagePath")
                    CompressPictureUtil.compressPic(
                        mContext, path
                    ) { file ->
                        mHeaderImagePath = file.absolutePath
                        ImageLoader.getInstance(mContext)
                            .displayImage("file://$mHeaderImagePath", iv_header_image)
                        checkValue()
                    }

                }

            }
            return
        }
        //证件正面照片
        if (REQ_CODE_SELECT_AUTHEN_IMAGE_FRONT == requestCode) {
            if (Activity.RESULT_OK == resultCode) {
                data?.let {
                    iv_add_authen_photo_front.visibility = INVISIBLE
                    val path = data.getStringExtra("extra_result_selection_path_first")
                    Logger.d("证件正面camera path: $path")
                    CompressPictureUtil.compressPic(
                        mContext, path
                    ) { file ->
                        mAuthenImageFrontPath = file.absolutePath
                        ImageLoader.getInstance(mContext)
                            .displayImage("file://$mAuthenImageFrontPath", iv_authen_photo_front)
                        checkValue()
                    }
                }

            }
            return
        }
        //证件反面照片
        if (REQ_CODE_SELECT_AUTHEN_IMAGE_BACK == requestCode) {
            if (Activity.RESULT_OK == resultCode) {
                data?.let {
                    iv_add_authen_photo_back.visibility = INVISIBLE
                    val path = data.getStringExtra("extra_result_selection_path_first")
                    Logger.d("证件反面camera path: $path")
                    CompressPictureUtil.compressPic(
                        mContext, path
                    ) { file ->
                        mAuthenImageBackPath = file.absolutePath
                        ImageLoader.getInstance(mContext)
                            .displayImage("file://$mAuthenImageBackPath", iv_authen_photo_back)
                        checkValue()
                    }
                }

            }
            return
        }
        //荣誉证书
        if (REQ_CODE_SELECT_CERTIFICATE_IMAGE == requestCode && Activity.RESULT_OK == resultCode) {
            if (resultCode == Activity.RESULT_OK) {
                data?.let {
                    val pathList = data.getStringArrayListExtra("extra_result_selection_path")
                    if (pathList != null && pathList.isNotEmpty()) {
                        CompressPictureUtil.compressPic(this, pathList) { list ->
                            if (mListPhotoPath == null) {
                                mListPhotoPath = mutableListOf()
                            }
                            for (file in list) {
                                val entity = IouData.FileEntity()
                                entity.value = "file://" + file.absolutePath
                                mListPhotoPath!!.add(entity)
                                mCertificateImageAdapter.addData(entity.value)
                            }
                        }
                    }
                }
            }
            return
        }
        //荣誉证书画廊
        if (REQ_CODE_CERTIFICATE_IMAGE_GALLERY == requestCode) {
            if (resultCode == Activity.RESULT_OK) {
                data?.let {
                    val delList =
                        data.getStringArrayListExtra(ImageGalleryActivity.EXTRA_KEY_DELETE_URLS)
                    if (delList == null || delList.isEmpty())
                        return
                    mListPhotoPath?.let {
                        for (url in delList) {
                            val it = it.iterator()
                            while (it.hasNext()) {
                                if (!TextUtils.isEmpty(url) && url == it.next().value) {
                                    it.remove()
                                }
                            }
                        }
                    }
                    mCertificateImageAdapter.deleteUrl(delList)
                }
            }
        }
    }

    override fun onClick(view: View?) {
        when (view) {
            tv_certificate_start_time -> {
                showDatePicker()
            }
            iv_header_image, iv_add_header_image -> {
                Router.getInstance()
                    .buildWithUrl("hmiou://m.54jietiao.com/select_pic/index")
                    .withString("enable_select_max_num", "1")
                    .navigation(mContext, REQ_CODE_SELECT_HEADER_IMAGE)
            }
            iv_authen_photo_front, iv_add_authen_photo_front -> {
                Router.getInstance()
                    .buildWithUrl("hmiou://m.54jietiao.com/select_pic/index")
                    .withString("enable_select_max_num", "1")
                    .navigation(mContext, REQ_CODE_SELECT_AUTHEN_IMAGE_FRONT)
            }
            iv_authen_photo_back, iv_add_authen_photo_back -> {
                Router.getInstance()
                    .buildWithUrl("hmiou://m.54jietiao.com/select_pic/index")
                    .withString("enable_select_max_num", "1")
                    .navigation(mContext, REQ_CODE_SELECT_AUTHEN_IMAGE_BACK)
            }
        }
    }


    private fun doSubmit() {
        //执业证号
        val certificateCode = et_certificate_code.text ?: ""
        //执业律所
        val lawyerFirmName = et_lawyer_firm.text ?: ""
        //持证日期
        val certificateStartTime = tv_certificate_start_time.text ?: ""
        //个人简介
        val selfIntroduction = et_self_introduction.text ?: ""

        if (certificateCode.length < 17) {
            toastErrorMessage("请输入正确的执业证号")
            return
        }
        if (lawyerFirmName.length < 5) {
            toastErrorMessage("执业律所必须在5-15个字以内")
            return
        }
        if (certificateStartTime.isEmpty()) {
            toastErrorMessage("请选择持证日期")
            return
        }
        if (selfIntroduction.length < 30) {
            toastErrorMessage("个人简介必须在30-200字以内")
            return
        }
        if (mHeaderImagePath == null) {
            toastErrorMessage("请上传个人形象照片")
            return
        }
        if (mAuthenImageFrontPath == null) {
            toastErrorMessage("请上传律师执业证姓名照片页")
            return
        }
        if (mAuthenImageBackPath == null) {
            toastErrorMessage("请上传律师执业证年检页")
            return
        }

//        val req = LawyerAuthenticationReqBean()
//        mPresenter.lawyerAuthentication()
    }

    /**
     * 校验结果
     */
    private fun checkValue() {
        //执业证号
        val certificateCode = et_certificate_code.text ?: ""
        //执业律所
        val lawyerFirmName = et_lawyer_firm.text ?: ""
        //持证日期
        val certificateStartTime = tv_certificate_start_time.text ?: ""
        //个人简介
        val selfIntroduction = et_self_introduction.text ?: ""

        if (certificateCode.length < 17) {
            bottomBar.setTitleBackgournd(R.drawable.uikit_selector_btn_minor_small)
            bottomBar.setTitleTextColor(R.color.uikit_text_auxiliary)
            return
        }
        if (lawyerFirmName.length < 5) {
            bottomBar.setTitleBackgournd(R.drawable.uikit_selector_btn_minor_small)
            bottomBar.setTitleTextColor(R.color.uikit_text_auxiliary)
            return
        }
        if (certificateStartTime.isEmpty()) {
            bottomBar.setTitleBackgournd(R.drawable.uikit_selector_btn_minor_small)
            bottomBar.setTitleTextColor(R.color.uikit_text_auxiliary)
            return
        }
        if (selfIntroduction.length < 30) {
            bottomBar.setTitleBackgournd(R.drawable.uikit_selector_btn_minor_small)
            bottomBar.setTitleTextColor(R.color.uikit_text_auxiliary)
            return
        }
        if (mHeaderImagePath == null) {
            bottomBar.setTitleBackgournd(R.drawable.uikit_selector_btn_minor_small)
            bottomBar.setTitleTextColor(R.color.uikit_text_auxiliary)
            return
        }
        if (mAuthenImageFrontPath == null) {
            bottomBar.setTitleBackgournd(R.drawable.uikit_selector_btn_minor_small)
            bottomBar.setTitleTextColor(R.color.uikit_text_auxiliary)
            return
        }
        if (mAuthenImageBackPath == null) {
            bottomBar.setTitleBackgournd(R.drawable.uikit_selector_btn_minor_small)
            bottomBar.setTitleTextColor(R.color.uikit_text_auxiliary)
            return
        }
        bottomBar.setTitleBackgournd(R.drawable.uikit_shape_common_btn_normal)
        bottomBar.setTitleTextColor(R.color.uikit_text_main_content)
    }


    override fun toLawyerHomePage() {

    }

    override fun toAuthenticationPage() {

    }

    override fun toAuthenProgressPage() {

    }


    private fun showDatePicker() {
        if (mDatePicker == null) {
            val now = System.currentTimeMillis()
            val calendar = Calendar.getInstance()
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

            calendar.timeInMillis = now
            val todayTime = SimpleDateFormat("yyyy年 MM月dd日").format(calendar.time)

            calendar.add(Calendar.YEAR, -40)
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
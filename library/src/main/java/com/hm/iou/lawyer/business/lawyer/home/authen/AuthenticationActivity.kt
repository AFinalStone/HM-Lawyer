package com.hm.iou.lawyer.business.lawyer.home.authen

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
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
import com.hm.iou.base.file.FileUtil
import com.hm.iou.base.mvp.HMBaseActivity
import com.hm.iou.base.photo.CompressPictureUtil
import com.hm.iou.base.photo.ImageCropper
import com.hm.iou.database.table.IouData
import com.hm.iou.lawyer.R
import com.hm.iou.lawyer.bean.res.ImageUrlFileIdBean
import com.hm.iou.lawyer.bean.res.LawyerAuthenticationResBean
import com.hm.iou.lawyer.business.NavigationHelper
import com.hm.iou.lawyer.business.comm.IouImageUploadAdapter
import com.hm.iou.logger.Logger
import com.hm.iou.router.Router
import com.hm.iou.tools.ImageLoader
import com.hm.iou.tools.kt.dp2px
import com.hm.iou.uikit.datepicker.TimePickerDialog
import kotlinx.android.synthetic.main.lawyer_activity_lawyer_authentication.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

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

    private var mCertificateStartTime: String = ""//持证日期
    private var mHeaderImageBean: ImageUrlFileIdBean? = null//头像地址
    private var mAuthenImageFrontBean: ImageUrlFileIdBean? = null//证件正面
    private var mAuthenImageBackBean: ImageUrlFileIdBean? = null//证件反面
    private var mListHonorImageBean: MutableList<IouData.FileEntity>? = null
    private val mHonorImageAdapter =
        IouImageUploadAdapter(this, MAX_CERTIFICATE_OF_HONOR_IMAGE_COUNT)//荣誉证书
    private var mDatePicker: Dialog? = null

    override fun getLayoutId(): Int = R.layout.lawyer_activity_lawyer_authentication

    override fun initPresenter(): AuthenticationPresenter =
        AuthenticationPresenter(this, this)

    override fun initEventAndData(savedInstanceState: Bundle?) {
        initView()
        mPresenter.init()
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
        rv_certificate_of_honor.adapter = mHonorImageAdapter
        mHonorImageAdapter.setOnItemClickListener { adapter, _, position ->
            val viewType = adapter.getItemViewType(position)
            if (viewType == IouImageUploadAdapter.ITEM_TYPE_ADD) {
                Router.getInstance()
                    .buildWithUrl("hmiou://m.54jietiao.com/select_pic/index")
                    .withString(
                        "enable_select_max_num",
                        (MAX_CERTIFICATE_OF_HONOR_IMAGE_COUNT - mHonorImageAdapter.getRealImageSize()).toString()
                    )
                    .navigation(mContext, REQ_CODE_SELECT_CERTIFICATE_IMAGE)
            } else if (viewType == IouImageUploadAdapter.ITEM_TYPE_IMAGE) {
                val urls = mHonorImageAdapter.getImageUrls()
                NavigationHelper.toEditGalleryPage(
                    mContext,
                    urls,
                    position,
                    REQ_CODE_CERTIFICATE_IMAGE_GALLERY
                )
            }
        }
        //提交认证
        bottom_bar.setOnTitleClickListener {
            doSubmit()
        }
        et_certificate_code.requestFocus()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //个人证件照片
        if (REQ_CODE_SELECT_HEADER_IMAGE == requestCode && Activity.RESULT_OK == resultCode) {
            data?.let {
                val path = data.getStringExtra("extra_result_selection_path_first")
                Logger.d("头像camera path: $mHeaderImageBean")

                ImageCropper.Helper.with(this)
                    .setCallback { bitmap, _ ->
                        val fileCrop =
                            File(FileUtil.getExternalCacheDirPath(mContext) + File.separator + System.currentTimeMillis() + ".jpg")
                        CompressPictureUtil.saveBitmapToTargetFile(
                            fileCrop,
                            bitmap,
                            Bitmap.CompressFormat.JPEG
                        )
                        CompressPictureUtil.compressPic(
                            mContext, fileCrop.absolutePath
                        ) { file ->
                            mHeaderImageBean = ImageUrlFileIdBean()
                            val picPath = "file://" + file.absolutePath
                            mHeaderImageBean?.url = picPath
                            ImageLoader.getInstance(mContext)
                                .displayImage(picPath, iv_header_image)
                            iv_add_header_image.visibility = INVISIBLE
                            checkValue()
                        }
                    }
                    .create()
                    .crop(path, 150, 150, false, "crop")


            }
            return
        }
        //证件正面照片
        if (REQ_CODE_SELECT_AUTHEN_IMAGE_FRONT == requestCode && Activity.RESULT_OK == resultCode) {
            data?.let {
                val path = data.getStringExtra("extra_result_selection_path_first")
                Logger.d("证件正面camera path: $path")
                CompressPictureUtil.compressPic(
                    mContext, path
                ) { file ->
                    mAuthenImageFrontBean = ImageUrlFileIdBean()
                    val picPath = "file://" + file.absolutePath
                    mAuthenImageFrontBean?.url = picPath
                    ImageLoader.getInstance(mContext)
                        .displayImage(picPath, iv_authen_photo_front)
                    iv_add_authen_photo_front.visibility = INVISIBLE
                    checkValue()
                }
            }

            return
        }
        //证件反面照片
        if (REQ_CODE_SELECT_AUTHEN_IMAGE_BACK == requestCode && Activity.RESULT_OK == resultCode) {
            data?.let {
                val path = data.getStringExtra("extra_result_selection_path_first")
                Logger.d("证件反面camera path: $path")
                CompressPictureUtil.compressPic(
                    mContext, path
                ) { file ->
                    mAuthenImageBackBean = ImageUrlFileIdBean()
                    val picPath = "file://" + file.absolutePath
                    mAuthenImageBackBean?.url = picPath
                    ImageLoader.getInstance(mContext)
                        .displayImage(picPath, iv_authen_photo_back)
                    iv_add_authen_photo_back.visibility = INVISIBLE
                    checkValue()
                }
            }
            return
        }
        //荣誉证书
        if (REQ_CODE_SELECT_CERTIFICATE_IMAGE == requestCode && Activity.RESULT_OK == resultCode) {
            data?.let {
                val pathList = data.getStringArrayListExtra("extra_result_selection_path")
                if (pathList != null && pathList.isNotEmpty()) {
                    CompressPictureUtil.compressPic(this, pathList) { list ->
                        if (mListHonorImageBean == null) {
                            mListHonorImageBean = mutableListOf()
                        }
                        for (file in list) {
                            val entity = IouData.FileEntity()
                            entity.value = "file://" + file.absolutePath
                            mListHonorImageBean!!.add(entity)
                            mHonorImageAdapter.addData(entity.value)
                        }
                        checkValue()
                    }
                }
            }
            return
        }
        //荣誉证书画廊
        if (REQ_CODE_CERTIFICATE_IMAGE_GALLERY == requestCode && resultCode == Activity.RESULT_OK) {
            data?.let {
                val delList =
                    data.getStringArrayListExtra(ImageGalleryActivity.EXTRA_KEY_DELETE_URLS)
                if (delList == null || delList.isEmpty())
                    return
                mListHonorImageBean?.let {
                    for (url in delList) {
                        val iterator = it.iterator()
                        while (iterator.hasNext()) {
                            if (!TextUtils.isEmpty(url) && url == iterator.next().value) {
                                iterator.remove()
                            }
                        }
                    }
                }
                mHonorImageAdapter.deleteUrl(delList)
                checkValue()
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

    override fun showDetail(detail: LawyerAuthenticationResBean) {
        //执业证号
        et_certificate_code.setText(detail.licenseNumber ?: "")
        et_certificate_code.setSelection(et_certificate_code.length())
        //执业律所
        et_lawyer_firm.setText(detail.lawFirm ?: "")
        //持证日期
        var certificateStartTime = ""
        try {
            val sdf01 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val sdf02 = SimpleDateFormat("yyyy年MM月dd日")
            val date = sdf01.parse(detail.holdingDate)
            certificateStartTime = sdf02.format(date)
            mCertificateStartTime = detail.holdingDate ?: ""
        } catch (e: Exception) {
            e.printStackTrace()
        }
        tv_certificate_start_time.text = certificateStartTime
        //个人介绍
        et_self_introduction.setText(detail.info ?: "")
        //个人证件照
        mHeaderImageBean = detail.image
        mHeaderImageBean?.let {
            ImageLoader.getInstance(mContext)
                .displayImage(
                    mHeaderImageBean?.url,
                    iv_header_image,
                    R.mipmap.uikit_icon_header_unknow
                )
            iv_add_header_image.visibility = INVISIBLE
        }
        //认证照片
        val listAuthen = detail.authCerts
        if (!listAuthen.isNullOrEmpty()) {
            try {
                mAuthenImageFrontBean = listAuthen[0]
                ImageLoader.getInstance(mContext).displayImage(
                    mAuthenImageFrontBean?.url,
                    iv_authen_photo_front,
                    R.mipmap.lawyer_ic_lawyer_authen_front_image
                )
                iv_add_authen_photo_front.visibility = INVISIBLE

                mAuthenImageBackBean = listAuthen[1]
                ImageLoader.getInstance(mContext)
                    .displayImage(
                        mAuthenImageBackBean?.url,
                        iv_authen_photo_back,
                        R.mipmap.lawyer_ic_lawyer_authen_back_image
                    )
                iv_add_authen_photo_back.visibility = INVISIBLE
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        //荣誉资质
        val listHonor = detail.honors
        if (!listHonor.isNullOrEmpty()) {
            for (file in listHonor) {
                val entity = IouData.FileEntity()
                entity.id = file.picId
                entity.value = file.url
                if (mListHonorImageBean == null) {
                    mListHonorImageBean = mutableListOf()
                }
                mListHonorImageBean?.add(entity)
                mHonorImageAdapter.addData(entity.value)
            }
        }
        checkValue()
    }

    override fun setResultOkAndClosePage() {
        val intent = Intent()
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun doSubmit() {
        //执业证号
        val certificateCode: String = et_certificate_code.text.toString()
        //执业律所
        val lawyerFirmName: String = et_lawyer_firm.text.toString()
        //个人简介
        val selfIntroduction: String = et_self_introduction.text.toString()

        if (certificateCode.length < 17) {
            toastErrorMessage("请输入正确的执业证号")
            return
        }
        if (lawyerFirmName.length < 5) {
            toastErrorMessage("执业律所必须在5-15个字以内")
            return
        }
        if (mCertificateStartTime.isEmpty()) {
            toastErrorMessage("请选择持证日期")
            return
        }
        if (selfIntroduction.length < 30) {
            toastErrorMessage("个人简介必须在30-200字以内")
            return
        }
        if (mHeaderImageBean == null) {
            toastErrorMessage("请上传个人形象照片")
            return
        }
        if (mAuthenImageFrontBean == null) {
            toastErrorMessage("请上传律师执业证姓名照片页")
            return
        }
        if (mAuthenImageBackBean == null) {
            toastErrorMessage("请上传律师执业证年检页")
            return
        }
        //认证照片
        val listAuthenImage = ArrayList<ImageUrlFileIdBean>()
        mAuthenImageFrontBean?.let {
            listAuthenImage.add(it)
        }
        mAuthenImageBackBean?.let {
            listAuthenImage.add(it)
        }
        mPresenter.lawyerAuthentication(
            certificateCode,
            lawyerFirmName,
            mCertificateStartTime,
            selfIntroduction,
            mHeaderImageBean,
            mAuthenImageFrontBean,
            mAuthenImageBackBean,
            mListHonorImageBean
        )
    }

    /**
     * 校验结果
     */
    private fun checkValue() {
        if (et_certificate_code.length() < 17) {
            bottom_bar.setTitleBackgournd(R.drawable.uikit_selector_btn_minor_small)
            bottom_bar.setTitleTextColor(R.color.uikit_text_auxiliary)
            return
        }
        if (et_lawyer_firm.length() < 5) {
            bottom_bar.setTitleBackgournd(R.drawable.uikit_selector_btn_minor_small)
            bottom_bar.setTitleTextColor(R.color.uikit_text_auxiliary)
            return
        }
        if (mCertificateStartTime.isEmpty()) {
            bottom_bar.setTitleBackgournd(R.drawable.uikit_selector_btn_minor_small)
            bottom_bar.setTitleTextColor(R.color.uikit_text_auxiliary)
            return
        }
        if (et_self_introduction.length() < 30) {
            bottom_bar.setTitleBackgournd(R.drawable.uikit_selector_btn_minor_small)
            bottom_bar.setTitleTextColor(R.color.uikit_text_auxiliary)
            return
        }
        if (mHeaderImageBean == null) {
            bottom_bar.setTitleBackgournd(R.drawable.uikit_selector_btn_minor_small)
            bottom_bar.setTitleTextColor(R.color.uikit_text_auxiliary)
            return
        }
        if (mAuthenImageFrontBean == null) {
            bottom_bar.setTitleBackgournd(R.drawable.uikit_selector_btn_minor_small)
            bottom_bar.setTitleTextColor(R.color.uikit_text_auxiliary)
            return
        }
        if (mAuthenImageBackBean == null) {
            bottom_bar.setTitleBackgournd(R.drawable.uikit_selector_btn_minor_small)
            bottom_bar.setTitleTextColor(R.color.uikit_text_auxiliary)
            return
        }
        bottom_bar.setTitleBackgournd(R.drawable.uikit_shape_common_btn_normal)
        bottom_bar.setTitleTextColor(R.color.uikit_text_main_content)
    }


    private fun showDatePicker() {
        if (mDatePicker == null) {
            val now = System.currentTimeMillis()
            val calendar = Calendar.getInstance()
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

            calendar.timeInMillis = now
            val todayTime = SimpleDateFormat("yyyy年 MM月dd日").format(calendar.time)

            calendar.add(Calendar.YEAR, -40)
            //开始时间
            var startTime: String = sdf.format(calendar.time)
            val startDate: Date = sdf.parse("1980-08-01 00:00:00")
            if (startDate.after(calendar.time)) {
                startTime = sdf.format(startDate)
            }
            val endTime = sdf.format(now)

            val tvToday = TextView(mContext)
            tvToday.text = "今日时间    $todayTime"
            tvToday.layoutParams =
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mContext.dp2px(50))
            tvToday.gravity = Gravity.CENTER

            val builder = TimePickerDialog.Builder(this)
                .setTitle("持证日期")
                .setHeaderView(tvToday)
                .setScrollType(TimePickerDialog.SCROLL_TYPE.DAY)
                .setTimeRange(startTime, endTime)
                .setOnPickerConfirmListener { year, month, day, i3, i4 ->
                    var month = month
                    month++
                    mCertificateStartTime =
                        String.format("%d-%s-%s 00:00:00", year, patchZero(month), patchZero(day))
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
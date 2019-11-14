package com.hm.iou.lawyer.business.lawyer.home.edit

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.INVISIBLE
import com.hm.iou.base.mvp.HMBaseActivity
import com.hm.iou.base.photo.CompressPictureUtil
import com.hm.iou.lawyer.R
import com.hm.iou.logger.Logger
import com.hm.iou.router.Router
import com.hm.iou.tools.ImageLoader
import kotlinx.android.synthetic.main.lawyer_activity_year_check_authen.*

/**
 * 年检认证
 */
class YearCheckAuthenActivity : HMBaseActivity<YearCheckAuthenPresenter>(),
    YearCheckAuthenContract.View, View.OnClickListener {

    companion object {
        private const val REQ_CODE_SELECT_AUTHEN_IMAGE_FRONT = 101
        private const val REQ_CODE_SELECT_AUTHEN_IMAGE_BACK = 102
    }

    private var mAuthenImageFrontPath: String? = null//证件正面
    private var mAuthenImageBackPath: String? = null//证件反面

    override fun getLayoutId(): Int = R.layout.lawyer_activity_year_check_authen

    override fun initPresenter(): YearCheckAuthenPresenter =
        YearCheckAuthenPresenter(this, this)

    override fun initEventAndData(savedInstanceState: Bundle?) {
        //律师正面
        iv_authen_photo_front.setOnClickListener(this)
        iv_add_authen_photo_front.setOnClickListener(this)
        //律师反面
        iv_authen_photo_back.setOnClickListener(this)
        iv_add_authen_photo_back.setOnClickListener(this)
        //提交认证
        bottom_bar.setOnTitleClickListener {
            if (mAuthenImageFrontPath == null) {
                toastErrorMessage("请上传律师执业证姓名照片页")
                return@setOnTitleClickListener
            }
            if (mAuthenImageBackPath == null) {
                toastErrorMessage("请上传律师执业证年检页")
                return@setOnTitleClickListener
            }
            //认证照片
            val listAuthenImage = ArrayList<String>()
            listAuthenImage.add(mAuthenImageFrontPath ?: "")
            listAuthenImage.add(mAuthenImageBackPath ?: "")
            mPresenter.updateLawyerAuthenticationInfo(listAuthenImage)
        }
    }

    override fun onClick(view: View?) {
        when (view) {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
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
    }

    /**
     * 校验结果
     */
    private fun checkValue() {
        if (mAuthenImageFrontPath == null) {
            bottom_bar.setTitleBackgournd(R.drawable.uikit_selector_btn_minor_small)
            bottom_bar.setTitleTextColor(R.color.uikit_text_auxiliary)
            return
        }
        if (mAuthenImageBackPath == null) {
            bottom_bar.setTitleBackgournd(R.drawable.uikit_selector_btn_minor_small)
            bottom_bar.setTitleTextColor(R.color.uikit_text_auxiliary)
            return
        }
        bottom_bar.setTitleBackgournd(R.drawable.uikit_shape_common_btn_normal)
        bottom_bar.setTitleTextColor(R.color.uikit_text_main_content)
    }
}
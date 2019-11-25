package com.hm.iou.lawyer.business.user.create

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import com.hm.iou.base.ImageGalleryActivity
import com.hm.iou.base.comm.HMTextChangeListener
import com.hm.iou.base.mvp.HMBaseActivity
import com.hm.iou.base.photo.CompressPictureUtil
import com.hm.iou.database.table.IouData
import com.hm.iou.lawyer.R
import com.hm.iou.lawyer.business.NavigationHelper
import com.hm.iou.lawyer.business.comm.IouImageUploadAdapter
import com.hm.iou.router.Router
import com.hm.iou.tools.kt.extraDelegate
import com.hm.iou.tools.kt.putValue
import kotlinx.android.synthetic.main.lawyer_activity_create_lawyer_consult.*

/**
 * Created by hjy on 2019/11/22
 *
 * 创建律师咨询页面
 */
class CreateLawyerConsultActivity : HMBaseActivity<CreateLawyerConsultPresenter>(),
    CreateLawyerConsultContract.View {

    companion object Key {
        const val EXTRA_KEY_LAWYER_ID = "lawyer_id"
        const val EXTRA_KEY_PRICE = "price"
        const val EXTRA_KEY_DESC = "desc"
        const val EXTRA_KEY_IMGS = "imgs"

        private const val MAX_IMAGE_COUNT = 3
        private const val REQ_OPEN_SELECT_PIC = 100
        private const val REQ_IMAGE_GALLERY = 101

        const val REQ_PAY_LAWYER_CONSULT = 103
    }

    private var mLawyerId: String? by extraDelegate(EXTRA_KEY_LAWYER_ID, null)
    private var mPrice: Int? by extraDelegate(EXTRA_KEY_PRICE, null)
    private var mDesc: String? by extraDelegate(EXTRA_KEY_DESC, null)

    private val mImageAdapter = IouImageUploadAdapter(this, MAX_IMAGE_COUNT)
    private var mFileList: MutableList<IouData.FileEntity>? = null

    override fun getLayoutId() = R.layout.lawyer_activity_create_lawyer_consult

    override fun initPresenter() = CreateLawyerConsultPresenter(this, this)

    override fun initEventAndData(savedInstanceState: Bundle?) {
        savedInstanceState?.let {
            mLawyerId = it.getString(EXTRA_KEY_LAWYER_ID)
            mPrice = it.getInt(EXTRA_KEY_PRICE, 0)
            mDesc = it.getString(EXTRA_KEY_DESC)
        }
        initViews()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putValue(EXTRA_KEY_LAWYER_ID, mLawyerId)
        outState?.putValue(EXTRA_KEY_PRICE, mPrice)
        outState?.putValue(EXTRA_KEY_DESC, mDesc)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQ_OPEN_SELECT_PIC -> {
                if (resultCode != Activity.RESULT_OK)
                    return
                data?.let {
                    val pathList = data.getStringArrayListExtra("extra_result_selection_path")
                    if (pathList != null && pathList.isNotEmpty()) {
                        CompressPictureUtil.compressPic(this, pathList) { list ->
                            if (mFileList == null) {
                                mFileList = mutableListOf()
                            }
                            for (file in list) {
                                val entity = IouData.FileEntity()
                                entity.value = "file://" + file.absolutePath
                                mFileList!!.add(entity)
                                mImageAdapter.addData(entity.value)
                            }
                        }
                    }
                }
            }
            REQ_IMAGE_GALLERY -> {
                if (resultCode != Activity.RESULT_OK)
                    return
                data?.let {
                    val delList =
                        data.getStringArrayListExtra(ImageGalleryActivity.EXTRA_KEY_DELETE_URLS)
                    if (delList == null || delList.isEmpty())
                        return
                    mFileList?.let {
                        for (url in delList) {
                            val it = it.iterator()
                            while (it.hasNext()) {
                                if (!TextUtils.isEmpty(url) && url == it.next().value) {
                                    it.remove()
                                }
                            }
                        }
                    }
                    mImageAdapter.deleteUrl(delList)
                }
            }
            REQ_PAY_LAWYER_CONSULT -> {
                if (resultCode == Activity.RESULT_OK) {
                    mPresenter.createOrderSuccess()
                }
            }
        }
    }

    private fun initViews() {
        et_consult_price.addTextChangedListener(object : HMTextChangeListener() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkInputValues()
            }
        })
        et_consult_desc.addTextChangedListener(object : HMTextChangeListener() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tv_consult_char_count.text = "${et_consult_desc.text.trim().length}/500"
                checkInputValues()
            }
        })

        rv_consult_image.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rv_consult_image.adapter = mImageAdapter
        mImageAdapter.setOnItemClickListener { adapter, _, position ->
            val viewType = adapter.getItemViewType(position)
            if (viewType == IouImageUploadAdapter.ITEM_TYPE_ADD) {
                Router.getInstance()
                    .buildWithUrl("hmiou://m.54jietiao.com/select_pic/index")
                    .withString(
                        "enable_select_max_num",
                        (MAX_IMAGE_COUNT - mImageAdapter.getRealImageSize()).toString()
                    )
                    .navigation(mContext, REQ_OPEN_SELECT_PIC)
            } else if (viewType == IouImageUploadAdapter.ITEM_TYPE_IMAGE) {
                val urls = mImageAdapter.getImageUrls()
                NavigationHelper.toEditGalleryPage(
                    mContext, urls, position,
                    REQ_IMAGE_GALLERY
                )
            }
        }

        if (!mLawyerId.isNullOrEmpty() && (mPrice ?: 0) > 0) {
            tv_consult_price_label.text = "订单金额"
            et_consult_price.setText("${mPrice}元")
            et_consult_price.isEnabled = false
        }

        if (!mDesc.isNullOrEmpty()) {
            et_consult_desc.setText(mDesc)
        }



        bottom_bar.setOnTitleClickListener {
            toCreateLawyerConsult(false)
        }

        bottom_bar.setOnTitleLongClickListener {
            toCreateLawyerConsult(true)
            return@setOnTitleLongClickListener true
        }
    }

    private fun checkInputValues() {
        if (et_consult_price.text.trim().isEmpty() ||
            et_consult_desc.text.trim().length < 15
        ) {
            bottom_bar.setTitleBtnBackground(R.drawable.uikit_selector_btn_minor_small)
            bottom_bar.setTitleBtnTextColor(resources.getColor(R.color.uikit_text_auxiliary))
            return
        }
        bottom_bar.setTitleBtnBackground(R.drawable.uikit_selector_btn_main_small)
        bottom_bar.setTitleBtnTextColor(resources.getColor(R.color.uikit_text_main_content))
    }

    private fun toCreateLawyerConsult(innerUser: Boolean) {
        val price =
            if ((!mLawyerId.isNullOrEmpty()) && mPrice != null && (mPrice ?: 0) > 0) mPrice!!
            else (et_consult_price.text.trim().toString().toIntOrNull() ?: 0)
        if (price <= 0) {
            toastMessage("请输入报价")
            return
        }
        if (price < 10 || price > 1000) {
            toastMessage("报价范围为10-1000")
            return
        }
        val desc = et_consult_desc.text.trim().toString()
        if (desc.length < 15) {
            toastMessage("请输入15字以上的案件描述")
            return
        }

        mPresenter.createLawyerConsult(mLawyerId, price, desc, mFileList, innerUser)
    }

}
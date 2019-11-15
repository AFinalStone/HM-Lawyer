package com.hm.iou.lawyer.business.lawyer.workbench.order

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
import com.hm.iou.tools.kt.clickWithDuration
import com.hm.iou.tools.kt.extraDelegate
import com.hm.iou.tools.kt.getValue
import com.hm.iou.tools.kt.putValue
import com.hm.iou.uikit.dialog.HMActionSheetDialog
import kotlinx.android.synthetic.main.lawyer_activity_lawyer_finish_order.*

/**
 * 律师完成
 */
class LawyerFinishOrderActivity : HMBaseActivity<LawyerFinishOrderPresenter>(),
    LawyerFinishOrderContract.View {

    companion object {
        const val EXTRA_KEY_ORDER_ID = "order_id"
        private const val MAX_IMAGE_COUNT = 3
        private const val REQ_CODE_SELECT_IMAGE = 100
        private const val REQ_CODE_IMAGE_GALLERY = 101
    }

    /**
     * 订单编号
     */
    private var mOrderId: String? by extraDelegate(
        EXTRA_KEY_ORDER_ID,
        null
    )

    private var mListImageBean: MutableList<IouData.FileEntity>? = null
    private val mImageAdapter = IouImageUploadAdapter(this, MAX_IMAGE_COUNT)
    private var mBottomDialog: HMActionSheetDialog? = null


    override fun getLayoutId(): Int = R.layout.lawyer_activity_lawyer_finish_order

    override fun initPresenter(): LawyerFinishOrderPresenter =
        LawyerFinishOrderPresenter(this, this)


    override fun initEventAndData(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            mOrderId = savedInstanceState.getValue(EXTRA_KEY_ORDER_ID)
        }
        tv_mail_company_name.addTextChangedListener(object : HMTextChangeListener() {
            override fun onTextChanged(
                charSequence: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                checkValue()
            }
        })
        tv_mail_company_name.clickWithDuration {
            if (mBottomDialog == null) {
                mPresenter.getMailList()
            }
            mBottomDialog?.show()
        }
        iv_mail_company_name.clickWithDuration {
            if (mBottomDialog == null) {
                mPresenter.getMailList()
            }
            mBottomDialog?.show()
        }
        et_mail_id.addTextChangedListener(object : HMTextChangeListener() {
            override fun onTextChanged(
                charSequence: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                checkValue()
            }
        })
        //荣誉资质照片
        rv_mail_img.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rv_mail_img.adapter = mImageAdapter
        mImageAdapter.setOnItemClickListener { adapter, _, position ->
            val viewType = adapter.getItemViewType(position)
            if (viewType == IouImageUploadAdapter.ITEM_TYPE_ADD) {
                Router.getInstance()
                    .buildWithUrl("hmiou://m.54jietiao.com/select_pic/index")
                    .withString(
                        "enable_select_max_num",
                        (MAX_IMAGE_COUNT - mImageAdapter.getRealImageSize()).toString()
                    )
                    .navigation(mContext, REQ_CODE_SELECT_IMAGE)
            } else if (viewType == IouImageUploadAdapter.ITEM_TYPE_IMAGE) {
                val urls = mImageAdapter.getImageUrls()
                NavigationHelper.toEditGalleryPage(
                    mContext,
                    urls,
                    position,
                    REQ_CODE_IMAGE_GALLERY
                )
            }
        }
        //提交认证
        bottom_bar.setOnTitleClickListener {
            doSubmit()
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putValue(EXTRA_KEY_ORDER_ID, mOrderId)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //荣誉证书
        if (REQ_CODE_SELECT_IMAGE == requestCode && Activity.RESULT_OK == resultCode) {
            data?.let {
                val pathList = data.getStringArrayListExtra("extra_result_selection_path")
                if (pathList != null && pathList.isNotEmpty()) {
                    CompressPictureUtil.compressPic(this, pathList) { list ->
                        if (mListImageBean == null) {
                            mListImageBean = mutableListOf()
                        }
                        for (file in list) {
                            val entity = IouData.FileEntity()
                            entity.value = "file://" + file.absolutePath
                            mListImageBean?.add(entity)
                            mImageAdapter.addData(entity.value)
                        }
                    }
                }
            }
            return
        }
        //荣誉证书画廊
        if (REQ_CODE_IMAGE_GALLERY == requestCode && resultCode == Activity.RESULT_OK) {
            data?.let {
                val delList =
                    data.getStringArrayListExtra(ImageGalleryActivity.EXTRA_KEY_DELETE_URLS)
                if (delList == null || delList.isEmpty())
                    return
                mListImageBean?.let {
                    for (url in delList) {
                        val iterator = it.iterator()
                        while (iterator.hasNext()) {
                            if (!TextUtils.isEmpty(url) && url == iterator.next().value) {
                                iterator.remove()
                            }
                        }
                    }
                }
                mImageAdapter.deleteUrl(delList)
            }
        }
    }

    override fun showMailList(list: ArrayList<String>) {
        mBottomDialog = HMActionSheetDialog.Builder(mContext)
            .setActionSheetList(list)
            .setOnItemClickListener { _, s ->
                tv_mail_company_name.text = s
            }
            .create()

        mBottomDialog?.show()
    }

    /**
     * 提交数据
     */
    private fun doSubmit() {
        if (tv_mail_company_name.length() == 0) {
            toastErrorMessage("请选择快递名称")
            return
        }
        if (et_mail_id.length() == 0) {
            toastErrorMessage("请输入快递单号")
            return
        }
        if (mListImageBean.isNullOrEmpty()) {
            toastErrorMessage("请至少上传一张照片")
            return
        }
        val orderId = mOrderId ?: ""
        val mailCompanyName = tv_mail_company_name.text.toString()
        val mailId = et_mail_id.text.toString()
        val listImage = ArrayList<String>()
        mListImageBean?.let {
            for (image in it) {
                listImage.add(image.value)
            }
        }
        mPresenter.finishOrder(orderId, mailCompanyName, mailId, listImage)
    }

    /**
     * 校验结果
     */
    private fun checkValue() {
        if (tv_mail_company_name.length() == 0) {
            bottom_bar.setTitleBackgournd(R.drawable.uikit_selector_btn_minor_small)
            bottom_bar.setTitleTextColor(R.color.uikit_text_auxiliary)
            return
        }
        if (et_mail_id.length() == 0) {
            bottom_bar.setTitleBackgournd(R.drawable.uikit_selector_btn_minor_small)
            bottom_bar.setTitleTextColor(R.color.uikit_text_auxiliary)
            return
        }
        if (mListImageBean.isNullOrEmpty()) {
            bottom_bar.setTitleBackgournd(R.drawable.uikit_selector_btn_minor_small)
            bottom_bar.setTitleTextColor(R.color.uikit_text_auxiliary)
        }
        bottom_bar.setTitleBackgournd(R.drawable.uikit_shape_common_btn_normal)
        bottom_bar.setTitleTextColor(R.color.uikit_text_main_content)
    }

}
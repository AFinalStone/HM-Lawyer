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
import com.hm.iou.base.utils.RouterUtil
import com.hm.iou.database.table.IouData
import com.hm.iou.lawyer.R
import com.hm.iou.lawyer.bean.LetterReceiverBean
import com.hm.iou.lawyer.bean.res.CustLetterDetailResBean
import com.hm.iou.lawyer.business.NavigationHelper
import com.hm.iou.lawyer.business.comm.IouImageUploadAdapter
import com.hm.iou.router.Router
import com.hm.iou.sharedata.UserManager
import com.hm.iou.tools.kt.clickWithDuration
import com.hm.iou.tools.kt.extraDelegate
import com.hm.iou.tools.kt.putValue
import com.hm.iou.uikit.HMTopBarView
import kotlinx.android.synthetic.main.lawyer_activity_create_lawyer_letter.*

/**
 * Created by hjy on 2019/11/12
 *
 * 创建律师函
 *
 */
class CreateLawyerLetterActivity : HMBaseActivity<CreateLawyerLetterPresenter>(),
    CreateLawyerLetterContract.View {

    companion object {
        const val EXTRA_KEY_LAWYER_ID = "lawyer_id"
        const val EXTRA_KEY_PRICE = "price"
        const val EXTRA_KEY_SOURCE = "source"
        const val EXTRA_KEY_ORDER = "order"

        private const val MAX_IMAGE_COUNT = 3

        private const val REQ_OPEN_SELECT_PIC = 100
        private const val REQ_IMAGE_GALLERY = 101
        private const val REQ_INPUT_RECEIVER = 102
        const val REQ_PAY_LAWYER_LETTER = 103
    }

    private var mLawyerId: String? by extraDelegate(EXTRA_KEY_LAWYER_ID, null)
    private var mPrice: Int? by extraDelegate(EXTRA_KEY_PRICE, null)
    private var mSource: String? by extraDelegate(EXTRA_KEY_SOURCE, null)

    private var mReceiverInfo: LetterReceiverBean? = null

    private val mImageAdapter = IouImageUploadAdapter(this, MAX_IMAGE_COUNT)
    private var mFileList: MutableList<IouData.FileEntity>? = null

    override fun initPresenter(): CreateLawyerLetterPresenter =
        CreateLawyerLetterPresenter(this, this)

    override fun getLayoutId(): Int = R.layout.lawyer_activity_create_lawyer_letter

    override fun initEventAndData(savedInstanceState: Bundle?) {
        savedInstanceState?.let {
            mLawyerId = it.getString(EXTRA_KEY_LAWYER_ID)
            mPrice = it.getInt(EXTRA_KEY_PRICE, 0)
            mSource = it.getString(EXTRA_KEY_SOURCE)
        }
        mPresenter.initSource(mSource)
        initViews()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putValue(EXTRA_KEY_LAWYER_ID, mLawyerId)
        outState?.putValue(EXTRA_KEY_PRICE, mPrice)
        outState?.putValue(EXTRA_KEY_SOURCE, mSource)
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
            REQ_INPUT_RECEIVER -> {
                if (resultCode != Activity.RESULT_OK)
                    return
                data?.let {
                    val receiverInfo = data.getParcelableExtra<LetterReceiverBean>("receiver")
                    mReceiverInfo = receiverInfo
                    mReceiverInfo?.let {
                        tv_letter_receiver_info.text = "${it.receiverName}/${it.receiverMobile}"
                    }
                }
            }
            REQ_PAY_LAWYER_LETTER -> {
                if (resultCode == Activity.RESULT_OK) {
                    mPresenter.createOrderSuccess()
                }
            }
        }
    }

    private fun initViews() {
        topbar.setOnMenuClickListener(object : HMTopBarView.OnTopBarMenuClickListener {
            override fun onClickTextMenu() {
            }

            override fun onClickImageMenu() {
                RouterUtil.clickMenuLink(this@CreateLawyerLetterActivity, "https://h5.54jietiao.com/appTopic/articleDetail.html?articleId=39")
            }
        })

        et_letter_name.requestFocus()
        showSoftKeyboard(et_letter_name)
        et_letter_name.addTextChangedListener(object : HMTextChangeListener() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkInputValues()
            }
        })
        et_letter_mobile.addTextChangedListener(object : HMTextChangeListener() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkInputValues()
            }
        })
        et_letter_price.addTextChangedListener(object : HMTextChangeListener() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkInputValues()
            }
        })
        tv_letter_receiver_info.addTextChangedListener(object : HMTextChangeListener() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkInputValues()
            }
        })
        et_letter_desc.addTextChangedListener(object : HMTextChangeListener() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tv_letter_char_count.text = "${et_letter_desc.text.trim().length}/500"
                checkInputValues()
            }
        })

        ll_letter_receiver_info.clickWithDuration {
            NavigationHelper.toInputReceiverAddress(this, REQ_INPUT_RECEIVER, mReceiverInfo)
        }

        rv_letter_image.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rv_letter_image.adapter = mImageAdapter
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
                NavigationHelper.toEditGalleryPage(mContext, urls, position, REQ_IMAGE_GALLERY)
            }
        }

        val userInfo = UserManager.getInstance(this).userInfo
        et_letter_name.setText(userInfo.name)
        et_letter_name.setSelection(et_letter_name.length())
        et_letter_mobile.setText(userInfo.mobile)

        //如果带有律师信息，则报价金额不能修改
        var hasLawyer = false
        if (!mLawyerId.isNullOrEmpty() && (mPrice ?: 0) > 0) {
            et_letter_price.setText("${mPrice}元")
            et_letter_price.isEnabled = false
            hasLawyer = true
        }

        //如果是重新创建，则会带过来原来的订单信息
        val data = intent.getSerializableExtra(EXTRA_KEY_ORDER) as? CustLetterDetailResBean
        data?.let {
            et_letter_name.setText(data.name)
            et_letter_name.setSelection(et_letter_name.length())
            et_letter_mobile.setText(data.mobile)

            if (!hasLawyer) {
                et_letter_price.setText(data.price.toString())
            }

            mReceiverInfo = data.receiveInfo
            mReceiverInfo?.let {
                tv_letter_receiver_info.text = "${it.receiverName}/${it.receiverMobile}"
            }
            et_letter_desc.setText(data.caseDescription)

            if (mFileList == null) {
                mFileList = mutableListOf()
            }
            data.fileList?.run {
                forEach { item ->
                    val entity = IouData.FileEntity()
                    entity.id = item.id
                    entity.value = item.url
                    mFileList?.add(entity)
                    mImageAdapter.addData(item.url)
                }
            }
        }

        bottom_bar.setOnTitleClickListener {
            toCreateLawyerLetter(false)
        }

        bottom_bar.setOnTitleLongClickListener {
            toCreateLawyerLetter(true)
            return@setOnTitleLongClickListener true
        }
    }

    private fun checkInputValues() {
        if (et_letter_name.text.trim().isEmpty() ||
            et_letter_mobile.text.trim().isEmpty() ||
            et_letter_price.text.trim().isEmpty() ||
            tv_letter_receiver_info.text.trim().isEmpty() ||
            et_letter_desc.text.trim().isEmpty()
        ) {
            bottom_bar.isEnabled = false
            return
        }
        bottom_bar.isEnabled = true
    }

    private fun toCreateLawyerLetter(innerUser: Boolean) {
        val name = et_letter_name.text.trim().toString()
        val mobile = et_letter_mobile.text.trim().toString()
        val price = if (mPrice != null && (mPrice ?: 0) > 0) mPrice!! else (et_letter_price.text.trim().toString().toIntOrNull() ?: 0)
        val desc = et_letter_desc.text.trim().toString()

        if (price < 300 || price > 10000) {
            toastMessage("报价输入范围不正确")
            return
        }

        mPresenter.createLawyerLetter(name, mobile, mLawyerId, price, mReceiverInfo, desc, mFileList, innerUser)
    }

}
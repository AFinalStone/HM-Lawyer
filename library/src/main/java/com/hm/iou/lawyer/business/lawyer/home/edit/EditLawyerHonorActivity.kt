package com.hm.iou.lawyer.business.lawyer.home.edit

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import com.hm.iou.base.ImageGalleryActivity
import com.hm.iou.base.mvp.HMBaseActivity
import com.hm.iou.base.photo.CompressPictureUtil
import com.hm.iou.database.table.IouData
import com.hm.iou.lawyer.R
import com.hm.iou.lawyer.business.NavigationHelper
import com.hm.iou.lawyer.business.comm.IouImageUploadAdapter
import com.hm.iou.router.Router
import kotlinx.android.synthetic.main.lawyer_activity_lawyer_authentication.*

/**
 * 荣誉资质
 */
class EditLawyerHonorActivity : HMBaseActivity<EditLawyerHonorPresenter>(),
    EditLawyerHonorContract.View {

    companion object {
        private const val MAX_CERTIFICATE_OF_HONOR_IMAGE_COUNT = 3
        private const val REQ_CODE_SELECT_CERTIFICATE_IMAGE = 100
        private const val REQ_CODE_CERTIFICATE_IMAGE_GALLERY = 101
    }


    private var mListPhotoPath: MutableList<IouData.FileEntity>? = null
    private val mHonorImageAdapter =
        IouImageUploadAdapter(this, MAX_CERTIFICATE_OF_HONOR_IMAGE_COUNT)//荣誉证书

    override fun getLayoutId(): Int = R.layout.lawyer_activity_edit_lawyer_honor

    override fun initPresenter(): EditLawyerHonorPresenter =
        EditLawyerHonorPresenter(this, this)

    override fun initEventAndData(savedInstanceState: Bundle?) {
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
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
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
                                mHonorImageAdapter.addData(entity.value)
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
                    mHonorImageAdapter.deleteUrl(delList)
                }
            }
        }
    }

}
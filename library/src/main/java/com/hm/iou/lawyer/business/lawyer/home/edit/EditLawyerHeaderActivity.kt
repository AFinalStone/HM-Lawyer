package com.hm.iou.lawyer.business.lawyer.home.edit

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import com.hm.iou.base.file.FileUtil
import com.hm.iou.base.mvp.HMBaseActivity
import com.hm.iou.base.photo.CompressPictureUtil
import com.hm.iou.base.photo.ImageCropper
import com.hm.iou.lawyer.R
import com.hm.iou.router.Router
import com.hm.iou.tools.ImageLoader
import com.hm.iou.tools.kt.extraDelegate
import com.hm.iou.tools.kt.getValue
import com.hm.iou.tools.kt.putValue
import com.hm.iou.uikit.HMTopBarView
import kotlinx.android.synthetic.main.lawyer_activity_lawyer_header_detail.*
import java.io.File

/**
 * 荣誉资质
 */
class EditLawyerHeaderActivity : HMBaseActivity<EditLawyerHeaderPresenter>(),
    EditLawyerHeaderContract.View {

    companion object {
        private const val REQ_CODE_ALBUM = 10
        const val EXTRA_KEY_LAWYER_HEADE = "lawyer_header"
    }

    private var mLawyerHeader: String? by extraDelegate(
        EXTRA_KEY_LAWYER_HEADE,
        null
    )

    override fun getLayoutId(): Int = R.layout.lawyer_activity_lawyer_header_detail

    override fun initEventAndData(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            mLawyerHeader = savedInstanceState.getValue(EXTRA_KEY_LAWYER_HEADE)
        }
        topbar.setOnMenuClickListener(object : HMTopBarView.OnTopBarMenuClickListener {
            override fun onClickTextMenu() {
                Router.getInstance()
                    .buildWithUrl("hmiou://m.54jietiao.com/select_pic/index")
                    .withString("enable_select_max_num", "1")
                    .navigation(mContext, REQ_CODE_ALBUM)
            }

            override fun onClickImageMenu() {

            }
        })
        showUserAvatar(mLawyerHeader ?: "")
    }

    override fun initPresenter(): EditLawyerHeaderPresenter = EditLawyerHeaderPresenter(this, this)

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putValue(EXTRA_KEY_LAWYER_HEADE, mLawyerHeader)
    }

    override fun showUserAvatar(url: String) {
        ImageLoader.getInstance(mContext).displayImage(url, iv_header,R.mipmap.uikit_icon_header_unknow)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_CODE_ALBUM && resultCode == Activity.RESULT_OK) {
            data?.let {
                val path = data.getStringExtra("extra_result_selection_path_first")
                ImageCropper.Helper.with(this)
                    .setCallback { bitmap, _ ->
                        val fileCrop =
                            File(FileUtil.getExternalCacheDirPath(mContext) + File.separator + System.currentTimeMillis() + ".jpg")
                        CompressPictureUtil.saveBitmapToTargetFile(
                            fileCrop,
                            bitmap,
                            Bitmap.CompressFormat.JPEG
                        )
                        compressPic(fileCrop.absolutePath)
                    }
                    .create()
                    .crop(path, 150, 150, false, "crop")
            }

        }
    }

    private fun compressPic(fileUrl: String) {
        CompressPictureUtil.compressPic(
            this, fileUrl
        ) { file -> mPresenter.uploadHeader(file) }
    }

}
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

    }

    override fun getLayoutId(): Int = R.layout.lawyer_activity_lawyer_header_detail

    override fun initEventAndData(savedInstanceState: Bundle?) {
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
    }

    override fun initPresenter(): EditLawyerHeaderPresenter = EditLawyerHeaderPresenter(this, this)

    override fun showUserAvatar(url: String) {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_CODE_ALBUM) {
            if (resultCode == Activity.RESULT_OK) {
                val list = data!!.getStringArrayListExtra("extra_result_selection_path")
                if (list != null && !list.isEmpty()) {
                    val path = list[0]
                    ImageCropper.Helper.with(this)
                        .setCallback { bitmap, tag ->
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
    }

    private fun compressPic(fileUrl: String) {
//        CompressPictureUtil.compressPic(
//            this, fileUrl
//        ) { file -> mPresenter.uploadFile(file) }
    }

}
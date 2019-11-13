package com.hm.iou.lawyer.business.user

import android.content.Context
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hm.iou.lawyer.R
import com.hm.iou.tools.ImageLoader

/**
 * 图片展示
 */
class CommImageAdapter(val context: Context) :
    BaseQuickAdapter<String, BaseViewHolder>(R.layout.lawyer_item_lawyer_honor_img) {

    override fun convert(helper: BaseViewHolder?, item: String?) {
        helper ?: return
        item ?: return
        ImageLoader.getInstance(context)
            .displayImage(
                item, helper.itemView as ImageView, R.drawable.jietiao_bg_loading_progress,
                R.drawable.uikit_bg_pic_loading_error
            )
    }
}
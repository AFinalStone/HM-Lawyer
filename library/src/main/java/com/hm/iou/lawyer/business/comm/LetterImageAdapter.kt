package com.hm.iou.lawyer.business.comm

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Outline
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.ImageView

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.hm.iou.lawyer.R
import com.hm.iou.tools.DensityUtil
import com.hm.iou.tools.ImageLoader

/**
 * Created by hjy on 2019/1/21.
 */

interface IIouImageItem : MultiItemEntity {

    val imageUrl: String?

}

class IouImageUploadAdapter(val context: Context, private val mMaxCount: Int) :
    BaseMultiItemQuickAdapter<IIouImageItem, BaseViewHolder>(null) {

    private val mDataList = mutableListOf<IIouImageItem>()

    private val mAddItemData = object : IIouImageItem {

        override val imageUrl: String? = null

        override fun getItemType(): Int {
            return ITEM_TYPE_ADD
        }
    }

    init {
        addItemType(ITEM_TYPE_ADD, R.layout.lawyer_item_letter_upload_add)
        addItemType(ITEM_TYPE_IMAGE, R.layout.lawyer_item_letter_upload)
        mDataList.clear()
        mDataList.add(mAddItemData)
        setNewData(mDataList)
    }

    fun getImageUrls(): Array<String> {
        var list = mutableListOf<String>()
        for (item in mDataList) {
            if (!item.imageUrl.isNullOrEmpty())
                list.add(item.imageUrl!!)
        }
        return list.toTypedArray()
    }

    fun getRealImageSize(): Int {
        return if (mDataList.contains(mAddItemData)) {
            mDataList.size - 1
        } else {
            mDataList.size
        }
    }

    fun deleteUrl(urlList: List<String>?) {
        if (urlList == null || urlList.isEmpty())
            return
        for (url in urlList) {
            if (url.isNullOrEmpty())
                continue
            val it = mDataList.iterator()
            while (it.hasNext()) {
                val item = it.next()
                if (url == item.imageUrl) {
                    it.remove()
                }
            }
        }
        if (!mDataList.contains(mAddItemData)) {
            mDataList.add(mAddItemData)
        }
        notifyDataSetChanged()
    }


    fun addData(url: String?) {
        if (url.isNullOrEmpty())
            return

        if (getRealImageSize() >= mMaxCount) {
            return
        }

        var exist = false
        for (item in mDataList) {
            if (url == item.imageUrl) {
                exist = true
                break
            }
        }
        if (!exist) {
            val item = object : IIouImageItem {
                override val imageUrl: String
                    get() = url

                override fun getItemType(): Int {
                    return ITEM_TYPE_IMAGE
                }
            }
            mDataList.remove(mAddItemData)
            mDataList.add(item)

            if (getRealImageSize() < 3) {
                mDataList.add(mAddItemData)
            }
        }
        notifyDataSetChanged()
    }

    override fun convert(helper: BaseViewHolder?, item: IIouImageItem?) {
        helper ?: return
        item ?: return
        if (item.itemType == ITEM_TYPE_IMAGE) {
            ImageLoader.getInstance(context)
                .displayImage(
                    item.imageUrl, helper.itemView as ImageView,
                    R.drawable.uikit_bg_pic_loading_place, R.drawable.uikit_bg_pic_loading_error
                )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val viewHolder = super.onCreateViewHolder(parent, viewType)
        if (viewType == ITEM_TYPE_ADD) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                setBgRadius(viewHolder.itemView)
            }
        }
        return viewHolder
    }

    /**
     * 设置dialog圆角
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setBgRadius(view: View) {
        val bgRadius = DensityUtil.dip2px(context, 4f)
        view.outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                outline.setRoundRect(0, 0, view.width, view.height, bgRadius.toFloat())
            }
        }
        view.clipToOutline = true
    }


    companion object {

        const val ITEM_TYPE_ADD = 1
        const val ITEM_TYPE_IMAGE = 2
    }
}
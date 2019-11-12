package com.hm.iou.lawyer.business.user.find

import android.content.Context
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hm.iou.lawyer.R
import com.hm.iou.tools.ImageLoader

/**
 * Created by hjy on 2019/11/11
 *
 * 查找律师列表adapter
 */
class LawyerListAdapter(val context: Context) :
    BaseQuickAdapter<ILawyerItem, BaseViewHolder>(R.layout.lawyer_item_find_lawyer) {

    override fun convert(helper: BaseViewHolder?, item: ILawyerItem?) {
        helper ?: return
        item ?: return

        ImageLoader.getInstance(context).displayImage(
            item.getAvatar(), helper.getView(R.id.iv_lawyer_avatar),
            R.mipmap.uikit_icon_header_unknow, R.mipmap.uikit_icon_header_unknow
        )

        helper.setText(R.id.tv_lawyer_name, item.getName())
        helper.setText(R.id.tv_lawyer_age_limit, item.getAgeLimit())
        helper.setText(R.id.tv_lawyer_company, item.getCompanyName())
        helper.setText(R.id.tv_lawyer_location, item.getLocation())
    }

}

interface ILawyerItem {

    fun getLawyerId(): String

    fun getAvatar(): String?

    fun getName(): String?

    fun getAgeLimit(): String?

    fun getCompanyName(): String?

    fun getLocation(): String?

}
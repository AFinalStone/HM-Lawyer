package com.hm.iou.lawyer.business.comm

import android.content.Context
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hm.iou.lawyer.R
import com.hm.iou.tools.ImageLoader

/**
 * Created by hjy on 2019-11-22
 *
 * 律师解答 adapter
 */
class ConsultAnswerAdapter : BaseQuickAdapter<IAnswer, BaseViewHolder> {

    constructor(context: Context) : super((R.layout.lawyer_item_consult_answer)) {
        mContext = context
    }

    override fun convert(helper: BaseViewHolder?, item: IAnswer?) {
        helper ?: return
        item ?: return

        ImageLoader.getInstance(mContext)
            .displayImage(
                item.getAvatar(),
                helper.getView(R.id.iv_answer_avatar),
                R.mipmap.uikit_icon_header_unknow,
                R.mipmap.uikit_icon_header_unknow
            )

        helper.setText(R.id.tv_answer_name, item.getName())
        helper.setText(R.id.tv_answer_time, item.getTime())
        helper.setText(R.id.tv_answer_content, item.getAnswer())

        val position = helper.adapterPosition
        val size = mData?.size ?: 0
        helper.setVisible(R.id.view_bottom_divider, position != (size - 1))
    }


}

interface IAnswer {

    fun getAvatar(): String?

    fun getName(): String?

    fun getTime(): String?

    fun getAnswer(): String?

}



package com.hm.iou.lawyer.business.lawyer.workbench.list

import android.content.Context
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hm.iou.lawyer.R
import com.hm.iou.tools.ImageLoader

/**
 * 订单列表适配器
 */
class OrderAdapter :
    BaseQuickAdapter<IOrderItem, BaseViewHolder> {

    constructor(context: Context) : super((R.layout.lawyer_item_lawyer_order)) {
        mContext = context
    }

    override fun convert(helper: BaseViewHolder?, item: IOrderItem?) {
        helper ?: return
        item ?: return
        val ivHeader = helper.getView<ImageView>(R.id.iv_header)
        ImageLoader.getInstance(mContext).displayImage(item.getUserHeader(), ivHeader)
        helper.setText(R.id.tv_name, item.getUserName())
        helper.setText(R.id.tv_order_time, item.getTime())
        helper.setText(R.id.tv_order_type, item.getTypeStr())
        helper.setText(R.id.tv_order_desc, item.getDesc())
        helper.setText(R.id.tv_order_price, item.getPrice())
        helper.setText(R.id.tv_order_status, item.getStatusStr())
    }
}

interface IOrderItem {

    fun getOrderId(): String?
    /**
     * 头像
     */
    fun getUserHeader(): String?

    /**
     * 姓名
     */
    fun getUserName(): String?

    fun getTime(): String?

    fun getDesc(): String?

    fun getTypeStr(): String?

    fun getPrice(): String?

    fun getStatusStr(): String?

    fun getRelationId(): Int?

}
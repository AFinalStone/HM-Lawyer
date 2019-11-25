package com.hm.iou.lawyer.business.user.order

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hm.iou.lawyer.R

/**
 * 订单列表
 */
class MyOrderAdapter :
    BaseQuickAdapter<IOrderItem, BaseViewHolder>(R.layout.lawyer_item_user_order) {

    override fun convert(helper: BaseViewHolder?, item: IOrderItem?) {
        helper ?: return
        item ?: return

        helper.setText(R.id.tv_order_time, item.getTime())
        helper.setText(R.id.tv_order_type, item.getTypeStr())
        helper.setText(R.id.tv_order_desc, item.getDesc())
        helper.setText(R.id.tv_order_price, item.getPrice())
        helper.setText(R.id.tv_order_status, item.getStatusStr())
        helper.setTextColor(R.id.tv_order_type, item.getTypeTextColor())
        helper.setBackgroundRes(R.id.tv_order_type, item.getTypeBgResId())
    }
}

interface IOrderItem {

    fun getOrderId(): String?

    fun getTime(): String?

    fun getDesc(): String?

    fun getType(): Int = 0

    fun getTypeStr(): String?

    fun getPrice(): String?

    fun getStatusStr(): String?

    fun getTypeBgResId(): Int

    fun getTypeTextColor(): Int
}
package com.hm.iou.lawyer.dict

enum class OrderStatus(val status: Int, val desc: String) {

    NOT_PAY(1, "待支付"),  //目前不展示
    WAIT(2, "待接单"),
    ONGOING(3, "进行中"),
    COMPLETE(4, "已完成"),
    CANCEL(5, "已取消"),
    REFUSE(6, "律师已拒绝")
}
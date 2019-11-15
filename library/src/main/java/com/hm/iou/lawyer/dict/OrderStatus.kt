package com.hm.iou.lawyer.dict

enum class OrderStatus(val status: Int, val desc: String) {

    NOT_PAY(1, "待支付"),  //目前不展示
    WAIT(2, "待接单"),
    ONGOING(3, "进行中"),
    COMPLETE(4, "已完成"),
    CANCEL(5, "已取消"),
    REFUSE(6, "律师已拒绝");

    companion object {
        /**
         * 通过类型获取实例
         */
        fun getInstanceByStatus(status: Int): OrderStatus {
            return when (status) {
                NOT_PAY.status -> NOT_PAY
                WAIT.status -> WAIT
                ONGOING.status -> ONGOING
                COMPLETE.status -> COMPLETE
                CANCEL.status -> CANCEL
                REFUSE.status -> REFUSE
                else -> WAIT
            }
        }
    }
}
package com.hm.iou.lawyer.dict

/**
 * @author : 借条管家-shilei
 * @version : 0.0.1
 * @Date : 2019-11-11 10:06
 * @E-Mail : afinalstone@foxmail.com
 */
enum class LawyerOrderTabStatus(val status: Int, val statusName: String) {

    ALL(0, "全部"),
    LOADING(3, "进行中"),
    FINISH(4, "已完成"),
    CANCEL(5, "已取消");

    companion object {
        /**
         * 通过类型获取实例
         */
        fun getInstanceByStatus(status: Int): LawyerOrderTabStatus {
            return when (status) {
                ALL.status -> ALL
                LOADING.status -> LOADING
                FINISH.status -> FINISH
                CANCEL.status -> CANCEL
                else -> ALL
            }
        }
    }

}
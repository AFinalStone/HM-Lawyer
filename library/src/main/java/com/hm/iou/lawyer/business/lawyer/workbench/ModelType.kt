package com.hm.iou.lawyer.business.lawyer.workbench

/**
 * @author : 借条管家-shilei
 * @version : 0.0.1
 * @Date : 2019-11-11 10:06
 * @E-Mail : afinalstone@foxmail.com
 */
enum class ModelType(val modelType: Int, val modelName: String) {

    WAIT_TO_LOADING(100, "进行中"),
    WAIT_TO_FINISH(101, "已完成"),
    WAIT_TO_ALL_ORDER(102, "全部订单"),
    LAWYER_ORDER_CONSULT(103, "律师咨询"),
    LAWYER_LETTER(103, "律师函"),
    LAWYER_INVITE_RECEIVE(103, "邀请接单");

    companion object {
        /**
         * 通过类型获取实例
         */
        fun getInstanceByType(type: Int): ModelType {
            return when (type) {
                WAIT_TO_LOADING.modelType -> WAIT_TO_LOADING
                WAIT_TO_FINISH.modelType -> WAIT_TO_FINISH
                WAIT_TO_ALL_ORDER.modelType -> WAIT_TO_ALL_ORDER
                LAWYER_ORDER_CONSULT.modelType -> LAWYER_ORDER_CONSULT
                LAWYER_LETTER.modelType -> LAWYER_LETTER
                LAWYER_INVITE_RECEIVE.modelType -> LAWYER_INVITE_RECEIVE
                else -> WAIT_TO_LOADING
            }
        }
    }

}
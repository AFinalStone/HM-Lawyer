package com.hm.iou.lawyer.dict

/**
 * 创建律师函来源
 */
enum class LawyerLetterSource(val source: Int, val desc: String) {

    SOURCE_IOU(1, "借条"),
    SOURCE_ONLINE(2, "APP在线律师服务")

}
package com.hm.iou.lawyer.bean.req

data class LawyerFinishOrderReqBean(
    val billId: String,
    val expressName: String,
    val expressNumber: String,
    val finishImgs: List<String>
)
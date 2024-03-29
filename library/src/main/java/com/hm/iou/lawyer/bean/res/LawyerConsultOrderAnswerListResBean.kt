package com.hm.iou.lawyer.bean.res

data class LawyerConsultOrderAnswerListResBean(
    val replies: List<LawyerConsultOrderAnswerItemBean>?
)

data class LawyerConsultOrderAnswerItemBean(
    val avatar: String?,
    val createTime: String?,
    val msg: String?,
    val name: String?
)
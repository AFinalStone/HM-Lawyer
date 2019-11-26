package com.hm.iou.lawyer.bean.res

/**
 * @author : 借条管家-shilei
 * @version : 0.0.1
 * @Date : 2019-11-25 18:22
 * @E-Mail : afinalstone@foxmail.com
 */
data class LawyerConsultOrderListResBean(
    val list: List<LawyerConsultOrderItemBean>?,
    val total: Int?
)

data class LawyerConsultOrderItemBean(
    val avatarUrl: String,
    val billId: String,
    val billType: Int,
    val caseDescription: String,
    val createTime: String,
    val name: String,
    val price: Int,
    val status: Int
)
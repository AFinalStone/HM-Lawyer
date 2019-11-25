package com.hm.iou.lawyer.bean.res

/**
 * @author : 借条管家-shilei
 * @version : 0.0.1
 * @Date : 2019-11-12 14:10
 * @E-Mail : afinalstone@foxmail.com
 */
data class LawyerConsultOrderDetailResBean(
    val appoint: Boolean,
    val billId: String,
    val caseDescription: String,
    val doDate: String,
    val fileUrls: List<String>,
    val price: Int,
    val status: Int
)
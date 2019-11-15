package com.hm.iou.lawyer.bean.res

/**
 * @author : 借条管家-shilei
 * @version : 0.0.1
 * @Date : 2019-11-15 09:14
 * @E-Mail : afinalstone@foxmail.com
 */
data class LawyerMyOrderListResBean(
    val list: List<LawyerOrderItem>?,
    val total: Int?
)

class LawyerOrderItem {
    val billId: String? = null
    val caseDescription: String? = null
    val createTime: String? = null
    val price: Int? = null
    val relationId: Int? = null
    val status: Int? = null
    val type: Int? = null
}
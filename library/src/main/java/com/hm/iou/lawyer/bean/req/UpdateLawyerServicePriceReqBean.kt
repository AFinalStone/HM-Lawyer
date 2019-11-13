package com.hm.iou.lawyer.bean.req

/**
 * @author : 借条管家-shilei
 * @version : 0.0.1
 * @Date : 2019-11-12 21:08
 * @E-Mail : afinalstone@foxmail.com
 */
data class UpdateLawyerServicePriceReqBean(
    val price: Int,//价格
    val serviceId: String//执业律所
)
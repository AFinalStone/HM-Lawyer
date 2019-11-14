package com.hm.iou.lawyer.bean.res

data class LawyerServiceBean(
    val logo: String?,//律师服务icon
    val serviceDesc: String?,//律师服务文案
    val serviceId: Int?,//律师服务id
    val serviceName: String?,//律师服务名
    val servicePrice: String?,//服务价格中问描述
    val price: Int = 0      //服务价格，单位"元"
)
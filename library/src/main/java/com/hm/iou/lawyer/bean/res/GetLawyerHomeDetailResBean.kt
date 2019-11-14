package com.hm.iou.lawyer.bean.res

/**
 * @author : 借条管家-shilei
 * @version : 0.0.1
 * @Date : 2019-11-12 14:10
 * @E-Mail : afinalstone@foxmail.com
 */
data class GetLawyerHomeDetailResBean(
    val authName: String?,//姓名
    val holdingYearCount: Int?,//执业年数
    val honors: List<String>?,//荣誉资质
    val image: String?,//个人形象照
    val info: String?,//个人简介
    val lawFirm: String?,//执业律所
    val location: String?,//地址
    val services: List<LawyerServiceBean>?//律师服务
)

data class LawyerServiceBean(
    val logo: String?,//律师服务icon
    val serviceDesc: String?,//律师服务文案
    val serviceId: Int?,//律师服务id
    val serviceName: String?,//律师服务名
    val servicePrice: String?,//服务价格中问描述
    val price: Int = 0      //服务价格，单位"元"
)
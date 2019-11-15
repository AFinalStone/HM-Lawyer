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
    val honors: ArrayList<ImageUrlFileIdBean>?,//荣誉资质
    val image: ImageUrlFileIdBean?,//个人形象照
    val info: String?,//个人简介
    val lawFirm: String?,//执业律所
    val location: String?,//地址
    val services: List<LawyerServiceBean>?//律师服务
)




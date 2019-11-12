package com.hm.iou.lawyer.bean.req

/**
 * @author : 借条管家-shilei
 * @version : 0.0.1
 * @Date : 2019-11-12 21:08
 * @E-Mail : afinalstone@foxmail.com
 */
data class LawyerAuthenticationReqBean(
    val licenseNumber: String,//执业证号
    val lawFirm: String,//执业律所
    val holdingDate: String,//持证日期
    val info: String,//个人简介
    val image: String,//个人形象照id
    val authCerts: List<String>,//执业证
    val honors: List<String>//荣誉资质

)
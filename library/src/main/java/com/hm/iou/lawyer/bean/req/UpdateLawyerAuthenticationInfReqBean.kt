package com.hm.iou.lawyer.bean.req

/**
 * @author : 借条管家-shilei
 * @version : 0.0.1
 * @Date : 2019-11-12 21:08
 * @E-Mail : afinalstone@foxmail.com
 */
class UpdateLawyerAuthenticationInfReqBean {
    var licenseNumber: String? = null//执业证号
    var lawFirm: String? = null//执业律所
    var holdingDate: String? = null//持证日期
    var info: String? = null//个人简介
    var image: String? = null//个人形象照id
    var authCerts: List<String>? = null//执业证
    var honors: List<String>? = null//荣誉资质
}
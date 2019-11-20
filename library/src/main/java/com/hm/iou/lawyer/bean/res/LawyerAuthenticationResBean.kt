package com.hm.iou.lawyer.bean.res

/**
 * @author : 借条管家-shilei
 * @version : 0.0.1
 * @Date : 2019-11-12 14:10
 * @E-Mail : afinalstone@foxmail.com
 */
data class LawyerAuthenticationResBean(
    val authCerts: List<ImageUrlFileIdBean>?,
    val holdingDate: String?,
    val honors: List<ImageUrlFileIdBean>?,
    val image: ImageUrlFileIdBean?,
    val info: String?,
    val lawFirm: String?,
    val licenseNumber: String?
)


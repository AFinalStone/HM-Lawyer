package com.hm.iou.lawyer.bean.res

/**
 * @author : 借条管家-shilei
 * @version : 0.0.1
 * @Date : 2019-11-12 14:10
 * @E-Mail : afinalstone@foxmail.com
 */
data class CheckLawyerCanAcceptOrderResBean(
    val note: String?,//提示
    val result: Int?//结果：0=可以接单，1=可以接单，且展示note，2=不可以接单 ，3=不可接单-需要更新年检
)
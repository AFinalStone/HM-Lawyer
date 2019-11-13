package com.hm.iou.lawyer.bean.res

/**
 * @author : 借条管家-shilei
 * @version : 0.0.1
 * @Date : 2019-11-12 14:10
 * @E-Mail : afinalstone@foxmail.com
 */
data class CalcaLawyerWithDrawRateResBean(
    val feePercent: String?,//费率，如 10%
    val serviceFee: String?,//服务费，如  ¥100.00
    val withdrawNum: String?//体现金额，如 ¥1000.00
)
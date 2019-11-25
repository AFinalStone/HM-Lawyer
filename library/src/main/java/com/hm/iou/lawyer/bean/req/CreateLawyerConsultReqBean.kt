package com.hm.iou.lawyer.bean.req
/**
 * Created by hjy on 2019-11-25
 *
 * 创建律师咨询
 */
class CreateLawyerConsultReqBean {

    var appoint: Int = 0        //0=不制定律师，1=制定律师（lawyerId必填）
    var lawyerId: String? = null            //
    var caseDescription: String? = null     //案件描述
    var price: Int = 0
    var fileIds: List<String>? = null
}
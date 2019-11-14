package com.hm.iou.lawyer.bean.req

import com.hm.iou.lawyer.bean.LetterReceiverBean

class CreateLawyerLetterReqBean {

    var appoint: Int = 0        //0=不制定律师，1=制定律师（lawyerId必填）
    var lawyerId: String? = null            //
    var caseDescription: String? = null     //案件描述
    var name: String? = null
    var mobile: String? = null
    var price: Int = 0
    var source: Int = 0     //1=借条，2=app在线律师服务
    var receiverInfoReq: LetterReceiverBean? = null     //收件人信息
    var fileIds: List<String>? = null
}
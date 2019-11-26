package com.hm.iou.lawyer.bean.res

/**
 * @author : 借条管家-shilei
 * @version : 0.0.1
 * @Date : 2019-11-12 14:10
 * @E-Mail : afinalstone@foxmail.com
 */
data class LawyerConsultOrderDetailResBean(
    val appoint: Boolean?,
    val billId: String?,
    val caseDescription: String?,
    val custInfo: LawyerConsultCustInfo?,
    val doDate: String?,
    val fileUrls: List<String>?,
    val lawyerAbout: LawyerInfoAbout?,
    val leftTime: Long?,
    val price: Int?,
    val status: Int?
)

data class LawyerConsultCustInfo(
    val applyDate: String?,//申请时间
    val avatarUrl: String?,//客户头像
    val mobile: String?,//客户手机号
    val name: String?//客户名
)

data class LawyerInfoAbout(
    val image: String?,//个人形象照
    val lawFirm: String?,//执业律所
    val lawYear: String?,//执业年限
    val lawyerId: String?,//律师id
    val name: String?//姓名
)
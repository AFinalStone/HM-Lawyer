package com.hm.iou.lawyer.bean.res

data class LawyerConsultOrderDetailBean(
    val appoint: Boolean?,//是否是邀请律师
    val billId: String?,//单子id
    val caseDescription: String?,//案件描述
    val custInfo: LawyerLetterCustInfo?,//律师函的客户信息
    val doDate: String?,//完成时间或者取消时间
    val fileUrls: List<String>?,//图片资料id列表
    val letterFinishInfo: LawyerLetterFinishInfo?,//律师函完成时填写的信息
    val price: Int?,//报价
    val status: Int?//状态：2待接单，3进行中，4已完成，5已取消，6已拒绝
)

data class LawyerConsultInfo(
    val applyDate: String?,//申请时间
    val avatarUrl: String?,//客户头像
    val mobile: String?,//客户手机号
    val name: String?//客户名
)

data class LawyerConsultFinishInfo(
    val expressName: String?,//快递名
    val expressNumber: String?,//快递单号
    val finishImgs: List<String>?//完成后上传的图片【律师函信息】
)

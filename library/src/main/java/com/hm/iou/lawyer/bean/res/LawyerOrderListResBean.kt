package com.hm.iou.lawyer.bean.res


data class LawyerOrderListResBean(
    val list: List<LawyerOrderItem>?,
    val total: Int?
)

class LawyerOrderItem {
    //律师我的订单，律师函订单，律师待接单列表的字段
    val avatarUrl: String? = null//用户头像
    val name: String? = null//用户姓名
    val billId: String? = null//订单id
    val billType: Int? = null//订单类型：1=律师函，2=律师咨询
    val caseDescription: String? = null//案件描述-用于订单创建订单
    val createTime: String? = null//时间
    val price: Int? = null//报价（单位元）
    val status: Int? = null//状态：2待接单，3进行中，4已完成，5已取消

    //律师我的订单字段
    val relationId: Int? = null//一个律师对一个订单可能有多个记录- 记录id
    val type: Int? = null//类型：1=律师函，2=律师咨询
}
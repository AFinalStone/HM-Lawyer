package com.hm.iou.lawyer.bean.res

class CustOrderListResBean {

    var total: Int = 0
    var list: List<CustOrderItemBean>? = null
}

class CustOrderItemBean {

    var billId: String? = null
    var caseDescription: String? = null
    var createTime: String? = null
    var price: Int = 0              //报价，单位（元）
    var status: Int = 0             //2待接单，3进行中，4已完成，5已取消
    var type: Int = 0               //类型：1-律师函，2-律师咨询

}
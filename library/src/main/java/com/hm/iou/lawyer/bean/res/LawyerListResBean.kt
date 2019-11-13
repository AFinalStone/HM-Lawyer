package com.hm.iou.lawyer.bean.res

class LawyerListResBean {

    var total: Int = 0
    var list: List<LawyerItemBean>? = null
}

class LawyerItemBean {

    var lawyerId: String? = null
    var authName: String? = null
    var lawFirm: String? = null
    var location: String? = null
    var image: String? = null
    var holdingYearCount: Int = 0

}
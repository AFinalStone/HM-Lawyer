package com.hm.iou.lawyer.bean.res

/**
 * Created by hjy on 2019-11-25
 *
 * 律师咨询详情
 */
class LawyerConsultDetailResBean {

    var billId: String? = null
    var canCancel: Boolean = false
    var caseDescription: String? = null
    var doDate: String? = null
    var price: Int = 0
    var status: Int = 0
    var evaluation: LawyerEvaluation? = null        //律师评价
    var fileList: List<FileInfo>? = null
    var lawyerAbout: LawyerAbout? = null            //律师简介信息

}

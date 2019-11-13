package com.hm.iou.lawyer.bean.res
import com.hm.iou.database.table.IouData
import com.hm.iou.lawyer.bean.LetterReceiverBean


class CustLetterDetailResBean {

    var billId: String? = null
    var caseDescription: String? = null
    var doDate: String? = null                      //完成时间或取消时间
    var evaluation: LawyerEvaluation? = null        //律师评价
    var fileList: List<FileInfo>? = null
    var lawyerAbout: LawyerAbout? = null            //律师简介信息
    var letterFinishInfo: LetterFinishInfo? = null
    var mobile: String? = null
    var name: String? = null
    var price: Int = 0
    var receiveInfo: LetterReceiverBean? = null
    var status: Int = 0

}

class FileInfo {
    var id: String? = null
    var url: String? = null
}

class LawyerEvaluation {

    var attitudeScore: Int = 0
    var professionalScore: Int = 0

}

class LawyerAbout {

    var image: String? = null
    var lawFirm: String? = null
    var lawYear: Int = 0
    var lawyerId: String? = null
    var name: String? = null

}

class LetterFinishInfo {

    var expressName: String? = null
    var expressNumber: String? = null
    var finishImgs: List<String>? = null

}
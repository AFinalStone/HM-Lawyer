package com.hm.iou.lawyer.dict

/**
 * Created by syl on 2019/7/25.
 */

enum class UpdateLawFirmStatusType(val status: Int, val des: String) {
    APPLYING(9, "更新执业机构审核中"),
    SUCCESS(10, "更细执业机构审核通过"),
    FAILED(11, "更新执业机构审核不通过")
}

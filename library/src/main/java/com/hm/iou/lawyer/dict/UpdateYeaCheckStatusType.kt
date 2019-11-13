package com.hm.iou.lawyer.dict

/**
 * Created by syl on 2019/7/25.
 */

enum class UpdateYeaCheckStatusType(val status: Int, val des: String) {
    NEED_UPDATE_YEAR_CHECK(5, "需要更新年检信息"),
    APPLYING(6, "更新年检审核中"),
    SUCCESS(7, "更新年检审核通过"),
    FAILED(8, "更新年检审核不通过")
}

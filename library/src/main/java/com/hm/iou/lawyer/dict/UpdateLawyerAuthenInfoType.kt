package com.hm.iou.lawyer.dict

/**
 * 创建律师函来源
 */
enum class UpdateLawyerAuthenInfoType(val type: Int, val desc: String) {

    HEADER(1, "更新【个人照片】"),
    SELF_INTRODUCE(2, "更新【律师介绍】"),
    LAWYER_HONOR(3, "更新【荣誉资质】"),
    LAWYER_FIRM(4, "更新【执业机构】"),
    YEAR_CHECK_INFO(5, "更新【年检信息】")

}
package com.hm.iou.lawyer.business.lawyer.workbench

import com.hm.iou.lawyer.dict.ModelType

/**
 * @author : 借条管家-shilei
 * @version : 0.0.1
 * @Date : 2019-11-08 17:15
 * @E-Mail : afinalstone@foxmail.com
 */
interface IMenuItem {

    var redCount: Int

    /**
     * 菜单icon
     */
    fun getIIcon(): Int

    /**
     * 菜单
     */
    fun getIModel(): ModelType

    /**
     * 红点数量
     */
    fun getIRedDotNum(): String {
        return if (redCount > 9) "N" else redCount.toString()
    }

    /**
     * 是否显示红点
     */
    fun ifShowRedDot(): Boolean

    fun updateRedCount(count: Int) {
        redCount = count
    }
}
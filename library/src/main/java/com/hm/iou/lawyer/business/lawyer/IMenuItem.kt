package com.hm.iou.lawyer.business.lawyer

/**
 * @author : 借条管家-shilei
 * @version : 0.0.1
 * @Date : 2019-11-08 17:15
 * @E-Mail : afinalstone@foxmail.com
 */
interface IMenuItem {

    /**
     * 菜单icon
     */
    fun getIIcon(): Int

    /**
     * 菜单名称
     */
    fun getIName(): String

    /**
     * 红点数量
     */
    fun getIRedDotNum(): String

    /**
     * 是否显示红点
     */
    fun ifShowRedDot(): Boolean
}
package com.hm.iou.lawyer.business.lawyer.workbench

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hm.iou.lawyer.R
import com.hm.iou.uikit.HMDotTextView

/**
 * @author : 借条管家-shilei
 * @version : 0.0.1
 * @Date : 2019-11-08 17:14
 * @E-Mail : afinalstone@foxmail.com
 */
class MenuAdapter :
    BaseQuickAdapter<IMenuItem, BaseViewHolder>(R.layout.lawyer_item_lawyer_workbench_list) {
    override fun convert(helper: BaseViewHolder?, item: IMenuItem?) {
        val itemIcon = item?.getIIcon()
        if (itemIcon != null) {
//            helper?.setImageResource(R.id.iv_icon, itemIcon)
        }
        helper?.setText(R.id.tv_name, item?.getIModel()?.modelName)

        val ifShow = item?.ifShowRedDot()
        if (ifShow == true) {
            helper?.setGone(R.id.dot_num, true)
            val dotRedMsg = helper?.getView<HMDotTextView>(R.id.dot_num)
            dotRedMsg?.setText("3")
        } else {
            helper?.setGone(R.id.dot_num, false)
        }
    }
}
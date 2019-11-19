package com.hm.iou.lawyer.business.user.find

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hm.iou.lawyer.R
import com.hm.iou.tools.kt.clickWithDuration

/**
 * Created by hjy on 2019/11/11
 *
 * 律师年限选择
 */
class LawyerAgeLimitPopWindow(val context: Context) : PopupWindow(context) {

    private val mAdaper: AgeLimitAdapter
    private val mDataList = listOf("不限", "1年-3年", "3年-5年", "5年-10年", "10年以上")
    private var mPosition = 0

    private var mOnAgeLimitListener: OnAgeLimitListener? = null

    private var mArrowView: ImageView? = null   //与之关联的三件指示箭头

    init {
        contentView = LayoutInflater.from(context).inflate(R.layout.lawyer_layout_age_limit, null)
        // 设置SelectPicPopupWindow弹出窗体的宽
        width = ViewGroup.LayoutParams.MATCH_PARENT
        // 设置SelectPicPopupWindow弹出窗体的高
        height = ViewGroup.LayoutParams.WRAP_CONTENT
        // 设置SelectPicPopupWindow弹出窗体可点击
        isFocusable = true
        isOutsideTouchable = true
        // 设置SelectPicPopupWindow弹出窗体动画效果
//        this.animationStyle = R.style.UikitPopupAnimationStyle_FromTop
        // 实例化一个ColorDrawable颜色为半透明
        val dw = ColorDrawable(-0x80000000)
        // 设置SelectPicPopupWindow弹出窗体的背景
        setBackgroundDrawable(dw)

        val recyclerView = contentView.findViewById<RecyclerView>(R.id.rv_search_age_limit)
        recyclerView.layoutManager = LinearLayoutManager(context)
        mAdaper = AgeLimitAdapter()
        recyclerView.adapter = mAdaper
        mAdaper.setNewData(mDataList)
        mAdaper.setOnItemClickListener { _, _, position ->
            dismiss()
            if (mPosition == position) {
                return@setOnItemClickListener
            }
            val item = mDataList[position]
            mPosition = position
            mAdaper.notifyDataSetChanged()
            mOnAgeLimitListener?.let {
                it.onSelected(position, item)
            }
        }

        contentView.findViewById<View>(R.id.view_search_placeholder).clickWithDuration {
            dismiss()
        }
    }

    fun setOnAgeLimitListener(listener: OnAgeLimitListener?) {
        mOnAgeLimitListener = listener
    }

    fun attachArrowView(view: ImageView?) {
        mArrowView = view
    }

    override fun showAsDropDown(anchor: View?) {
        super.showAsDropDown(anchor)
        mArrowView?.let {
            it.setImageResource(R.mipmap.uikit_ic_arrow_top_small)
        }
    }

    override fun dismiss() {
        super.dismiss()
        mArrowView?.let {
            it.setImageResource(R.mipmap.uikit_ic_arrow_bottom_small)
        }
    }

    inner class AgeLimitAdapter :
        BaseQuickAdapter<String, BaseViewHolder>(R.layout.lawyer_item_age_limit) {

        override fun convert(helper: BaseViewHolder?, item: String?) {
            helper?.run {
                val selected = adapterPosition == mPosition
                setText(R.id.tv_age_limit, item)
                if (selected) {
                    setTextColor(R.id.tv_age_limit, 0xFF111111.toInt())
                    setBackgroundColor(R.id.tv_age_limit, 0xFFF8F8F9.toInt())
                } else {
                    setTextColor(R.id.tv_age_limit, 0xFF9B9B9B.toInt())
                    setBackgroundColor(R.id.tv_age_limit, Color.WHITE)
                }
            }
        }
    }

}

interface OnAgeLimitListener {
    fun onSelected(position: Int, item: String)
}
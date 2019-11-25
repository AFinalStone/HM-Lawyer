package com.hm.iou.lawyer.business.lawyer.workbench.order

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.view.Gravity
import android.view.View.GONE
import android.view.View.VISIBLE
import com.hm.iou.base.mvp.HMBaseActivity
import com.hm.iou.lawyer.R
import com.hm.iou.lawyer.bean.res.LawyerConsultOrderDetailBean
import com.hm.iou.lawyer.business.NavigationHelper
import com.hm.iou.lawyer.business.comm.ConsultAnswerAdapter
import com.hm.iou.lawyer.business.comm.IAnswer
import com.hm.iou.lawyer.business.lawyer.home.edit.YearCheckAuthenActivity
import com.hm.iou.lawyer.business.user.CommImageAdapter
import com.hm.iou.lawyer.dict.OrderStatus
import com.hm.iou.tools.ImageLoader
import com.hm.iou.tools.kt.*
import com.hm.iou.uikit.dialog.HMActionSheetDialog
import com.hm.iou.uikit.dialog.HMAlertDialog
import kotlinx.android.synthetic.main.lawyer_activity_lawyer_consult_order_detail.*
import java.text.SimpleDateFormat

/**
 * 律师咨询订单详情
 */
class ConsultDetailActivity : HMBaseActivity<ConsultDetailPresenter>(), ConsultDetailContract.View {

    companion object {
        const val EXTRA_KEY_ORDER_ID = "order_id"
        const val EXTRA_KEY_RELATION_ID = "relation_id"
    }

    /**
     * 订单编号
     */
    private var mOrderId: String? by extraDelegate(
        EXTRA_KEY_ORDER_ID,
        null
    )
    /**
     * 记录id
     */
    private var mRelationId: String? by extraDelegate(
        EXTRA_KEY_RELATION_ID,
        null
    )

    private var mAnswerAdapter: ConsultAnswerAdapter? = null

    override fun initPresenter(): ConsultDetailPresenter = ConsultDetailPresenter(this, this)

    override fun getLayoutId(): Int = R.layout.lawyer_activity_lawyer_consult_order_detail

    override fun initEventAndData(bundle: Bundle?) {
        if (bundle != null) {
            mOrderId = bundle.getValue(EXTRA_KEY_ORDER_ID)
            mRelationId = bundle.getValue(EXTRA_KEY_RELATION_ID)
        }
        initViews()
        val order = mOrderId
        if (order.isNullOrEmpty()) {
            toastErrorMessage("参数异常")
            finish()
            return
        }
        iv_back.clickWithDuration {
            onBackPressed()
        }
        var relationId: Int? = null
        try {
            relationId = mRelationId?.toInt()
        } catch (e: Exception) {

        }
        mPresenter.init(order, relationId)

    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putValue(EXTRA_KEY_ORDER_ID, mOrderId)
        outState?.putValue(EXTRA_KEY_RELATION_ID, mRelationId)
    }

    private fun initViews() {

    }

    override fun showInitView() {

    }

    override fun hideInitView() {

    }

    override fun showInitFailed(msg: String) {

    }

    override fun showDetail(detail: LawyerConsultOrderDetailBean) {
        when (detail.status) {
            OrderStatus.WAIT.status -> {
                showWait(detail)
            }
            OrderStatus.ONGOING.status -> {
                showDonging(detail)
            }
            OrderStatus.COMPLETE.status -> {
                showComplete(detail)
            }
            OrderStatus.CANCEL.status -> {
                rl_order_status.visibility = VISIBLE
                rl_order_operate_time.visibility = VISIBLE
                view_order_status.visibility = VISIBLE
                view_order_operate_time.visibility = VISIBLE
                iv_more.visibility = GONE
                tv_order_status.text = "订单已取消"
                tv_order_operate_label.text = "取消时间"
                val time = detail.doDate?.replace("-", ".")
                tv_order_operate_time.text = time
            }
            OrderStatus.REFUSE.status -> {
                rl_order_status.visibility = VISIBLE
                rl_order_operate_time.visibility = VISIBLE
                view_order_status.visibility = VISIBLE
                view_order_operate_time.visibility = VISIBLE
                iv_more.visibility = GONE
                tv_order_status.text = "已拒绝接单"
                tv_order_operate_label.text = "拒绝时间"
                val time = detail.doDate?.replace("-", ".")
                tv_order_operate_time.text = time
            }
        }
        //头像，昵称，时间
        val custInfo = detail.custInfo
        custInfo?.let {
            ImageLoader.getInstance(mContext).displayImage(
                custInfo.avatarUrl,
                iv_customer_avatar,
                R.mipmap.uikit_icon_header_unknow
            )
            tv_customer_name.text = custInfo.name
            try {
                val sdf01 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                val sdf02 = SimpleDateFormat("yyyy.MM.dd HH:mm")
                val date = sdf01.parse(custInfo.applyDate)
                tv_time.text = sdf02.format(date)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        //报价
        tv_price.text = "¥ ${detail.price}"
        //需求描述
        tv_order_case_desc.text = detail.caseDescription ?: ""
        //图片资料
        val list: List<String>? = null
        if (list.isNullOrEmpty()) {
            view_divider_order_img.visibility = GONE
            tv_order_img_label.visibility = GONE
            rv_order_img.visibility = GONE
        } else {
            view_divider_order_img.visibility = VISIBLE
            tv_order_img_label.visibility = VISIBLE
            rv_order_img.visibility = VISIBLE
            rv_order_img.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            val adapter = CommImageAdapter(this)
            adapter.setNewData(list)
            rv_order_img.adapter = adapter
            adapter.setOnItemClickListener { adapter, _, position ->
                val list = adapter.data as? List<String>
                list?.let {
                    NavigationHelper.toImageGalleryPage(this, list, position)
                }
            }
        }
    }

    override fun showAnswerList(list: ArrayList<IAnswer>) {
        rv_order_answer.layoutManager = LinearLayoutManager(this)
        mAnswerAdapter = ConsultAnswerAdapter(mContext)
        rv_order_answer.adapter = mAnswerAdapter
        mAnswerAdapter?.setNewData(list)
    }

    private fun showWait(detail: LawyerConsultOrderDetailBean) {
        rl_order_status.visibility = VISIBLE
        tv_order_status.text = "等待接单"
        rl_order_operate_time.visibility = GONE
        view_order_status.visibility = GONE
        view_order_operate_time.visibility = GONE
        iv_more.visibility = GONE
        tv_count_down_time.visibility = GONE
        tv_operate_02.visibility = VISIBLE
        tv_operate_02.text = "立即接单"
        if (true == detail.appoint) {
            tv_operate_01.visibility = VISIBLE
            tv_operate_01.text = "拒绝接单"
            tv_operate_01.clickWithDuration {
                val msg = "是否确认拒绝此订单"
                val sp = SpannableString(msg)
                sp.setSpan(AbsoluteSizeSpan(mContext.dp2px(18)), 0, msg.length, 0)
                HMAlertDialog.Builder(mContext)
                    .setMessage(sp)
                    .setMessageGravity(Gravity.CENTER)
                    .setNegativeButton("确认拒绝")
                    .setPositiveButton("考虑一下")
                    .setOnClickListener(object : HMAlertDialog.OnClickListener {
                        override fun onPosClick() {
                        }

                        override fun onNegClick() {
                            mPresenter.resumeOrder()
                        }
                    })
                    .create()
                    .show()
            }
        } else {
            tv_operate_01.visibility = GONE
        }
    }

    private fun showDonging(detail: LawyerConsultOrderDetailBean) {
        rl_order_status.visibility = GONE
        rl_order_operate_time.visibility = GONE
        view_order_status.visibility = GONE
        view_order_operate_time.visibility = GONE
        iv_more.visibility = VISIBLE
        tv_operate_01.visibility = GONE
        tv_operate_02.visibility = VISIBLE
        tv_operate_02.text = "咨询解答"
        iv_more.clickWithDuration {
            showBottomDialog()
        }
        tv_operate_02.clickWithDuration {

        }
    }

    private fun showComplete(detail: LawyerConsultOrderDetailBean) {
        rl_order_status.visibility = VISIBLE
        tv_order_status.text = "订单已完成"
        rl_order_operate_time.visibility = VISIBLE
        tv_order_operate_label.text = "完成时间"
        view_order_status.visibility = VISIBLE
        view_order_operate_time.visibility = VISIBLE
        iv_more.visibility = GONE
        tv_operate_01.visibility = GONE
        tv_operate_02.visibility = VISIBLE
        tv_operate_02.text = "咨询解答"

        val time = detail.doDate?.replace("-", ".")
        tv_order_operate_time.text = time ?: ""
    }

    private fun showBottomDialog() {
        val list = ArrayList<String>()
        list.add("取消订单")
        HMActionSheetDialog.Builder(mContext)
            .setActionSheetList(list)
            .setCanSelected(false)
            .setOnItemClickListener { position, _ ->
                if (0 == position) {
                    val msg = "取消订单后今日将不能接单，是否继续取消？"
                    val sp = SpannableString(msg)
                    sp.setSpan(AbsoluteSizeSpan(mContext.dp2px(18)), 0, msg.length, 0)
                    HMAlertDialog.Builder(mContext)
                        .setMessage(sp)
                        .setPositiveButton("放弃取消")
                        .setNegativeButton("继续取消")
                        .setOnClickListener(object : HMAlertDialog.OnClickListener {
                            override fun onPosClick() {

                            }

                            override fun onNegClick() {
                                mPresenter.cancelOrder()
                            }
                        })
                        .create()
                        .show()
                }
            }
            .create()
            .show()
    }

    override fun showNeedUpdateYearCheckByCanAcceptOrder(msg: String?) {
        msg?.let {
            val sp = SpannableString(msg)
            sp.setSpan(AbsoluteSizeSpan(mContext.dp2px(18)), 0, msg.length, 0)
            HMAlertDialog.Builder(mContext)
                .setMessage(sp)
                .setNegativeButton("更新年检")
                .setPositiveButton("继续接单")
                .setOnClickListener(object : HMAlertDialog.OnClickListener {
                    override fun onNegClick() {
                        startActivity<YearCheckAuthenActivity>()
                    }

                    override fun onPosClick() {
                        mPresenter.acceptOrder()
                    }
                })
                .create()
                .show()
        }

    }

    override fun showNeedUpdateYearCheckByNotCanAcceptOrder(msg: String?) {
        msg?.let {
            val sp = SpannableString(msg)
            sp.setSpan(AbsoluteSizeSpan(mContext.dp2px(18)), 0, msg.length, 0)
            HMAlertDialog.Builder(mContext)
                .setMessage(sp)
                .setNegativeButton("稍后更新")
                .setPositiveButton("立即更新")
                .setOnClickListener(object : HMAlertDialog.OnClickListener {
                    override fun onNegClick() {
                    }

                    override fun onPosClick() {
                        startActivity<YearCheckAuthenActivity>()
                    }
                })
                .create()
                .show()
        }
    }

    override fun showNotCanAcceptOrder(msg: String?) {
        msg?.let {
            val sp = SpannableString(msg)
            sp.setSpan(AbsoluteSizeSpan(mContext.dp2px(18)), 0, msg.length, 0)
            HMAlertDialog.Builder(mContext)
                .setMessage(sp)
                .setPositiveButton("知道了")
                .create()
                .show()
        }
    }

}
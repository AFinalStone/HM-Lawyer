package com.hm.iou.lawyer.business.lawyer.workbench.order

import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.LinearLayoutManager
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.view.Gravity
import android.view.View
import android.view.View.*
import com.hm.iou.base.mvp.HMBaseActivity
import com.hm.iou.lawyer.R
import com.hm.iou.lawyer.bean.res.LawyerConsultOrderDetailResBean
import com.hm.iou.lawyer.business.NavigationHelper
import com.hm.iou.lawyer.business.comm.ConsultAnswerAdapter
import com.hm.iou.lawyer.business.comm.IAnswer
import com.hm.iou.lawyer.business.lawyer.home.edit.YearCheckAuthenActivity
import com.hm.iou.lawyer.business.user.CommImageAdapter
import com.hm.iou.lawyer.dict.OrderStatus
import com.hm.iou.sharedata.UserManager
import com.hm.iou.tools.ImageLoader
import com.hm.iou.tools.kt.*
import com.hm.iou.uikit.dialog.HMActionSheetDialog
import com.hm.iou.uikit.dialog.HMAlertDialog
import kotlinx.android.synthetic.main.lawyer_activity_lawyer_consult_order_detail.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.DecimalFormat
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
    private var mJobTimeCount: Job? = null

    override fun initPresenter(): ConsultDetailPresenter = ConsultDetailPresenter(this, this)

    override fun getLayoutId(): Int = R.layout.lawyer_activity_lawyer_consult_order_detail

    override fun initEventAndData(bundle: Bundle?) {
        if (bundle != null) {
            mOrderId = bundle.getValue(EXTRA_KEY_ORDER_ID)
            mRelationId = bundle.getValue(EXTRA_KEY_RELATION_ID)
        }
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

    override fun onResume() {
        super.onResume()
        mPresenter.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putValue(EXTRA_KEY_ORDER_ID, mOrderId)
        outState?.putValue(EXTRA_KEY_RELATION_ID, mRelationId)
    }


    override fun showInitView() {
        loading_view.visibility = VISIBLE
        loading_view.showDataLoading()
    }

    override fun hideInitView() {
        loading_view.visibility = INVISIBLE
        loading_view.stopLoadingAnim()
    }

    override fun showInitFailed(msg: String) {
        loading_view.visibility = VISIBLE
        loading_view.showDataFail(msg) {
            mOrderId?.let {
                var relationId: Int? = null
                try {
                    relationId = mRelationId?.toInt()
                } catch (e: Exception) {

                }
                mPresenter.init(it, relationId)
            }
        }
    }

    override fun showDetail(detail: LawyerConsultOrderDetailResBean) {
        when (detail.status) {
            OrderStatus.WAIT.status -> {
                showWait(detail)
            }
            OrderStatus.ONGOING.status -> {
                showDonging(detail)
            }
            OrderStatus.COMPLETE.status -> {
                showComplete(detail)
                mPresenter.getAnswerList()
            }
            OrderStatus.CANCEL.status -> {
                //订单状态
                tv_order_status.text = "订单已取消"
                rl_order_operate.visibility = VISIBLE
                tv_order_operate_label.text = "取消时间"
                try {
                    val sdf01 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    val sdf02 = SimpleDateFormat("yyyy.MM.dd HH:mm")
                    val date = sdf01.parse(detail.doDate)
                    tv_order_operate_time.text = sdf02.format(date)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                //底部栏
                tv_count_down_time.visibility = GONE
                iv_more.visibility = GONE
                tv_operate_01.visibility = GONE
                tv_operate_02.visibility = GONE
                //律师解答列表
                rl_lawyer_answer.visibility = GONE
                ll_order_answer.visibility = GONE
            }
            OrderStatus.REFUSE.status -> {
                //订单状态
                tv_order_status.text = "已拒绝接单"
                rl_order_operate.visibility = VISIBLE
                tv_order_operate_label.text = "拒绝时间"
                try {
                    val sdf01 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    val sdf02 = SimpleDateFormat("yyyy.MM.dd HH:mm")
                    val date = sdf01.parse(detail.doDate)
                    tv_order_operate_time.text = sdf02.format(date)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                //底部栏
                tv_count_down_time.visibility = GONE
                iv_more.visibility = GONE
                tv_operate_01.visibility = GONE
                tv_operate_02.visibility = GONE
                //律师解答列表
                rl_lawyer_answer.visibility = GONE
                ll_order_answer.visibility = GONE
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
        val list: List<String>? = detail.fileUrls
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

    override fun showAnswerList(list: List<IAnswer>) {
        if (mAnswerAdapter == null) {
            rv_order_answer.layoutManager = LinearLayoutManager(this)
            mAnswerAdapter = ConsultAnswerAdapter(mContext)
            rv_order_answer.adapter = mAnswerAdapter
            rv_order_answer.isNestedScrollingEnabled = false
        }
        mAnswerAdapter?.setNewData(list)
    }


    override fun scrollToBottom() {
        nsv_order_content.fullScroll(NestedScrollView.FOCUS_DOWN)
    }

    override fun hideAnswerListLoadingView() {
        loading_order_answer.visibility = GONE
        rv_order_answer.visibility = VISIBLE
    }

    override fun showAnswerListLoadingView() {
        loading_order_answer.visibility = VISIBLE
        loading_order_answer.showDataLoading()
        rv_order_answer.visibility = GONE
    }

    override fun showAnswerListFailed(msg: String?) {
        loading_order_answer.visibility = VISIBLE
        rv_order_answer.visibility = GONE
        loading_order_answer.showDataFail(msg) {
            mPresenter.getAnswerList()
        }
    }

    private fun showWait(detail: LawyerConsultOrderDetailResBean) {
        //订单状态
        tv_order_status.text = "等待接单"
        rl_order_operate.visibility = GONE
        view_order_operate.visibility = GONE
        //底部栏
        tv_count_down_time.visibility = GONE
        iv_more.visibility = GONE
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
        tv_operate_02.visibility = VISIBLE
        tv_operate_02.text = "立即接单"
        tv_operate_02.clickWithDuration {
            mPresenter.checkCanAcceptOrder()
        }
        //律师解答列表
        rl_lawyer_answer.visibility = GONE
        ll_order_answer.visibility = GONE
    }

    private fun showDonging(detail: LawyerConsultOrderDetailResBean) {
        //订单状态
        tv_order_status.text = "等待解答"
        rl_order_operate.visibility = GONE
        view_order_operate.visibility = GONE
        //底部栏
        tv_count_down_time.visibility = VISIBLE
        iv_more.visibility = VISIBLE
        iv_more.clickWithDuration {
            showBottomDialog()
        }
        tv_operate_01.visibility = GONE
        tv_operate_02.visibility = VISIBLE
        tv_operate_02.text = "咨询解答"
        tv_operate_02.clickWithDuration {
            val intent = Intent(mContext, InputLawyerConsultAnswerActivity::class.java)
            intent.putExtra(InputLawyerConsultAnswerActivity.EXTRA_KEY_ORDER_ID, mOrderId)
            intent.putExtra(InputLawyerConsultAnswerActivity.EXTRA_KEY_MIN_LENGTH, 10)
            startActivity(intent)
        }
        //律师解答列表
        rl_lawyer_answer.visibility = VISIBLE
        ll_order_answer.visibility = GONE
        val lawyerInfoAbout = detail.lawyerAbout
        tv_lawyer_name.text = lawyerInfoAbout?.name
        ImageLoader.getInstance(mContext)
            .displayImage(
                lawyerInfoAbout?.image,
                iv_lawyer_avatar,
                R.mipmap.uikit_icon_header_unknow
            )
        if (mJobTimeCount != null) {
            mJobTimeCount?.cancel()
        }
        mJobTimeCount = launch {
            var time = detail.leftTime ?: 0
            while (time > 0) {
                tv_count_down_time.text = formatTime(time)
                delay(1000)
                time -= 1
            }
            tv_count_down_time.visibility = GONE
        }
    }

    private fun formatTime(time: Long): String {
        val hour = time / 3600
        val remainderTime = time % 3600
        val minute = remainderTime / 60
        val second = remainderTime % 60
        val df = DecimalFormat("00")
        return "请在%S:%S:%S秒内进行解答".format(df.format(hour), df.format(minute), df.format(second))
    }

    private fun showComplete(detail: LawyerConsultOrderDetailResBean) {
        //订单状态
        tv_order_status.text = "订单已完成"
        rl_order_operate.visibility = VISIBLE
        tv_order_operate_label.text = "完成时间"
        val time = detail.doDate?.replace("-", ".")
        tv_order_operate_time.text = time
        //底部栏
        tv_count_down_time.visibility = GONE
        iv_more.visibility = GONE
        iv_more.clickWithDuration {
            showBottomDialog()
        }
        tv_operate_01.visibility = GONE
        tv_operate_02.visibility = VISIBLE
        tv_operate_02.text = "咨询解答"
        tv_operate_02.clickWithDuration {
            val intent = Intent(mContext, InputLawyerConsultAnswerActivity::class.java)
            intent.putExtra(InputLawyerConsultAnswerActivity.EXTRA_KEY_ORDER_ID, mOrderId)
            intent.putExtra(InputLawyerConsultAnswerActivity.EXTRA_KEY_MIN_LENGTH, 2)
            startActivity(intent)
        }
        //解答列表
        rl_lawyer_answer.visibility = GONE
        ll_order_answer.visibility = VISIBLE
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
package com.hm.iou.lawyer.business.lawyer.workbench.order

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.View
import android.view.View.*
import com.hm.iou.base.mvp.HMBaseActivity
import com.hm.iou.lawyer.R
import com.hm.iou.lawyer.bean.res.LawyerLetterDetailBean
import com.hm.iou.lawyer.business.NavigationHelper
import com.hm.iou.lawyer.business.user.CommImageAdapter
import com.hm.iou.lawyer.dict.OrderStatus
import com.hm.iou.tools.ImageLoader
import com.hm.iou.tools.kt.clickWithDuration
import com.hm.iou.tools.kt.extraDelegate
import com.hm.iou.tools.kt.getValue
import com.hm.iou.tools.kt.putValue
import com.hm.iou.uikit.dialog.HMActionSheetDialog
import com.hm.iou.uikit.dialog.HMAlertDialog
import kotlinx.android.synthetic.main.lawyer_activity_lawyer_order_detail.*
import java.text.SimpleDateFormat

/**
 * 我的钱包
 */
class OrderDetailActivity : HMBaseActivity<OrderDetailPresenter>(),
    OrderDetailContract.View {

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
    private var mRelationId: Int? by extraDelegate(
        EXTRA_KEY_RELATION_ID,
        null
    )

    override fun getLayoutId(): Int = R.layout.lawyer_activity_lawyer_order_detail

    override fun initPresenter(): OrderDetailPresenter =
        OrderDetailPresenter(this, this)

    override fun initEventAndData(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            mOrderId = savedInstanceState.getValue(EXTRA_KEY_ORDER_ID)
            mRelationId = savedInstanceState.getValue(EXTRA_KEY_RELATION_ID)
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
        mPresenter.init(order, mRelationId)
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
                mPresenter.init(it, mRelationId)
            }
        }
    }


    @SuppressLint("SetTextI18n")
    override fun showDetail(detail: LawyerLetterDetailBean) {
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
                ll_order_receiver_info.visibility = VISIBLE
                view_order_status.visibility = VISIBLE
                view_order_operate_time.visibility = VISIBLE
                ll_mail_info.visibility = GONE
                iv_more.visibility = GONE
                tv_operate_01.visibility = GONE
                tv_operate_02.visibility = GONE
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
                ll_order_receiver_info.visibility = GONE
                ll_mail_info.visibility = GONE
                iv_more.visibility = GONE
                tv_operate_01.visibility = GONE
                tv_operate_02.visibility = GONE
                tv_order_status.text = "订单已拒绝"
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
        val list = detail.fileUrls
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
        val receiveInfo = detail.receiveInfo
        receiveInfo?.let {
            //收件人信息
            tv_order_receiver_name.text = receiveInfo.receiverName
            tv_order_receiver_mobile.text = receiveInfo.receiverMobile
            tv_order_receiver_addr.text = receiveInfo.receiverDetailAddress
        }

    }

    private fun showWait(detail: LawyerLetterDetailBean) {
        rl_order_status.visibility = GONE
        rl_order_operate_time.visibility = GONE
        view_order_status.visibility = GONE
        view_order_operate_time.visibility = GONE
        ll_order_receiver_info.visibility = GONE
        ll_mail_info.visibility = GONE
        iv_more.visibility = GONE
        if (true == detail.appoint) {
            tv_operate_01.visibility = VISIBLE
            tv_operate_01.text = "拒绝接单"
            tv_operate_01.clickWithDuration {
                HMAlertDialog.Builder(mContext)
                    .setMessage("是否确认拒绝此订单")
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
            }
        } else {
            tv_operate_01.visibility = GONE
        }
        tv_operate_02.visibility = VISIBLE
        tv_operate_02.text = "立即接单"

        tv_operate_02.clickWithDuration {
            mPresenter.acceptOrder()
        }
    }

    private fun showDonging(detail: LawyerLetterDetailBean) {
        rl_order_status.visibility = GONE
        rl_order_operate_time.visibility = GONE
        view_order_status.visibility = GONE
        view_order_operate_time.visibility = GONE
        ll_order_receiver_info.visibility = GONE
        ll_mail_info.visibility = GONE
        iv_more.visibility = VISIBLE
        tv_operate_01.visibility = VISIBLE
        tv_operate_02.visibility = VISIBLE
        tv_operate_01.text = "联系TA"
        tv_operate_02.text = "订单完成"
        iv_more.clickWithDuration {
            showBottomDialog()
        }
        tv_operate_01.clickWithDuration {
            val phone = detail.custInfo?.mobile
            phone?.let {
                HMAlertDialog.Builder(mContext)
                    .setMessage(phone)
                    .setMessageGravity(Gravity.CENTER)
                    .setNegativeButton("取消")
                    .setPositiveButton("呼叫")
                    .setOnClickListener(object : HMAlertDialog.OnClickListener {
                        override fun onPosClick() {
                            val intent = Intent(Intent.ACTION_DIAL)
                            val data = Uri.parse("tel:$phone")
                            intent.data = data
                            startActivity(intent)
                        }

                        override fun onNegClick() {
                        }
                    })
                    .create().show()
            }
        }
        tv_operate_02.clickWithDuration {
            val intent = Intent(mContext, LawyerFinishOrderActivity::class.java)
            intent.putExtra(LawyerFinishOrderActivity.EXTRA_KEY_ORDER_ID, mOrderId ?: "")
            startActivity(intent)
        }
    }

    private fun showComplete(detail: LawyerLetterDetailBean) {
        rl_order_status.visibility = VISIBLE
        rl_order_operate_time.visibility = VISIBLE
        view_order_status.visibility = VISIBLE
        view_order_operate_time.visibility = VISIBLE
        ll_order_receiver_info.visibility = VISIBLE
        ll_mail_info.visibility = VISIBLE
        iv_more.visibility = GONE
        tv_operate_01.visibility = GONE
        tv_operate_02.visibility = GONE
        tv_order_status.text = "订单已完成"
        tv_order_operate_label.text = "完成时间"
        val time = detail.doDate?.replace("-", ".")
        tv_order_operate_time.text = time ?: ""
        val finishInfo = detail.letterFinishInfo
        finishInfo?.let {
            tv_mail_company_name.text = it.expressName
            tv_mail_numbr.text = it.expressNumber
            val list = it.finishImgs
            if (list.isNullOrEmpty()) {
                view_divider_letter.visibility = GONE
                tv_letter_label.visibility = GONE
                rv_letter_image.visibility = GONE
            } else {
                view_divider_letter.visibility = VISIBLE
                tv_letter_label.visibility = VISIBLE
                rv_letter_image.visibility = VISIBLE
                rv_letter_image.layoutManager =
                    LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                val adapter = CommImageAdapter(this)
                adapter.setNewData(it.finishImgs)
                rv_letter_image.adapter = adapter
                adapter.setOnItemClickListener { adapter, _, position ->
                    val list = adapter.data as? List<String>
                    list?.let {
                        NavigationHelper.toImageGalleryPage(this, list, position)
                    }
                }
            }
        }
    }

    private fun showBottomDialog() {
        val list = ArrayList<String>()
        list.add("取消订单")
        HMActionSheetDialog.Builder(mContext)
            .setActionSheetList(list)
            .setCanSelected(false)
            .setOnItemClickListener { position, _ ->
                if (0 == position) {
                    HMAlertDialog.Builder(mContext)
                        .setMessage("取消订单后今日将不能接单，是否继续取消？")
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

}
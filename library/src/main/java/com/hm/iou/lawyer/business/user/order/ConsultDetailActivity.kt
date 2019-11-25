package com.hm.iou.lawyer.business.user.order

import android.os.Bundle
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.LinearLayoutManager
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.view.Gravity
import android.view.View
import com.hm.iou.base.mvp.HMBaseActivity
import com.hm.iou.lawyer.R
import com.hm.iou.lawyer.business.NavigationHelper
import com.hm.iou.lawyer.business.user.CommImageAdapter
import com.hm.iou.tools.ImageLoader
import com.hm.iou.tools.kt.clickWithDuration
import com.hm.iou.tools.kt.dp2px
import com.hm.iou.tools.kt.extraDelegate
import com.hm.iou.uikit.dialog.HMAlertDialog
import kotlinx.android.synthetic.main.lawyer_activity_user_consult_detail.*

/**
 * Created by hjy on 2019-11-22
 *
 * 律师咨询详情
 */
class ConsultDetailActivity : HMBaseActivity<ConsultDetailPresenter>(), ConsultDetailContract.View {

    companion object {
        const val EXTRA_KEY_ORDER_ID = "order_id"
    }

    private var mOrderId: String? by extraDelegate(EXTRA_KEY_ORDER_ID, null)

    private var mImageAdapter = CommImageAdapter(this)
    private var mAnswerAdapter = ConsultAnswerAdapter(this)

    override fun initPresenter(): ConsultDetailPresenter = ConsultDetailPresenter(this, this)

    override fun getLayoutId(): Int = R.layout.lawyer_activity_user_consult_detail

    override fun initEventAndData(savedInstanceState: Bundle?) {
        savedInstanceState?.let {
            mOrderId = it.getString(mOrderId)
        }
        initViews()
        mPresenter.getOrderDetail(mOrderId ?: "")
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putString(EXTRA_KEY_ORDER_ID, mOrderId)
    }

    private fun initViews() {
        rv_order_img.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rv_order_img.adapter = mImageAdapter
        mImageAdapter.setOnItemClickListener { adapter, _, position ->
            val list = adapter.data as? List<String>
            list?.let {
                NavigationHelper.toImageGalleryPage(this, list, position)
            }
        }

        rv_order_answer.layoutManager = LinearLayoutManager(this)
        rv_order_answer.adapter = mAnswerAdapter
        rv_order_answer.isNestedScrollingEnabled = false
    }

    override fun showInitLoading(show: Boolean) {
        if (show) {
            loading_view.showDataLoading()
            nsv_order_content.visibility = View.GONE
        } else {
            loading_view.stopLoadingAnim()
            loading_view.visibility = View.GONE
            nsv_order_content.visibility = View.VISIBLE
        }
    }

    override fun showInitFail(error: String?) {
        loading_view.showDataFail(error) {
            mPresenter.getOrderDetail(mOrderId ?: "")
        }
    }

    override fun showOrderStatus(statusStr: String?) {
        tv_order_status.text = statusStr
    }

    override fun showOrderCancelTips(visibility: Int, returnMoneyTips: String?) {
        tv_order_return_tips.visibility = visibility
        tv_order_return_tips.text = returnMoneyTips
    }

    override fun showOrderTime(timeLabel: String?, orderTime: String?) {
        tv_order_label.text = timeLabel
        tv_order_time.text = orderTime
    }

    override fun showOrderPrice(priceStr: String?) {
        tv_order_price.text = priceStr
    }

    override fun showOrderDesc(desc: String?) {
        tv_order_case_desc.text = desc
    }

    override fun showOrderImages(list: List<String>?) {
        if (list.isNullOrEmpty()) {
            view_divider_order_img.visibility = View.GONE
            tv_order_img_label.visibility = View.GONE
            rv_order_img.visibility = View.GONE
        } else {
            view_divider_order_img.visibility = View.VISIBLE
            tv_order_img_label.visibility = View.VISIBLE
            rv_order_img.visibility = View.VISIBLE
            mImageAdapter.setNewData(list)
        }
    }

    override fun showOrHideAnswerContentView(visibility: Int) {
        ll_order_answer_content.visibility = visibility
    }

    override fun showLawyerStatus(status: String?) {
        tv_order_accept_label.text = status
    }

    override fun showLawyerInfo(
        avatar: String?,
        name: String?,
        ageLimit: String?,
        company: String?
    ) {
        ImageLoader.getInstance(this).displayImage(
            avatar, iv_lawyer_avatar,
            R.mipmap.uikit_icon_header_unknow,
            R.mipmap.uikit_icon_header_unknow
        )
        tv_lawyer_name.text = name
        tv_lawyer_age_limit.text = ageLimit
        tv_lawyer_company.text = company

        rl_order_lawyer_info.clickWithDuration {
            mPresenter.toSeeLawyerInfo()
        }
    }

    override fun showLawyerAnswerLabel(visibility: Int) {
        tv_order_answer_label.visibility = visibility
    }

    override fun showLawyerAnswerList(list: List<IAnswer>?) {
        mAnswerAdapter.setNewData(list)
    }

    override fun showOrHideServiceRatingView(show: Boolean) {
        ll_order_rating.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun showServiceAttitudeRating(rating: Int) {
        rating_order_attitude.setRating(rating)
    }

    override fun showServiceProfessionalRating(rating: Int) {
        rating_order_professional.setRating(rating)
    }

    override fun showOrHideMainBtn(show: Boolean) {
        bottomBar.setTitleGone(show)
    }

    override fun showBottomMainBtn(btnText: String, callback: () -> Unit) {
        bottomBar.updateTitle(btnText)
        bottomBar.setOnTitleClickListener {
            callback()
        }
    }

    override fun showSecondBtn(btnText: String, callback: () -> Unit) {
        bottomBar.showSecondButton(btnText) {
            callback()
        }
    }

    override fun showCommDialog(
        title: String?,
        msg: String?,
        posBtn: String?,
        negBtn: String?,
        callback: (pos: Boolean) -> Unit
    ) {
        msg?.let {
            val sp = SpannableString(msg)
            sp.setSpan(AbsoluteSizeSpan(mContext.dp2px(18)), 0, msg.length, 0)
            HMAlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(sp)
                .setMessageGravity(Gravity.CENTER)
                .setPositiveButton(posBtn)
                .setNegativeButton(negBtn)
                .setOnClickListener(object : HMAlertDialog.OnClickListener {
                    override fun onNegClick() {
                        callback(false)
                    }

                    override fun onPosClick() {
                        callback(true)
                    }
                })
                .create().show()
        }
    }

    override fun scrollToBottom() {
        nsv_order_content.fullScroll(NestedScrollView.FOCUS_DOWN)
    }

}
package com.hm.iou.lawyer.business.user.order

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.View
import com.hm.iou.base.mvp.HMBaseActivity
import com.hm.iou.lawyer.R
import com.hm.iou.lawyer.business.NavigationHelper
import com.hm.iou.lawyer.business.user.CommImageAdapter
import com.hm.iou.tools.ImageLoader
import com.hm.iou.tools.kt.clickWithDuration
import com.hm.iou.tools.kt.extraDelegate
import com.hm.iou.uikit.HMTopBarView
import com.hm.iou.uikit.dialog.HMAlertDialog
import kotlinx.android.synthetic.main.lawyer_activity_user_order_detail.*
import kotlinx.android.synthetic.main.lawyer_activity_user_order_detail.iv_lawyer_avatar
import kotlinx.android.synthetic.main.lawyer_activity_user_order_detail.tv_lawyer_age_limit
import kotlinx.android.synthetic.main.lawyer_activity_user_order_detail.tv_lawyer_company
import kotlinx.android.synthetic.main.lawyer_activity_user_order_detail.tv_lawyer_name

/**
 * Created by hjy on 2019/11/12
 *
 * 我的订单详情页面
 */
class MyOrderDetailActivity : HMBaseActivity<MyOrderDetailPresenter>(), MyOrderDetailContract.View {

    companion object {
        const val EXTRA_KEY_ORDER_ID = "order_id"
    }

    private var mOrderId: String? by extraDelegate(EXTRA_KEY_ORDER_ID, null)

    override fun getLayoutId(): Int = R.layout.lawyer_activity_user_order_detail

    override fun initPresenter(): MyOrderDetailPresenter = MyOrderDetailPresenter(this, this)

    override fun initEventAndData(savedInstanceState: Bundle?) {
        savedInstanceState?.let {
            mOrderId = it.getString(mOrderId)
        }
        mPresenter.getOrderDetail(mOrderId ?: "")
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putString(EXTRA_KEY_ORDER_ID, mOrderId)
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

    override fun showOrderPrice(priceStr: String?) {
        tv_order_price.text = priceStr
    }

    override fun showOrHideTimeView(show: Boolean) {
        val visibility = if (show) View.VISIBLE else View.GONE
        rl_order_cancel_time.visibility = visibility
        view_divider_cancel_time.visibility = visibility
        tv_order_return_tips.visibility = visibility
    }

    override fun showOrderCancelTime(time: String?, returnMoneyTips: String?) {
        tv_order_cancel_label.text = "取消时间"
        tv_order_cancel_time.text = time
        tv_order_return_tips.text = returnMoneyTips
    }

    override fun showOrderCompleteTime(time: String?) {
        tv_order_cancel_label.text = "完成时间"
        tv_order_cancel_time.text = time
        tv_order_return_tips.visibility = View.GONE
    }

    override fun showOrHideLawyerInfoView(show: Boolean) {
        rl_lawyer_info.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun showLawyerInfo(
        avatar: String?,
        name: String?,
        ageLimit: String?,
        company: String?
    ) {
        ImageLoader.getInstance(this).displayImage(
            avatar,
            iv_lawyer_avatar,
            R.mipmap.uikit_icon_header_unknow,
            R.mipmap.uikit_icon_header_unknow
        )
        tv_lawyer_name.text = name
        tv_lawyer_age_limit.text = ageLimit
        tv_lawyer_company.text = company

        rl_lawyer_info.clickWithDuration {
            mPresenter.toSeeLawyerInfo()
        }
    }

    override fun showLetterReceiverInfo(
        name: String?,
        mobile: String?,
        idNo: String?,
        address: String?
    ) {
        tv_order_receiver_name.text = name
        tv_order_receiver_mobile.text = mobile
        if (idNo.isNullOrEmpty()) {
            tv_order_receiver_idno.visibility = View.GONE
        } else {
            tv_order_receiver_idno.visibility = View.VISIBLE
            tv_order_receiver_idno.text = "身份证号码 $idNo"
        }
        tv_order_receiver_addr.text = address
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

    override fun showOrHideExpressInfoView(show: Boolean) {
        ll_order_express_info.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun showExpressName(name: String?) {
        tv_order_express_name.text = name
    }

    override fun showExpressNo(no: String?) {
        tv_order_express_no.text = no
    }

    override fun showExpressImg(list: List<String>?) {
        rv_order_express_img.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val adapter = CommImageAdapter(this)
        adapter.setNewData(list)
        rv_order_express_img.adapter = adapter
        adapter.setOnItemClickListener { adapter, _, position ->
            val list = adapter.data as? List<String>
            list?.let {
                NavigationHelper.toImageGalleryPage(this, list, position)
            }
        }
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

    override fun showOrHideBottomBtn(show: Boolean) {
        bottomBar.setTitleVisible(show)
    }

    override fun showBottomBtn(btnText: String, callback: () -> Unit) {
        bottomBar.updateTitle(btnText)
        bottomBar.setOnTitleClickListener {
            callback()
        }
    }

    override fun showTopBarMenu(menuText: String, callback: () -> Unit) {
        topBar.setRightText(menuText)
        topBar.setOnMenuClickListener(object : HMTopBarView.OnTopBarMenuClickListener {
            override fun onClickTextMenu() {
                callback()
            }

            override fun onClickImageMenu() {
            }
        })
    }

    override fun showCommDialog(
        title: String?,
        msg: String?,
        posBtn: String?,
        negBtn: String?,
        callback: (pos: Boolean) -> Unit
    ) {
        HMAlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(msg)
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
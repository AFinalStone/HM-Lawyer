package com.hm.iou.lawyer.business.user.order

import android.os.Bundle
import com.hm.iou.base.comm.HMTextChangeListener
import com.hm.iou.base.mvp.BaseContract
import com.hm.iou.base.mvp.HMBaseActivity
import com.hm.iou.base.mvp.HMBasePresenter
import com.hm.iou.base.utils.RouterUtil
import com.hm.iou.lawyer.R
import com.hm.iou.lawyer.api.LawyerApi
import com.hm.iou.lawyer.event.RatingLawyerSuccEvent
import com.hm.iou.tools.kt.extraDelegate
import com.hm.iou.uikit.HMTopBarView
import kotlinx.android.synthetic.main.lawyer_activity_rating_lawyer.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import kotlin.Exception

/**
 * Created by hjy on 2019/11/13
 *
 * 律师评价
 */
class RatingLawyerActivity : HMBaseActivity<HMBasePresenter<BaseContract.BaseView>>() {

    companion object {
        const val EXTRA_KEY_ORDER_ID = "order_id"
    }

    private var mOrderId: String? by extraDelegate(EXTRA_KEY_ORDER_ID, null)

    override fun getLayoutId(): Int = R.layout.lawyer_activity_rating_lawyer

    override fun initPresenter(): HMBasePresenter<BaseContract.BaseView> =
        HMBasePresenter(this, this)

    override fun initEventAndData(savedInstanceState: Bundle?) {
        savedInstanceState?.let {
            mOrderId = it.getString(mOrderId)
        }

        et_rating_desc.addTextChangedListener(object : HMTextChangeListener() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                bottomBar.isEnabled = et_rating_desc.text.trim().length >= 5
            }
        })

        topBar.setOnMenuClickListener(object : HMTopBarView.OnTopBarMenuClickListener {
            override fun onClickTextMenu() {
                RouterUtil.toSubmitFeedback(this@RatingLawyerActivity, "Lawyer", "Report")
            }

            override fun onClickImageMenu() {

            }
        })

        bottomBar.setOnTitleClickListener {
            val desc = et_rating_desc.text.trim().toString()
            val r1 = rating_order_attitude.rating
            val r2 = rating_order_professional.rating
            submitRating(desc, r1, r2)
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putString(EXTRA_KEY_ORDER_ID, mOrderId)
    }

    private fun submitRating(desc: String, r1: Int, r2: Int) {
        launch {
            showLoadingView()
            try {
                mPresenter.handleResponse(LawyerApi.ratingLawyer(mOrderId ?: "", desc, r1, r2))
                dismissLoadingView()
                toastMessage("评价成功")
                EventBus.getDefault().register(RatingLawyerSuccEvent(mOrderId))
                finish()
            } catch (e: Exception) {
                dismissLoadingView()
                mPresenter.handleException(e)
            }
        }
    }

}
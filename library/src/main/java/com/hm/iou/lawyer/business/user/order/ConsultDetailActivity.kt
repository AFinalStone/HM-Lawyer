package com.hm.iou.lawyer.business.user.order

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.hm.iou.base.mvp.HMBaseActivity
import com.hm.iou.lawyer.R
import kotlinx.android.synthetic.main.lawyer_activity_user_consult_detail.*

/**
 * Created by hjy on 2019-11-22
 *
 * 律师咨询详情
 */
class ConsultDetailActivity: HMBaseActivity<ConsultDetailPresenter>(), ConsultDetailContract.View {

    override fun initPresenter(): ConsultDetailPresenter = ConsultDetailPresenter(this, this)

    override fun getLayoutId(): Int = R.layout.lawyer_activity_user_consult_detail

    override fun initEventAndData(savedInstanceState: Bundle?) {
        initViews()
    }

    private fun initViews() {
        rv_order_answer.layoutManager = LinearLayoutManager(this)

        val adapter = ConsultAnswerAdapter()
        rv_order_answer.adapter = adapter
        val list = mutableListOf<IAnswer>()
        for (i in 0..30) {
            list.add(object : IAnswer{

            })
        }
        adapter.setNewData(list)
    }

}
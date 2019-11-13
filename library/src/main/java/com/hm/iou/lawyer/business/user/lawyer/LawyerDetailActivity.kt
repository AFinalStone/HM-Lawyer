package com.hm.iou.lawyer.business.user.lawyer

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hm.iou.base.mvp.HMBaseActivity
import com.hm.iou.lawyer.R
import com.hm.iou.lawyer.bean.res.LawyerServiceBean
import com.hm.iou.lawyer.business.NavigationHelper
import com.hm.iou.lawyer.business.user.CommImageAdapter
import com.hm.iou.tools.ImageLoader
import com.hm.iou.tools.kt.*
import com.hm.iou.uikit.HMGrayDividerItemDecoration
import kotlinx.android.synthetic.main.lawyer_activity_lawyer_detail_info.*
import kotlinx.android.synthetic.main.lawyer_layout_layout_service.*

/**
 * Created by hjy on 2019/11/12
 *
 * 用户查看律师详情页
 *
 */
class LawyerDetailActivity : HMBaseActivity<LawyerDetailPresenter>(), LawyerDetailContract.View {

    companion object {
        const val EXTRA_KEY_LAWYER_ID = "lawyer_id"
    }

    private var mLawyerId: String? by extraDelegate(EXTRA_KEY_LAWYER_ID, null)

    override fun initPresenter(): LawyerDetailPresenter = LawyerDetailPresenter(this, this)

    override fun getLayoutId(): Int = R.layout.lawyer_activity_lawyer_detail_info

    override fun initEventAndData(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            mLawyerId = savedInstanceState.getValue(EXTRA_KEY_LAWYER_ID)
        }

        if (mLawyerId.isNullOrEmpty()) {
            closeCurrPage()
        } else {
            mPresenter.getLawyerDetailInfo(mLawyerId!!)
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putValue(EXTRA_KEY_LAWYER_ID, mLawyerId)
    }

    override fun showInitLoading(show: Boolean) {
        if (show) {
            loading_view.showDataLoading()
        } else {
            loading_view.stopLoadingAnim()
            loading_view.visibility = View.GONE
        }
    }

    override fun showLoadError(err: String?) {
        loading_view.showDataFail(err) {
            mPresenter.getLawyerDetailInfo(mLawyerId ?: "")
        }
    }

    override fun showLawyerDetailView(show: Boolean) {
        nsv_layer_content.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun showAvatar(avatar: String?) {
        ImageLoader.getInstance(this).displayImage(
            avatar,
            iv_lawyer_avatar,
            R.mipmap.uikit_icon_header_unknow,
            R.mipmap.uikit_icon_header_unknow
        )
    }

    override fun showLawyerName(name: String?) {
        tv_lawyer_name.text = name
    }

    override fun showLawyerAgeLimit(ageLimit: String?) {
        tv_lawyer_age_limit.text = ageLimit
    }

    override fun showLawyerCompany(company: String?) {
        tv_lawyer_company.text = company
    }

    override fun showLawyerLocation(location: String?) {
        tv_lawyer_location.text = location
    }

    override fun showLawyerService(list: List<LawyerServiceBean>?) {
        ll_lawyer_service.removeAllViews()
        list?.run {
            forEach { item ->
                val view = inflateLayout(R.layout.lawyer_layout_layout_service, null, false)
                val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp2px(85))
                ll_lawyer_service.addView(view, layoutParams)
                layoutParams.bottomMargin = dp2px(8)
                view.clickWithDuration {
                    val data = it.tag as? LawyerServiceBean
                    data?.let {
                        if (data.serviceName?.contains("律师函") == true) {
                            NavigationHelper.toCreateLawyerLetter(this@LawyerDetailActivity,
                                mLawyerId, 300)
                        }
                    }
                }
                view.tag = item
                view.findViewById<TextView>(R.id.tv_lawyer_service_name).text = item.serviceName
                view.findViewById<TextView>(R.id.tv_lawyer_service_desc).text = item.serviceDesc
                view.findViewById<TextView>(R.id.tv_lawyer_service_price).text = item.servicePrice
                //TODO
//                view.findViewById<TextView>(R.id.iv_lawyer_service_logo).text = ""
            }
        }
    }

    override fun showLawyerDesc(desc: String?) {
        tv_lawyer_desc.text = desc
    }

    override fun showLawyerHonorImage(list: List<String>?) {
        rv_lawyer_honor.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val adapter = CommImageAdapter(this)
        adapter.setNewData(list)
        rv_lawyer_honor.adapter = adapter
        adapter.setOnItemClickListener { adapter, _, position ->
            val list = adapter.data as? List<String>
            list?.let {
                NavigationHelper.toImageGalleryPage(this, list, position)
            }
        }
    }


}
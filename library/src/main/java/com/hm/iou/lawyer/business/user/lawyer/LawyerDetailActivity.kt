package com.hm.iou.lawyer.business.user.lawyer

import android.content.Context
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
import com.hm.iou.lawyer.bean.res.ImageUrlFileIdBean
import com.hm.iou.lawyer.bean.res.LawyerServiceBean
import com.hm.iou.lawyer.business.NavigationHelper
import com.hm.iou.tools.ImageLoader
import com.hm.iou.tools.kt.*
import kotlinx.android.synthetic.main.lawyer_activity_lawyer_detail_info.*

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
            var index = 0
            forEach { item ->
                val view = inflateLayout(R.layout.lawyer_layout_lawyer_service, null, false)
                val layoutParams =
                    LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, dp2px(106))
                ll_lawyer_service.addView(view, layoutParams)
                layoutParams.weight = 1f
                if (index > 0) {
                    layoutParams.leftMargin = dp2px(4)
                }
                view.clickWithDuration {
                    val data = it.tag as? LawyerServiceBean
                    data?.let {
                        if (data.serviceName?.contains("律师函") == true) {
                            NavigationHelper.toCreateLawyerLetter(
                                this@LawyerDetailActivity,
                                mLawyerId, data.price
                            )
                        } else if (data.serviceName?.contains("律师咨询") == true){
                            NavigationHelper.toCreateLawyerConsultPage(this@LawyerDetailActivity, mLawyerId, data.price)
                        }
                    }
                }
                view.tag = item
                view.findViewById<TextView>(R.id.tv_lawyer_service_name).text = item.serviceName
//                view.findViewById<TextView>(R.id.tv_lawyer_service_desc).text = item.serviceDesc
                view.findViewById<TextView>(R.id.tv_lawyer_service_price).text = item.servicePrice
                val ivLogo = view.findViewById<ImageView>(R.id.iv_lawyer_service_logo)
                ImageLoader.getInstance(this@LawyerDetailActivity)
                    .displayImage(item.logo, ivLogo, R.mipmap.lawyer_ic_lawyer_letter2, R.mipmap.lawyer_ic_lawyer_letter2)
                index++
            }
        }
    }

    override fun showLawyerDesc(desc: String?) {
        tv_lawyer_desc.text = desc
    }

    override fun showLawyerHonorImage(list: List<ImageUrlFileIdBean>?) {
        tv_lawyer_honor_label.visibility = if (list.isNullOrEmpty()) View.INVISIBLE else View.VISIBLE
        
        rv_lawyer_honor.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val adapter = LawyerHonorAdapter(this)
        adapter.setNewData(list)
        rv_lawyer_honor.adapter = adapter
        adapter.setOnItemClickListener { adapter, _, position ->
            val list: List<ImageUrlFileIdBean> = adapter.data as List<ImageUrlFileIdBean>
            val listUrl = ArrayList<String>()
            for (honor in list) {
                listUrl.add(honor?.url ?: "")
            }
            NavigationHelper.toImageGalleryPage(this, listUrl, position)
        }
    }

    class LawyerHonorAdapter : BaseQuickAdapter<ImageUrlFileIdBean, BaseViewHolder> {

        constructor(context: Context) : super(R.layout.lawyer_item_lawyer_home_lawyer_honor) {
            this@LawyerHonorAdapter.mContext = context
        }

        override fun convert(helper: BaseViewHolder?, item: ImageUrlFileIdBean?) {
            val ivLogo = helper?.getView<ImageView>(R.id.iv_image)
            ivLogo?.let {
                ImageLoader.getInstance(mContext).displayImage(item?.url, ivLogo)
            }
        }
    }


}
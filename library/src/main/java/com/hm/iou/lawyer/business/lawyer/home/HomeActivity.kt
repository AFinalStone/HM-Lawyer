package com.hm.iou.lawyer.business.lawyer.home

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View.*
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hm.iou.base.mvp.HMBaseActivity
import com.hm.iou.lawyer.R
import com.hm.iou.lawyer.bean.res.GetLawyerHomeDetailResBean
import com.hm.iou.lawyer.bean.res.LawyerServiceBean
import com.hm.iou.lawyer.business.lawyer.home.edit.EditLawyerHonorActivity
import com.hm.iou.lawyer.business.lawyer.home.edit.EditLawyerSelfIntroduceActivity
import com.hm.iou.lawyer.business.lawyer.home.edit.EditLawyerServiceActivity
import com.hm.iou.lawyer.dict.UpdateLawFirmStatusType
import com.hm.iou.lawyer.dict.UpdateYeaCheckStatusType
import com.hm.iou.tools.ImageLoader
import com.hm.iou.tools.kt.*
import com.hm.iou.uikit.HMTopBarView
import com.hm.iou.uikit.dialog.HMActionSheetDialog
import kotlinx.android.synthetic.main.lawyer_activity_lawyer_home.*

/**
 * 律师首页前置页面
 */
class HomeActivity : HMBaseActivity<HomePresenter>(),
    HomeContract.View {

    companion object {
        const val EXTRA_KEY_UPDATE_LAW_FIRM_STATE = "update_law_firm_state"
        const val EXTRA_KEY_UPDATE_YEAR_CHECK_STATE = "update_year_check_state"

        class LawyerServiceAdapter : BaseQuickAdapter<LawyerServiceBean, BaseViewHolder> {

            constructor(context: Context) : super(R.layout.lawyer_item_lawyer_home_lawyer_service) {
                this.mContext = context
            }

            override fun convert(helper: BaseViewHolder?, item: LawyerServiceBean?) {
                val ivLogo = helper?.getView<ImageView>(R.id.iv_lawyer_service_logo)
                ivLogo?.let {
                    ImageLoader.getInstance(mContext).displayImage(item?.logo, ivLogo)
                }
                helper?.setText(R.id.tv_lawyer_service_name, item?.serviceName)
                helper?.setText(R.id.tv_lawyer_service_desc, item?.serviceDesc)
                helper?.setText(R.id.tv_lawyer_service_price, item?.servicePrice)
            }
        }

        class LawyerHonorAdapter : BaseQuickAdapter<String, BaseViewHolder> {

            constructor(context: Context) : super(R.layout.lawyer_item_lawyer_home_lawyer_honor) {
                this.mContext = context
            }

            override fun convert(helper: BaseViewHolder?, item: String?) {
                val ivLogo = helper?.getView<ImageView>(R.id.iv_image)
                ivLogo?.let {
                    ImageLoader.getInstance(mContext).displayImage(item, ivLogo)
                }
            }
        }
    }

    /**
     * 更新执业机构的状态
     */
    private var mUpdateLawFirmState: Int? by extraDelegate(
        EXTRA_KEY_UPDATE_LAW_FIRM_STATE,
        null
    )

    /**
     * 年检状态
     */
    private var updateYearCheckState: Int? by extraDelegate(
        EXTRA_KEY_UPDATE_YEAR_CHECK_STATE,
        null
    )

    private var mLawyerServiceAdapter: LawyerServiceAdapter? = null
    private var mLawyerHonorAdapter: LawyerHonorAdapter? = null

    override fun getLayoutId(): Int = R.layout.lawyer_activity_lawyer_home

    override fun initPresenter(): HomePresenter =
        HomePresenter(this, this)

    override fun initEventAndData(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            mUpdateLawFirmState = savedInstanceState.getValue(EXTRA_KEY_UPDATE_LAW_FIRM_STATE)
            updateYearCheckState = savedInstanceState.getValue(EXTRA_KEY_UPDATE_YEAR_CHECK_STATE)
        }
        initView()
        mPresenter.init()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putValue(EXTRA_KEY_UPDATE_LAW_FIRM_STATE, mUpdateLawFirmState)
        outState?.putValue(EXTRA_KEY_UPDATE_YEAR_CHECK_STATE, updateYearCheckState)
    }

    private fun initView() {
        topbar.setOnMenuClickListener(object : HMTopBarView.OnTopBarMenuClickListener {
            override fun onClickTextMenu() {
            }

            override fun onClickImageMenu() {
                showBottomDialog()
            }

        })
        iv_lawyer_service_edit.clickWithDuration {
            startActivity<EditLawyerServiceActivity>()
        }
        iv_lawyer_self_info_edit.clickWithDuration {
            startActivity<EditLawyerSelfIntroduceActivity>()
        }
        iv_lawyer_honor_edit.clickWithDuration {
            startActivity<EditLawyerHonorActivity>()
        }
        mLawyerServiceAdapter = LawyerServiceAdapter(mContext)
        rv_lawyer_service.layoutManager = LinearLayoutManager(mContext)
        rv_lawyer_service.adapter = mLawyerServiceAdapter

        mLawyerHonorAdapter = LawyerHonorAdapter(mContext)
        rv_lawyer_honor.layoutManager =
            LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
        rv_lawyer_honor.adapter = mLawyerHonorAdapter
        bottom_bar.setOnTitleClickListener {

        }

    }

    override fun showDetail(detail: GetLawyerHomeDetailResBean) {

        tv_lawyer_name.text = detail.authName ?: ""
        tv_lawyer_age_limit.text = "执业%S年".format(detail.holdingYearCount)
        tv_lawyer_company.text = detail.lawFirm ?: ""
        tv_lawyer_location.text = detail.location ?: ""
        tv_lawyer_desc.text = detail.info ?: ""
        mLawyerServiceAdapter?.setNewData(detail.services)
        mLawyerHonorAdapter?.setNewData(detail.honors)
        tv_year_check_status.visibility = GONE
        //变更执业机构认证审核中
        if (UpdateLawFirmStatusType.APPLYING.status == mUpdateLawFirmState) {
            tv_year_check_status.text = "年检信息正在审核中"
            tv_year_check_status.setTextColor(resources.getColor(R.color.uikit_function_remind))
            tv_year_check_status.setBackgroundColor(0xFFF5F8FE.toInt())
            tv_year_check_status.visibility = VISIBLE
        } else if (UpdateLawFirmStatusType.FAILED.status == mUpdateLawFirmState) {
            tv_year_check_status.text = "年检信息审核失败，请重新上传年检信息"
            tv_year_check_status.setTextColor(resources.getColor(R.color.uikit_function_exception))
            tv_year_check_status.setBackgroundColor(0xFFFFF2F2.toInt())
            tv_year_check_status.visibility = VISIBLE
        }
        //更新年检信息的状态
        if (UpdateYeaCheckStatusType.APPLYING.status == mUpdateLawFirmState) {
            tv_year_check_status.text = "变更执业机构认证审核中"
            tv_year_check_status.setTextColor(resources.getColor(R.color.uikit_function_remind))
            tv_year_check_status.setBackgroundColor(0xFFF5F8FE.toInt())
            tv_year_check_status.visibility = VISIBLE
        }
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
            mPresenter.init()
        }
    }

    private fun showBottomDialog() {
        val list = ArrayList<String>()
        list.add("我已变更执业机构")
        HMActionSheetDialog.Builder(mContext)
            .setActionSheetList(list)
            .setOnItemClickListener { position, _ ->
                if (0 == position) {

                }
            }
            .create()
            .show()
    }


}
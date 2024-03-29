package com.hm.iou.lawyer.business.lawyer.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.View.*
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hm.iou.base.mvp.HMBaseActivity
import com.hm.iou.lawyer.R
import com.hm.iou.lawyer.bean.res.GetLawyerHomeDetailResBean
import com.hm.iou.lawyer.bean.res.ImageUrlFileIdBean
import com.hm.iou.lawyer.bean.res.LawyerServiceBean
import com.hm.iou.lawyer.business.NavigationHelper
import com.hm.iou.lawyer.business.lawyer.home.authen.AuthenProgressActivity
import com.hm.iou.lawyer.business.lawyer.home.edit.*
import com.hm.iou.lawyer.dict.UpdateLawFirmStatusType
import com.hm.iou.lawyer.dict.UpdateYeaCheckStatusType
import com.hm.iou.logger.Logger
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
                    ImageLoader.getInstance(mContext).displayImage(
                        item?.logo,
                        ivLogo,
                        R.mipmap.lawyer_ic_lawyer_letter2,
                        R.mipmap.lawyer_ic_lawyer_letter2
                    )
                }
                helper?.setText(R.id.tv_lawyer_service_name, item?.serviceName)
                helper?.setText(R.id.tv_lawyer_service_price, item?.servicePrice)
                helper?.addOnClickListener(R.id.iv_lawyer_service_edit)
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

    /**
     * 更新执业机构的状态
     */
    private var mUpdateLawFirmState: Int? by extraDelegate(
        EXTRA_KEY_UPDATE_LAW_FIRM_STATE,
        -1
    )

    /**
     * 年检状态
     */
    private var updateYearCheckState: Int? by extraDelegate(
        EXTRA_KEY_UPDATE_YEAR_CHECK_STATE,
        -1
    )

    private var mLawyerServiceAdapter: LawyerServiceAdapter? = null
    private var mLawyerHonorAdapter: LawyerHonorAdapter? = null
    private var mDetail: GetLawyerHomeDetailResBean? = null


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

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        mUpdateLawFirmState = intent?.getIntExtra(EXTRA_KEY_UPDATE_LAW_FIRM_STATE, -1)
        updateYearCheckState = intent?.getIntExtra(EXTRA_KEY_UPDATE_YEAR_CHECK_STATE, -1)
        Logger.d("mUpdateLawFirmState=$mUpdateLawFirmState")
        Logger.d("updateYearCheckState=$updateYearCheckState")
        mPresenter.init()
    }

    override fun onResume() {
        super.onResume()
        mPresenter.onResume()
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
        iv_lawyer_avatar.clickWithDuration {
            val intent = Intent(mContext, EditLawyerHeaderActivity::class.java)
            mDetail?.image?.url?.let {
                intent.putExtra(EditLawyerHeaderActivity.EXTRA_KEY_LAWYER_HEADE, it)
            }
            startActivity(intent)
        }
        iv_lawyer_self_info_edit.clickWithDuration {
            val intent = Intent(mContext, EditLawyerSelfIntroduceActivity::class.java)
            mDetail?.info?.let {
                intent.putExtra(
                    EditLawyerSelfIntroduceActivity.EXTRA_KEY_SELF_INTRODUCE,
                    mDetail?.info
                )
            }
            startActivity(intent)
        }
        iv_lawyer_honor_edit.clickWithDuration {
            val intent = Intent(mContext, EditLawyerHonorActivity::class.java)
            val honors = mDetail?.honors
            honors?.let {
                intent.putExtra(EditLawyerHonorActivity.EXTRA_KEY_LAWYER_HONOR, it)
            }
            startActivity(intent)
        }
        mLawyerServiceAdapter = LawyerServiceAdapter(mContext)
        rv_lawyer_service.layoutManager = GridLayoutManager(mContext, 2)
        rv_lawyer_service.adapter = mLawyerServiceAdapter
        mLawyerServiceAdapter?.setOnItemChildClickListener { _, view, position ->
            if (R.id.iv_lawyer_service_edit == view.id) {
                val lawYerServicePrice = mLawyerServiceAdapter?.getItem(position)?.price
                val lawYerServiceId = mLawyerServiceAdapter?.getItem(position)?.serviceId
                if (0 == position) {
                    val intent = Intent(mContext, EditLawyerServiceLetterPriceActivity::class.java)
                    lawYerServicePrice?.let {
                        intent.putExtra(
                            EditLawyerServiceLetterPriceActivity.EXTRA_KEY_SERVICE_PRICE,
                            lawYerServicePrice
                        )
                    }
                    lawYerServiceId?.let {
                        intent.putExtra(
                            EditLawyerServiceLetterPriceActivity.EXTRA_KEY_SERVICE_ID,
                            lawYerServiceId
                        )
                    }
                    startActivity(intent)
                } else if (1 == position) {
                    val intent = Intent(mContext, EditLawyerServiceConsultPriceActivity::class.java)
                    lawYerServicePrice?.let {
                        intent.putExtra(
                            EditLawyerServiceLetterPriceActivity.EXTRA_KEY_SERVICE_PRICE,
                            lawYerServicePrice
                        )
                    }
                    lawYerServiceId?.let {
                        intent.putExtra(
                            EditLawyerServiceLetterPriceActivity.EXTRA_KEY_SERVICE_ID,
                            lawYerServiceId
                        )
                    }
                    startActivity(intent)
                }
            }
        }


        mLawyerHonorAdapter = LawyerHonorAdapter(mContext)
        mLawyerHonorAdapter?.setOnItemClickListener { adapter, _, position ->
            val list: List<ImageUrlFileIdBean> = adapter.data as List<ImageUrlFileIdBean>
            val listUrl = ArrayList<String>()
            for (honor in list) {
                listUrl.add(honor.url ?: "")
            }
            NavigationHelper.toImageGalleryPage(this, listUrl, position)
        }
        rv_lawyer_honor.layoutManager =
            LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
        rv_lawyer_honor.adapter = mLawyerHonorAdapter
    }


    override fun showDetail(detail: GetLawyerHomeDetailResBean) {
        mDetail = detail
        val headerUrl = detail.image?.url
        ImageLoader.getInstance(mContext)
            .displayImage(headerUrl, iv_lawyer_avatar, R.mipmap.uikit_icon_header_unknow)
        tv_lawyer_name.text = detail.authName ?: ""
        tv_lawyer_age_limit.text = "执业%S年".format(detail.holdingYearCount)
        tv_lawyer_company.text = detail.lawFirm ?: ""
        tv_lawyer_location.text = detail.location ?: ""
        tv_lawyer_desc.text = detail.info ?: ""
        mLawyerServiceAdapter?.setNewData(detail.services)
        mLawyerHonorAdapter?.setNewData(detail.honors)
        tv_check_status.visibility = GONE
        bottom_bar.setTitleVisible(false)
        //变更执业机构认证审核中
        if (UpdateLawFirmStatusType.APPLYING.status == mUpdateLawFirmState) {
            tv_check_status.text = "变更执业机构认证审核中"
            tv_check_status.setTextColor(resources.getColor(R.color.uikit_function_remind))
            tv_check_status.setBackgroundColor(0xFFF5F8FE.toInt())
            tv_check_status.visibility = VISIBLE
        }
        //更新年检信息的状态
        when (updateYearCheckState) {
            UpdateYeaCheckStatusType.APPLYING.status -> {
                tv_check_status.text = "年检信息正在审核中"
                tv_check_status.setTextColor(resources.getColor(R.color.uikit_function_remind))
                tv_check_status.setBackgroundColor(0xFFF5F8FE.toInt())
                tv_check_status.visibility = VISIBLE
            }
            UpdateYeaCheckStatusType.FAILED.status -> {
                tv_check_status.text = "年检信息审核失败，请重新上传年检信息"
                tv_check_status.setTextColor(resources.getColor(R.color.uikit_function_exception))
                tv_check_status.setBackgroundColor(0xFFFFF2F2.toInt())
                tv_check_status.visibility = VISIBLE
                bottom_bar.updateTitle("请重新上传年检信息")
                bottom_bar.setTitleVisible(true)
                bottom_bar.setOnTitleClickListener {
                    startActivity<YearCheckAuthenActivity>()
                }
            }
            UpdateYeaCheckStatusType.NEED_UPDATE_YEAR_CHECK.status -> {
                bottom_bar.updateTitle("更新年检信息")
                bottom_bar.setTitleVisible(true)
                bottom_bar.setOnTitleClickListener {
                    startActivity<YearCheckAuthenActivity>()
                }
            }
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
                    if (UpdateLawFirmStatusType.APPLYING.status == mUpdateLawFirmState) {
                        startActivity<AuthenProgressActivity>(
                            AuthenProgressActivity.EXTRA_KEY_IF_AUTHENTICATION_FAILED to false
                        )
                    } else {
                        startActivity<UpdateLawyerFirmActivity>()
                    }
                }
            }
            .create()
            .show()
    }


}
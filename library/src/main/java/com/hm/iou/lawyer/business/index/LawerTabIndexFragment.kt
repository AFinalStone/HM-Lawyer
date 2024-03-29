package com.hm.iou.lawyer.business.index

import android.content.Context
import android.graphics.Outline
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.hm.iou.base.BaseBizAppLike
import com.hm.iou.base.adver.AdBean
import com.hm.iou.base.mvp.HMBaseFragment
import com.hm.iou.base.utils.RouterUtil
import com.hm.iou.base.utils.StatusBarUtil
import com.hm.iou.lawyer.R
import com.hm.iou.lawyer.business.NavigationHelper
import com.hm.iou.sharedata.event.CommBizEvent
import com.hm.iou.tools.DensityUtil
import com.hm.iou.tools.ImageLoader
import com.hm.iou.tools.ScreenUtil
import com.hm.iou.tools.StringUtil
import com.hm.iou.tools.kt.clickWithDuration
import com.hm.iou.tools.kt.density
import com.youth.banner.Banner
import com.youth.banner.loader.ImageLoaderInterface
import org.greenrobot.eventbus.EventBus
import java.util.*

/**
 * Created by hjy on 2019/11/11
 *
 * 律师 tab 首页
 */
class LawerTabIndexFragment : HMBaseFragment<LawerTabIndexPresenter>(),
    LawerTabIndexContract.View {

    override fun getLayoutId(): Int = R.layout.lawyer_fragment_lawer_tab_index

    override fun initPresenter() = LawerTabIndexPresenter(activity!!, this)

    override fun initEventAndData(savedInstanceState: Bundle?) {
        StatusBarUtil.setStatusBarDarkFont(mActivity, true)
        initClickEvents()
        mPresenter.init()
    }

    private fun initClickEvents() {
        //头像
        mContentView?.findViewById<View>(R.id.rl_lawyer_header)?.clickWithDuration {
            EventBus.getDefault().post(CommBizEvent("iou_show_home_left_menu", "显示首页左侧菜单"))
        }
        //律师工作台
        mContentView?.findViewById<View>(R.id.tv_lawyer_workbench)?.clickWithDuration {
            activity?.let { NavigationHelper.toWorkbenchActivity(it) }
        }

        //律师咨询
        mContentView?.findViewById<View>(R.id.tv_lawyer_lawyer_consult)?.clickWithDuration {
            activity?.let { NavigationHelper.toCreateLawyerConsultPage(it) }
        }
        //找律师
        mContentView?.findViewById<View>(R.id.tv_lawyer_find_lawyer)?.clickWithDuration {
            activity?.let { NavigationHelper.toFindLawyerPage(it) }
        }
        //律师函
        mContentView?.findViewById<View>(R.id.tv_lawyer_lawyer_letter)?.clickWithDuration {
            activity?.let {
                NavigationHelper.toLetterDescPage(it)
            }
        }
        //我的订单
        mContentView?.findViewById<View>(R.id.tv_lawyer_my_order)?.clickWithDuration {
            activity?.let {
                NavigationHelper.toUserPersonalOrderListPage(it)
            }
        }
        //北京互联网法院
        mContentView?.findViewById<View>(R.id.iv_lawyer_court_banner)?.clickWithDuration {
            activity?.let {
                NavigationHelper.toBeijingInternetCourtPage(it)
            }
        }
        //法律百科
        mContentView?.findViewById<View>(R.id.rl_lawyer_baike)?.clickWithDuration {
            activity?.let { NavigationHelper.toLawBaikePage(it) }
        }
        //计算
        mContentView?.findViewById<View>(R.id.rl_lawyer_calc)?.clickWithDuration {
            activity?.let { NavigationHelper.toFeeCalcPage(it) }
        }

        mContentView?.findViewById<View>(R.id.rl_lawyer_delegate)?.clickWithDuration {
            activity?.let { NavigationHelper.toCaseDelegatePage(it) }
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            StatusBarUtil.setStatusBarDarkFont(mActivity, true)
            mPresenter.updateRedFlagCount()
            mPresenter.getTopBanner()
        }
    }

    override fun onResume() {
        super.onResume()
        val banner = mContentView?.findViewById<Banner>(R.id.banner_lawyer)
        banner?.let {
            val list: List<AdBean>? = banner.getTag(R.string.app_name) as? List<AdBean>
            if (list != null && list.size > 1) {
                banner.startAutoPlay()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        val banner = mContentView?.findViewById<Banner>(R.id.banner_lawyer)
        banner?.stopAutoPlay()
    }

    override fun updateRedFlagCount(redFlagCount: String) {
        val tvHeadRedNum = mContentView?.findViewById<TextView>(R.id.tv_lawyer_unread)
        tvHeadRedNum?.let {
            if (redFlagCount.isNullOrEmpty() || "0" == redFlagCount) {
                it.visibility = View.INVISIBLE
            } else {
                it.visibility = View.VISIBLE
                it.text = redFlagCount
            }
        }
    }

    override fun showBanner(list: List<AdBean>?) {
        val banner = mContentView?.findViewById<Banner>(R.id.banner_lawyer)
        banner ?: return
        if (list.isNullOrEmpty()) {
            banner.visibility = View.GONE
            banner.stopAutoPlay()
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                banner.outlineProvider = object : ViewOutlineProvider() {
                    override fun getOutline(view: View?, outline: Outline?) {
                        view ?: return
                        outline ?: return
                        outline.setRoundRect(0, 0, view.width, view.height, view.context.density * 4)
                    }
                }
                banner.clipToOutline = true
            }

            //设置 banner 宽度、高度
            val params = banner.layoutParams as LinearLayout.LayoutParams
            params.height = ((ScreenUtil.getScreenWidth(activity) - DensityUtil.dip2px(
                activity,
                24f
            )) / 3f).toInt()
            banner.layoutParams = params
            banner.setImageLoader(object : ImageLoaderInterface<View> {
                override fun createImageView(context: Context?): View? = null
                override fun displayImage(context: Context?, path: Any?, imageView: View?) {
                    val data = path as? AdBean
                    data?.let {
                        ImageLoader.getInstance(mActivity)
                            .displayImage(it.url, imageView as ImageView)
                    }
                }
            })
            banner.setOnBannerListener { position ->
                val list: List<AdBean>? = banner.getTag(R.string.app_name) as? List<AdBean>
                if (list.isNullOrEmpty() || position >= list.size)
                    return@setOnBannerListener

                val data = list[position]
                if (data != null) {
                    var linkUrl = StringUtil.getUnnullString(data.linkUrl)
                    RouterUtil.clickMenuLink(mActivity, linkUrl)
                }
            }
            banner.setImages(ArrayList<AdBean>()).start()

            banner.visibility = View.VISIBLE
            banner.update(list)
            if (list.size == 1) {
                banner.stopAutoPlay()
            } else {
                banner.startAutoPlay()
            }
            banner.setTag(R.string.app_name, list)
        }
    }

    override fun showTopLawyerWorkbench(visibility: Int) {
        mContentView?.findViewById<View>(R.id.tv_lawyer_workbench)?.visibility = visibility
    }
}
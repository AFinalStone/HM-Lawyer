package com.hm.iou.lawyer.business

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Parcelable

import com.hm.iou.lawyer.business.lawyer.home.authen.AuthenticationActivity

import com.hm.iou.base.ImageGalleryActivity
import com.hm.iou.lawyer.R
import com.hm.iou.lawyer.bean.LetterReceiverBean
import com.hm.iou.lawyer.bean.res.CustLetterDetailResBean
import com.hm.iou.lawyer.business.lawyer.home.HomeActivity
import com.hm.iou.lawyer.business.lawyer.home.authen.AuthenProgressActivity

import com.hm.iou.lawyer.business.lawyer.withdraw.WithDrawActivity
import com.hm.iou.lawyer.business.user.create.CreateLawyerLetterActivity
import com.hm.iou.lawyer.business.user.create.InputReceiverAddressActivity
import com.hm.iou.lawyer.business.user.create.LawyerLetterDescActivity
import com.hm.iou.lawyer.business.user.find.FindLawyerActivity
import com.hm.iou.lawyer.business.user.lawyer.LawyerDetailActivity
import com.hm.iou.lawyer.business.user.order.MyOrderDetailActivity
import com.hm.iou.lawyer.business.user.order.MyOrderListActivity
import com.hm.iou.lawyer.business.user.order.RatingLawyerActivity
import com.hm.iou.router.Router
import com.hm.iou.tools.kt.startActivity
import com.umeng.commonsdk.stateless.UMSLEnvelopeBuild.mContext

/**
 * @author : 借条管家-shilei
 * @version : 0.0.1
 * @Date : 2019-11-11 11:45
 * @E-Mail : afinalstone@foxmail.com
 */
object NavigationHelper {

    /**
     * 我的钱包
     */
    fun toMyWallet(activity: Activity) {
        val intent = Intent(activity, WithDrawActivity::class.java)
        activity.startActivity(intent)
    }

    /**
     * 律师页面
     */
    fun toAuthentication(context: Context) {
        context.startActivity<AuthenticationActivity>()
    }

    /**
     * 进入查找律师页面
     */
    fun toFindLawyerPage(context: Context) {
        context.startActivity<FindLawyerActivity>()
    }

    /**
     * 查找律师进入律师页详情
     */
    fun toLawyerDetailPage(context: Context, lawyerId: String) {
        context.startActivity<LawyerDetailActivity>(
            LawyerDetailActivity.EXTRA_KEY_LAWYER_ID to lawyerId
        )
    }

    /**
     * 查看完整图片，没有任何操作按钮，只是查看
     */
    fun toImageGalleryPage(context: Context, imgList: List<String>, index: Int) {
        val intent = Intent(context, ImageGalleryActivity::class.java)
        intent.putExtra(ImageGalleryActivity.EXTRA_KEY_IMAGES, imgList.toTypedArray())
        intent.putExtra(ImageGalleryActivity.EXTRA_KEY_INDEX, index)
        context.startActivity(intent)
    }

    /**
     * 查看并且能够删除图片
     */
    fun toEditGalleryPage(
        context: Activity,
        urls: Array<String>,
        selectedIndex: Int,
        reqCode: Int
    ) {
        val intent = Intent(context, ImageGalleryActivity::class.java)
        intent.putExtra(ImageGalleryActivity.EXTRA_KEY_IMAGES, urls)
        intent.putExtra(ImageGalleryActivity.EXTRA_KEY_INDEX, selectedIndex)
        intent.putExtra(ImageGalleryActivity.EXTRA_KEY_SHOW_DELETE, 1)
        context.startActivityForResult(intent, reqCode)
    }

    /**
     * 创建律师函
     *
     * @param context
     * @param lawyerId 邀请的律师id，不邀请则为空
     * @param price 价格，要求的律师价格，不要求则为空
     */
    fun toCreateLawyerLetter(context: Context, lawyerId: String?, price: Int?) {
        context.startActivity<CreateLawyerLetterActivity>(
            CreateLawyerLetterActivity.EXTRA_KEY_LAWYER_ID to (lawyerId ?: ""),
            CreateLawyerLetterActivity.EXTRA_KEY_PRICE to (price ?: 0)
        )
    }

    /**
     * 重新创建律师函
     */
    fun toCreateLawyerLetter(context: Context, data: CustLetterDetailResBean) {
        context.startActivity<CreateLawyerLetterActivity>(
            CreateLawyerLetterActivity.EXTRA_KEY_LAWYER_ID to (data.lawyerAbout?.lawyerId ?: ""),
            CreateLawyerLetterActivity.EXTRA_KEY_PRICE to (data.price)
        )
    }

    /**
     * 输入收件人地址页面
     */
    fun toInputReceiverAddress(context: Activity, reqCode: Int, data: LetterReceiverBean?) {
        val intent = Intent(context, InputReceiverAddressActivity::class.java)
        intent.putExtra("receiver", data as? Parcelable)
        context.startActivityForResult(intent, reqCode)
    }

    /**
     * 进入律师函介绍页面
     */
    fun toLetterDescPage(context: Context) {
        context.startActivity<LawyerLetterDescActivity>()
    }

    /**
     * 进入用户个人的订单列表
     */
    fun toUserPersonalOrderListPage(context: Context) {
        context.startActivity<MyOrderListActivity>()
    }

    /**
     * 用户进入我的订单详情页面
     */
    fun toUserOrderDetailPage(context: Context, orderId: String) {
        context.startActivity<MyOrderDetailActivity>(
            MyOrderDetailActivity.EXTRA_KEY_ORDER_ID to orderId
        )
    }

    /**
     * 进入评价律师页面
     */
    fun toRatingLawyerPage(context: Context, orderId: String) {
        context.startActivity<RatingLawyerActivity>(
            RatingLawyerActivity.EXTRA_KEY_ORDER_ID to orderId
        )
    }

    /**
     * 支付律师函
     */
    fun toPayLawyerLetter(context: Context, innerUser: Boolean, billId: String, money: String, reqCode: Int) {
        Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/pay/lawyer_letter_pay")
            .withString("package_title", "律师函服务费")
            .withString("package_money", money)
            .withString("package_content", context.getString(R.string.lawyer_pay_info))
            .withString("bill_id", billId)
            .withString("inner_user", if (innerUser) "1" else "")
            .navigation(context, reqCode)
    }

}
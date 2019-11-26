package com.hm.iou.lawyer.business

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Parcelable
import com.hm.iou.base.BaseBizAppLike
import com.hm.iou.base.ImageGalleryActivity
import com.hm.iou.base.utils.RouterUtil
import com.hm.iou.lawyer.R
import com.hm.iou.lawyer.bean.LetterReceiverBean
import com.hm.iou.lawyer.bean.res.CustLetterDetailResBean
import com.hm.iou.lawyer.bean.res.FileInfo
import com.hm.iou.lawyer.business.lawyer.home.authen.AuthenticationActivity
import com.hm.iou.lawyer.business.lawyer.home.prepare.HomePrepareActivity
import com.hm.iou.lawyer.business.lawyer.workbench.WorkBenchActivity
import com.hm.iou.lawyer.business.lawyer.workbench.wallet.WalletActivity
import com.hm.iou.lawyer.business.lawyer.workbench.withdraw.WithDrawActivity
import com.hm.iou.lawyer.business.user.create.CreateLawyerConsultActivity
import com.hm.iou.lawyer.business.user.create.CreateLawyerLetterActivity
import com.hm.iou.lawyer.business.user.create.InputReceiverAddressActivity
import com.hm.iou.lawyer.business.user.create.LawyerLetterDescActivity
import com.hm.iou.lawyer.business.user.find.FindLawyerActivity
import com.hm.iou.lawyer.business.user.lawyer.LawyerDetailActivity
import com.hm.iou.lawyer.business.user.order.*
import com.hm.iou.router.Router
import com.hm.iou.tools.kt.startActivity
import java.util.*

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
    fun toMyWallet(context: Context) {
        context.startActivity<WalletActivity>()
    }

    /**
     * 进入提现页面
     */
    fun toWithdrawMoneyPage(context: Context, walletBalance: Int) {
        context.startActivity<WithDrawActivity>(
            WithDrawActivity.EXTRA_KEY_WALLET_BALANCE to walletBalance
        )
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
            CreateLawyerLetterActivity.EXTRA_KEY_PRICE to (data.price),
            CreateLawyerLetterActivity.EXTRA_KEY_ORDER to data
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

    /**
     * 支付律师咨询
     */
    fun toPayLawyerConsult(context: Context, innerUser: Boolean, billId: String, money: String, reqCode: Int) {
        Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/pay/lawyer_letter_pay")
            .withString("package_title", "律师咨询费")
            .withString("package_money", money)
            .withString("package_content", context.getString(R.string.lawyer_pay_consult_info))
            .withString("bill_id", billId)
            .withString("inner_user", if (innerUser) "1" else "")
            .navigation(context, reqCode)
    }

    /**
     * 进入律师工作台
     */
    fun toWorkbenchActivity(context: Context) {
        context.startActivity<WorkBenchActivity>()
    }

    /**
     * 进入律师个人首页
     */
    fun toLawyerHomePage(context: Context) {
        context.startActivity<HomePrepareActivity>()
    }

    /**
     * 进入费用计算页面
     */
    fun toFeeCalcPage(context: Context) {
        Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/webview/index")
            .withString("url", "${BaseBizAppLike.getInstance().h5Server}/apph5/HM-lawyer/")
            .withString("showtitlebar", "false")
            .navigation(context)
    }

    /**
     * 进入法律百科
     */
    fun toLawBaikePage(context: Context) {
        RouterUtil.clickMenuLink(context, "https://h5.54jietiao.com/moneyMarket_V1-1-2/html/otherLaw.html")
    }

    /**
     * 交易记录
     */
    fun toTradeRecordListPage(context: Context) {
        RouterUtil.clickMenuLink(context, "${BaseBizAppLike.getInstance().h5Server}/apph5/HM-lawyer/#/businessRecord")
    }

    /**
     * 进入常见问题页面
     */
    fun toCommQuestionPage(context: Context) {
        RouterUtil.clickMenuLink(context, "https://h5.54jietiao.com/appTopic/articleDetail.html?articleId=38")
    }

    /**
     * 进入提现记录页面
     */
    fun toWithdrawMoneyRecordPage(context: Context) {
        RouterUtil.clickMenuLink(context, "${BaseBizAppLike.getInstance().h5Server}/apph5/HM-lawyer/#/cashOut")
    }

    /**
     * 进入银行卡绑定页面
     */
    fun toBindBankCardPage(context: Context) {
        Router.getInstance()
            .buildWithUrl("hmiou://m.54jietiao.com/pay/user_bind_bank?source=lawyer")
            .navigation(context)
    }

    /**
     * 进入提现详情页面
     */
    fun toWithdrawDetailPage(context: Context, sequenceId: String) {
        RouterUtil.clickMenuLink(context,
            "${BaseBizAppLike.getInstance().h5Server}/apph5/HM-lawyer/#/cashDetail?sequenceId=$sequenceId")
    }

    /**
     * 点击背景互联网法院页面
     */
    fun toBeijingInternetCourtPage(context: Context) {
        RouterUtil.clickMenuLink(context, "https://h5.54jietiao.com/Content/index.html?autoId=1580")
    }

    /**
     * 进入案件委托页面
     */
    fun toCaseDelegatePage(context: Context) {
        RouterUtil.clickMenuLink(context, "https://h5.54jietiao.com/moneyMarket_V1-1-2/html/typeSelect.html")
    }

    /**
     * 进入创建律师咨询页面
     */
    fun toCreateLawyerConsultPage(context: Context) {
        context.startActivity<CreateLawyerConsultActivity>()
    }

    /**
     * 进入创建律师咨询页面，指定了律师
     */
    fun toCreateLawyerConsultPage(context: Context, lawyerId: String?, price: Int?,
                                  desc: String? = null, fileList: List<FileInfo>? = null) {
        val intent = Intent(context, CreateLawyerConsultActivity::class.java)
        intent.putExtra(CreateLawyerConsultActivity.EXTRA_KEY_LAWYER_ID, lawyerId)
        intent.putExtra(CreateLawyerConsultActivity.EXTRA_KEY_PRICE, price)
        intent.putExtra(CreateLawyerConsultActivity.EXTRA_KEY_DESC, desc)
        val arrayList = ArrayList<FileInfo>()
        fileList?.forEach { arrayList.add(it) }
        intent.putExtra(CreateLawyerConsultActivity.EXTRA_KEY_IMGS, arrayList)
        context.startActivity(intent)
    }

    /***
     * 进入律师咨询详情
     */
    fun toLawyerConsultDetailPage(context: Context, orderId: String) {
        context.startActivity<ConsultDetailActivity>(
            ConsultDetailActivity.EXTRA_KEY_ORDER_ID to orderId
        )
    }

    /**
     * 进入咨询添加问题页面
     */
    fun toAddConsultQuestion(context: Context, orderId: String){
        context.startActivity<ConsultAddQuestionActivity>(
            ConsultAddQuestionActivity.EXTRA_KEY_ORDER_ID to orderId
        )
    }


}
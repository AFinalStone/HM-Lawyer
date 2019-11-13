package com.hm.iou.lawyer.business.user.order

import android.content.Context
import com.hm.iou.base.mvp.HMBasePresenter
import com.hm.iou.lawyer.business.NavigationHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Created by hjy on 2019/11/12
 *
 * 我的订单详情
 */
class MyOrderDetailPresenter(context: Context, view: MyOrderDetailContract.View) :
    HMBasePresenter<MyOrderDetailContract.View>(context, view),
    MyOrderDetailContract.Presenter {

    override fun getOrderDetail(orderId: String) {
        launch {
            mView.showInitLoading(true)
            try {
                delay(2000)

                mView.showOrderStatus("订单已完成")
                mView.showOrderPrice("￥100.00")
                mView.showOrHideTimeView(true)
                mView.showOrderCompleteTime("2019.11.23 12:10:10")
                mView.showOrHideLawyerInfoView(true)
                mView.showLawyerInfo(
                    "http://b-ssl.duitang.com/uploads/item/201707/10/20170710070015_XjiQM.jpeg",
                    "张三律师", "执业7年", "杭州泰杭律师事务所"
                )

                mView.showLetterReceiverInfo(
                    "张三", "15967132742", "429006198609252111",
                    "浙江省杭州市余杭区竹海水韵"
                )
                mView.showOrderDesc("朋友准备向我借钱，但是不知道借条是否合法，想请律师查看。")
                val list = listOf(
                    "http://b-ssl.duitang.com/uploads/item/201707/10/20170710070015_XjiQM.jpeg",
                    "http://b-ssl.duitang.com/uploads/item/201707/10/20170710070015_XjiQM.jpeg",
                    "http://b-ssl.duitang.com/uploads/item/201707/10/20170710070015_XjiQM.jpeg"
                )
                mView.showOrderImages(list)

                mView.showOrHideExpressInfoView(true)
                mView.showExpressName("顺丰快递")
                mView.showExpressNo("128318273123")
                mView.showExpressImg(list)

                mView.showOrHideServiceRatingView(true)

                mView.showOrHideBottomBtn(true)
                mView.showBottomBtn("评论律师") {
                    NavigationHelper.toRatingLawyerPage(mContext, "123")
                }

                mView.showInitLoading(false)
            } catch (e: Exception) {
                handleException(e, showBusinessError = false, showCommError = false)
                mView.showInitFail(e.message)
            }
        }
    }

    override fun toSeeLawyerInfo() {
        NavigationHelper.toLawyerDetailPage(mContext, "123")
    }
}
package com.hm.iou.lawyer.business.lawyer.home.edit

import com.hm.iou.base.mvp.BaseContract

/**
 * 律师首页前置页面
 */
class EditLawyerServicePriceContract {

    interface View : BaseContract.BaseView {
    }

    interface Presenter : BaseContract.BasePresenter {
        /**
         * 更新律师服务价格
         */
        fun updateLawyerServicePrice(price: Int, serviceId: Int)
    }
}
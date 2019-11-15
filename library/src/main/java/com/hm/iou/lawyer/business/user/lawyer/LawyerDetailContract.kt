package com.hm.iou.lawyer.business.user.lawyer

import com.hm.iou.base.mvp.BaseContract
import com.hm.iou.lawyer.bean.res.ImageUrlFileIdBean
import com.hm.iou.lawyer.bean.res.LawyerServiceBean

/**
 * Created by hjy on 2019/11/12
 *
 * 律师详情
 */
interface LawyerDetailContract {

    interface View : BaseContract.BaseView {

        fun showInitLoading(show: Boolean)

        fun showLoadError(err: String?)

        fun showLawyerDetailView(show: Boolean)

        fun showAvatar(avatar: String?)

        fun showLawyerName(name: String?)

        fun showLawyerAgeLimit(ageLimit: String?)

        fun showLawyerCompany(company: String?)

        fun showLawyerLocation(location: String?)

        fun showLawyerService(list: List<LawyerServiceBean>?)

        fun showLawyerDesc(desc: String?)

        fun showLawyerHonorImage(list: List<ImageUrlFileIdBean>?)
    }

    interface Presenter : BaseContract.BasePresenter {

        fun getLawyerDetailInfo(lawyerId: String)
    }
}
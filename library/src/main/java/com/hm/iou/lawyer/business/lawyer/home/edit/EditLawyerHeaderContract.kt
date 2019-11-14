package com.hm.iou.lawyer.business.lawyer.home.edit

import com.hm.iou.base.mvp.BaseContract
import com.hm.iou.database.table.IouData
import java.io.File

/**
 * 律师首页前置页面
 */
class EditLawyerHeaderContract {

    interface View : BaseContract.BaseView {

        fun showUserAvatar(url: String)

    }

    interface Presenter : BaseContract.BasePresenter {

        fun uploadHeader(file: File)
    }
}
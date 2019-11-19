package com.hm.iou.lawyer.business.user.find

import android.content.Context
import android.view.View
import com.hm.iou.base.mvp.HMBasePresenter
import com.hm.iou.lawyer.api.LawyerApi
import com.hm.iou.lawyer.bean.res.LawyerItemBean
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Created by hjy on 2019/11/11
 *
 * 查找律师
 */
class FindLawyerPresenter(context: Context, view: FindLawyerContract.View) :
    HMBasePresenter<FindLawyerContract.View>(context, view), FindLawyerContract.Presenter {

    private var mAgeLimitType = 0       //0：不限，1：1年-3年，2：3年-5年，3：5年-10年，4：10年以上
    private var mSearchName = ""

    private var mPageNoByAgeLimit = 1
    private var mPageNoByName = 1

    private val mDataList: MutableList<ILawyerItem> = mutableListOf()
    private val mDataListByName: MutableList<ILawyerItem> = mutableListOf()

    override fun findByAgeLimit(ageLimitType: Int) {
        mAgeLimitType = ageLimitType
        mDataList.clear()
        mPageNoByAgeLimit = 1
        mView.clearList(true)
    }

    override fun getNextPageByAgeLimit() {
        launch {
            if (mPageNoByAgeLimit == 1) {
                mView.showInitLoading(true)
            }
            try {
                val result = handleResponse(
                    LawyerApi.getLawyerList(
                        mPageNoByAgeLimit,
                        10,
                        mAgeLimitType,
                        null
                    )
                )
                val list = mutableListOf<ILawyerItem>()
                result?.list?.let {
                    it.forEach { bean ->
                        list.add(convertData(bean))
                    }
                }

                mView.hideInitLoading(true)
                mView.showRecyclerView(View.VISIBLE, true)
                mDataList.addAll(list)
                mView.showLawyers(list, true)

                if (mDataList.isNullOrEmpty()) {
                    mView.showDataEmpty(true)
                } else {
                    if (list.size < 10) {
                        mView.showLoadMoreEnd(true)
                    } else {
                        mView.showLoadMoreComplete(true)
                    }
                }
                mPageNoByAgeLimit++
            } catch (e: Exception) {
                handleException(e, showCommError = false, showBusinessError = false)
                mView.showRecyclerView(View.GONE, true)
                mView.showLoadingError("数据加载失败，请重试", true)
            }
        }
    }

    override fun findByLawyerName(name: String) {
        mSearchName = name
        mDataListByName.clear()
        mPageNoByName = 1
        mView.clearList(false)
    }

    override fun getNextPageByLawyerName() {
        launch {
            if (mPageNoByName == 1) {
                mView.showInitLoading(false)
            }
            try {
                val result = handleResponse(
                    LawyerApi.getLawyerList(
                        mPageNoByName,
                        10,
                        0,
                        mSearchName
                    )
                )
                val list = mutableListOf<ILawyerItem>()
                result?.list?.let {
                    it.forEach { bean ->
                        list.add(convertData(bean))
                    }
                }

                mView.hideInitLoading(false)
                mView.showRecyclerView(View.VISIBLE, false)
                mDataListByName.addAll(list)
                mView.showLawyers(list, false)

                if (mDataListByName.isNullOrEmpty()) {
                    mView.showDataEmpty(false)
                } else {
                    if (list.size < 10) {
                        mView.showLoadMoreEnd(false)
                    } else {
                        mView.showLoadMoreComplete(false)
                    }
                }
                mPageNoByName++
            } catch (e: Exception) {
                handleException(e, showCommError = false, showBusinessError = false)
                mView.showRecyclerView(View.GONE, false)
                mView.showLoadingError("数据加载失败，请重试", false)
            }
        }
    }

    private fun convertData(bean: LawyerItemBean): ILawyerItem {
        return object : ILawyerItem {
            override fun getLawyerId(): String? {
                return bean.lawyerId
            }

            override fun getAvatar(): String? {
                return bean.image
            }

            override fun getName(): String? {
                return "${bean.authName}律师"
            }

            override fun getAgeLimit(): String? {
                return "执业${bean.holdingYearCount}年"
            }

            override fun getCompanyName(): String? {
                return bean.lawFirm
            }

            override fun getLocation(): String? {
                return bean.location
            }
        }
    }

}
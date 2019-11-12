package com.hm.iou.lawyer.business.user.find

import android.content.Context
import android.view.View
import com.hm.iou.base.mvp.HMBasePresenter
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

    private var mPageNoByAgeLimit = 0
    private var mPageNoByName = 0

    private val mDataList: MutableList<ILawyerItem> = mutableListOf()
    private val mDataListByName: MutableList<ILawyerItem> = mutableListOf()

    override fun findByAgeLimit(ageLimitType: Int) {
        mAgeLimitType = ageLimitType
        mDataList.clear()
        mView.clearList(true)
    }

    override fun getNextPageByAgeLimit() {
        launch {
            if (mPageNoByAgeLimit == 0) {
                mView.showInitLoading(true)
            }

            try {
                val list = mutableListOf<ILawyerItem>()
                for (index in 1..5) {
                    list.add(object : ILawyerItem {
                        override fun getLawyerId(): String {
                            return index.toString()
                        }

                        override fun getAvatar(): String? {
                            return ""
                        }

                        override fun getName(): String? {
                            return "张三律师"
                        }

                        override fun getAgeLimit(): String? {
                            return "z执业7年"
                        }

                        override fun getCompanyName(): String? {
                            return "杭州泰杭律师事务所"
                        }

                        override fun getLocation(): String? {
                            return "浙江省杭州市余杭区"
                        }
                    })
                }

                delay(2000)

                mView.showRecyclerView(View.VISIBLE, true)
                mView.hideInitLoading(true)
                mView.showLoadMoreComplete(true)
                mDataList.addAll(list)
                mView.showLawyers(list, true)
            } catch (e: Exception) {
                mView.showRecyclerView(View.GONE, true)
                mView.showLoadingError("error", true)
            }
        }
    }

    override fun findByLawyerName(name: String) {
        mSearchName = name
        mDataListByName.clear()
        mView.clearList(false)
    }

    override fun getNextPageByLawyerName() {
        launch {
            if (mPageNoByName == 0) {
                mView.showInitLoading(false)
            }
            try {
                val list = mutableListOf<ILawyerItem>()
                for (index in 1..10) {
                    list.add(object : ILawyerItem {
                        override fun getLawyerId(): String {
                            return index.toString()
                        }

                        override fun getAvatar(): String? {
                            return ""
                        }

                        override fun getName(): String? {
                            return "张三律师"
                        }

                        override fun getAgeLimit(): String? {
                            return "z执业7年"
                        }

                        override fun getCompanyName(): String? {
                            return "杭州泰杭律师事务所"
                        }

                        override fun getLocation(): String? {
                            return "浙江省杭州市余杭区"
                        }
                    })
                }

                delay(2000)

                mView.showRecyclerView(View.VISIBLE, false)
                mView.hideInitLoading(false)
                mView.showLoadMoreComplete(false)
                mDataListByName.addAll(list)
                mView.showLawyers(list, false)
            } catch (e: Exception) {
                mView.showRecyclerView(View.GONE, false)
                mView.showLoadingError("error", false)
            }
        }
    }

}
package com.hm.iou.lawyer.business.user.find

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.InputFilter
import android.view.View
import android.view.inputmethod.EditorInfo
import com.hm.iou.base.mvp.HMBaseActivity
import com.hm.iou.lawyer.R
import com.hm.iou.lawyer.business.NavigationHelper
import com.hm.iou.tools.kt.clickWithDuration
import com.hm.iou.uikit.HMLoadMoreView
import com.hm.iou.uikit.HMTopBarView
import kotlinx.android.synthetic.main.lawyer_activity_find_lawyer.*
import kotlinx.android.synthetic.main.lawyer_layout_search_by_name.*

/**
 * Created by hjy on 2019/11/11
 *
 * 查找律师
 *
 */
class FindLawyerActivity : HMBaseActivity<FindLawyerPresenter>(), FindLawyerContract.View {

    private var mLayoutSearchByName: View? = null
    private var mAgeLimitPopWindow: LawyerAgeLimitPopWindow? = null

    private val mAdapter: LawyerListAdapter = LawyerListAdapter(this)
    private val mAdapterFindByName = LawyerListAdapter(this)

    override fun getLayoutId(): Int = R.layout.lawyer_activity_find_lawyer

    override fun initPresenter(): FindLawyerPresenter = FindLawyerPresenter(this, this)

    override fun initEventAndData(savedInstanceState: Bundle?) {
        topbar.setOnMenuClickListener(object : HMTopBarView.OnTopBarMenuClickListener {
            override fun onClickTextMenu() {}

            override fun onClickImageMenu() {
                ensureInitSearchByNameView()
                mLayoutSearchByName!!.visibility = View.VISIBLE
                et_search_content.setSelection(et_search_content.length())
                et_search_content.requestFocus()
                showSoftKeyboard(et_search_content)
            }
        })
        ll_search_by_year.clickWithDuration {
            if (mAgeLimitPopWindow == null) {
                mAgeLimitPopWindow = LawyerAgeLimitPopWindow(this)
                mAgeLimitPopWindow!!.attachArrowView(iv_search_arrow)
                mAgeLimitPopWindow!!.setOnAgeLimitListener(object : OnAgeLimitListener {
                    override fun onSelected(position: Int, item: String) {
                        tv_search_age_limit.text = item
                        mPresenter.findByAgeLimit(position)
                        mPresenter.getNextPageByAgeLimit()
                    }
                })
            }
            mAgeLimitPopWindow!!.showAsDropDown(ll_search_by_year)
        }

        rv_search_list.layoutManager = LinearLayoutManager(this)
        rv_search_list.adapter = mAdapter
        mAdapter.bindToRecyclerView(rv_search_list)
        mAdapter.setLoadMoreView(HMLoadMoreView())
        mAdapter.setOnLoadMoreListener({
            mPresenter.getNextPageByAgeLimit()
        }, rv_search_list)
        mAdapter.setOnItemClickListener { adapter, _, position ->
            val item = adapter.getItem(position) as? ILawyerItem
            item?.let {
                NavigationHelper.toLawyerDetailPage(this, it.getLawyerId() ?: "")
            }
        }

        mPresenter.findByAgeLimit(0)
        mPresenter.getNextPageByAgeLimit()
    }

    override fun onBackPressed() {
        if (mAgeLimitPopWindow?.isShowing == true) {
            mAgeLimitPopWindow?.dismiss()
            return
        }
        super.onBackPressed()
    }

    override fun showInitLoading(isFindByAgeLimit: Boolean) {
        if (isFindByAgeLimit) {
            loading_view.showDataLoading()
        } else {
            ensureInitSearchByNameView()
            loading_view_name.showDataLoading()
        }
    }

    override fun hideInitLoading(isFindByAgeLimit: Boolean) {
        if (isFindByAgeLimit) {
            loading_view.visibility = View.GONE
        } else {
            ensureInitSearchByNameView()
            loading_view_name.visibility = View.GONE
        }
    }

    override fun showLoadingError(msg: String, isFindByAgeLimit: Boolean) {
        if (isFindByAgeLimit) {
            loading_view.showDataFail(msg) {
                mPresenter.getNextPageByAgeLimit()
            }
        } else {
            ensureInitSearchByNameView()
            loading_view_name.showDataFail(msg) {
                mPresenter.getNextPageByLawyerName()
            }
        }
    }

    override fun showDataEmpty(isFindByAgeLimit: Boolean) {
        if (isFindByAgeLimit) {
            loading_view.showDataEmpty("该律师不存在哦~~")
        } else {
            ensureInitSearchByNameView()
            loading_view_name.showDataEmpty("该律师不存在哦~~")
        }
    }

    override fun clearList(isFindByAgeLimit: Boolean) {
        if (isFindByAgeLimit) {
            mAdapter.setNewData(null)
        } else {
            ensureInitSearchByNameView()
            mAdapterFindByName.setNewData(null)
        }
    }

    override fun showLawyers(list: List<ILawyerItem>, isFindByAgeLimit: Boolean) {
        if (isFindByAgeLimit) {
            mAdapter.addData(list)
        } else {
            ensureInitSearchByNameView()
            mAdapterFindByName.addData(list)
        }
    }

    override fun showLoadMoreEnd(isFindByAgeLimit: Boolean) {
        if (isFindByAgeLimit) {
            mAdapter.loadMoreEnd()
        } else {
            ensureInitSearchByNameView()
            mAdapterFindByName.loadMoreEnd()
        }
    }

    override fun showLoadMoreComplete(isFindByAgeLimit: Boolean) {
        if (isFindByAgeLimit) {
            mAdapter.loadMoreComplete()
        } else {
            ensureInitSearchByNameView()
            mAdapterFindByName.loadMoreComplete()
        }
    }

    override fun showRecyclerView(visibility: Int, isFindByAgeLimit: Boolean) {
        if (isFindByAgeLimit) {
            rv_search_list.visibility = visibility
        } else {
            ensureInitSearchByNameView()
            rv_search_list_name.visibility = visibility
        }
    }

    private fun ensureInitSearchByNameView() {
        if (mLayoutSearchByName == null) {
            mLayoutSearchByName = vs_search_by_name.inflate()
            et_search_content.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 先隐藏键盘
                    hideSoftKeyboard()
                    // 搜索，进行自己要的操作..
                    val text = et_search_content.text.toString()
                    if (!text.isNullOrEmpty()) {
                        mPresenter.findByLawyerName(text)
                        mPresenter.getNextPageByLawyerName()
                    }
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }
            val filterList = mutableListOf<InputFilter>()
            filterList.add(InputFilter.LengthFilter(5))
            et_search_content.filters = filterList.toTypedArray()
            tv_search_cancel.clickWithDuration {
                mLayoutSearchByName!!.visibility = View.GONE
                hideSoftKeyboard()
            }

            rv_search_list_name.layoutManager = LinearLayoutManager(this)
            rv_search_list_name.adapter = mAdapterFindByName
            mAdapterFindByName.setLoadMoreView(HMLoadMoreView())
            mAdapterFindByName.bindToRecyclerView(rv_search_list_name)
            mAdapterFindByName.setOnLoadMoreListener({
                mPresenter.getNextPageByLawyerName()
            }, rv_search_list_name)

            mAdapterFindByName.setOnItemClickListener { adapter, _, position ->
                val item = adapter.getItem(position) as? ILawyerItem
                item?.let {
                    NavigationHelper.toLawyerDetailPage(this, it.getLawyerId() ?: "")
                }
            }
        }
    }

}
package com.hm.iou.lawyer.business.lawyer.workbench.list

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.View
import com.hm.iou.lawyer.dict.LawyerOrderTabStatus
import java.util.*

/**
 * @author : 借条管家-shilei
 * @version : 0.0.1
 * @Date : 2019-11-14 20:23
 * @E-Mail : afinalstone@foxmail.com
 */
class MyOrderPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val mOrderStatus = arrayOf(
        LawyerOrderTabStatus.ALL,
        LawyerOrderTabStatus.LOADING,
        LawyerOrderTabStatus.FINISH,
        LawyerOrderTabStatus.CANCEL
    )

    internal var list: MutableList<Fragment> = ArrayList()

    init {
        for (status in mOrderStatus) {
            list.add(MyOrderListPageFragment.newInstance(status))
        }
    }

    fun getPositionByType(orderStatus: Int): Int {
        for (i in mOrderStatus.indices) {
            if (orderStatus == mOrderStatus[i].status)
                return i
        }
        return 0
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mOrderStatus[position].statusName
    }

    override fun getCount(): Int {
        return mOrderStatus.size
    }

    override fun getItem(position: Int): Fragment {
        return list[position]
    }

}
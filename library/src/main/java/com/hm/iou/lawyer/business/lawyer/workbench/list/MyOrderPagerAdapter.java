//package com.hm.iou.lawyer.business.lawyer.workbench.list;
//
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentPagerAdapter;
//
//import com.hm.iou.jietiao.bean.dict.IOUEnum;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by hjy on 18/4/30.<br>
// */
//public class MyOrderPagerAdapter extends FragmentPagerAdapter {
//
//    private final String[] titles = {"吕约\n借条", "吕约\n欠条", "吕约\n收条", "纸质\n借条", "纸质\n收条", "房贷\n合同", "房租\n合同", "平台\n债务", "信用\n卡债"};
//
//    private final int[] iouTypes = {
//            IOUEnum.MoneyElecBorrower.getValue(),
//            IOUEnum.MoneyQianTiao.getValue(),
//            IOUEnum.MoneyElecRecv.getValue(),
//            IOUEnum.PaperBorrower.getValue(),
//            IOUEnum.PaperRecv.getValue(),
//            IOUEnum.FdContract.getValue(),
//            IOUEnum.FzContract.getValue(),
//            IOUEnum.AgencyBorrower.getValue(),
//            IOUEnum.CreditCard.getValue()
//    };
//
//    List<Fragment> list = new ArrayList<>();
//
//    public MyOrderPagerAdapter(FragmentManager fm) {
//        super(fm);
//        for (int type : iouTypes) {
//            HistoryIouFragment fragment = HistoryIouFragment.newInstance(type);
//            list.add(fragment);
//        }
//    }
//
//    public int getPositionByType(int iouType) {
//        for (int i = 0; i < iouTypes.length; i++) {
//            if (iouType == iouTypes[i])
//                return i;
//        }
//        return 0;
//    }
//
//    @Override
//    public CharSequence getPageTitle(int position) {
//        return titles[position];
//    }
//
//    @Override
//    public int getCount() {
//        return titles.length;
//    }
//
//    @Override
//    public Fragment getItem(int position) {
//        return list.get(position);
//    }
//}
//

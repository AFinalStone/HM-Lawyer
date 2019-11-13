package com.hm.iou.lawyer.bean.res

/**
 * @author : 借条管家-shilei
 * @version : 0.0.1
 * @Date : 2019-11-12 14:10
 * @E-Mail : afinalstone@foxmail.com
 */
data class GetLawyerWorkBenchResBean(
    val doingNum: Int,//进行中的数量
    val inviteLetterNum: Int,//邀请接单数量
    val todayAcceptNum: Int,//今日接单
    val todayFinishNum: Int,//今日完成
    val todayIncome: String,//今日收益
    val walletBalance: String//钱包余额
)
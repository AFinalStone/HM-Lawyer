package com.hm.iou.lawyer.api

import com.hm.iou.lawyer.bean.req.LawyerAuthenticationReqBean
import com.hm.iou.lawyer.bean.req.UpdateLawyerServicePriceReqBean
import com.hm.iou.lawyer.bean.req.WithDrawMoneyReqBean
import com.hm.iou.lawyer.bean.res.*
import com.hm.iou.network.HttpReqManager
import com.hm.iou.sharedata.model.BaseResponse
import com.hm.iou.sharedata.model.UserThirdPlatformInfo

/**
 * Created by syl on 2019/8/5.
 */
object LawyerApi {


    private fun getService(): LawyerService {
        return HttpReqManager.getInstance().getService(LawyerService::class.java)
    }


    /**
     * 获取用户绑定的第三方平台信息,这里主要为了获取银行卡信息
     *
     * @return
     */
    suspend fun getUserThirdPlatformInfo(): BaseResponse<UserThirdPlatformInfo> {
        return getService().getUserThirdPlatformInfo()
    }

    /**
     * 获取律师首页状态
     *
     * @return
     */
    suspend fun getLawyerHomeStatus(): BaseResponse<GetLawyerHomeStatusResBean> {
        return getService().getLawyerHomeStatus()
    }

    /**
     * 律师首页详情
     *
     * @return
     */
    suspend fun getLawyerHomeDetail(lawyerId: String): BaseResponse<GetLawyerHomeDetailResBean> {
        return getService().getLawyerHomeDetail(lawyerId)
    }

    /**
     * 律师认证
     *
     * @return
     */
    suspend fun lawyerAuthentication(req: LawyerAuthenticationReqBean): BaseResponse<LawyerAuthenticationResBean> {
        return getService().lawyerAuthentication(req)
    }

    /**
     * 更新律师认证的相关信息
     *
     * @return
     */
    suspend fun updateLawyerAuthenticationInfo(req: LawyerAuthenticationReqBean): BaseResponse<LawyerAuthenticationResBean> {
        return getService().updateLawyerAuthenticationInfo(req)
    }

    /**
     * 更新律师服务费
     *
     * @return
     */
    suspend fun updateLawyerServicePrice(req: UpdateLawyerServicePriceReqBean): BaseResponse<Any> {
        return getService().updateLawyerServicePrice(req)
    }

    /**
     * 获取律师工作台数据
     *
     * @return
     */
    suspend fun getLawyerWorkBench(): BaseResponse<GetLawyerWorkBenchResBean> {
        return getService().getLawyerWorkBench()
    }

    /**
     * 获取律师我的钱包
     *
     * @return
     */
    suspend fun getLawyerWallet(): BaseResponse<GetLawyerWalletResBean> {
        return getService().getLawyerWallet()
    }

    /**
     * 计算服务费率
     *
     * @return
     */
    suspend fun calcLawyerWithDrawRate(fen: Int): BaseResponse<CalcaLawyerWithDrawRateResBean> {
        return getService().calcLawyerWithDrawRate(fen)
    }

    /**
     * 申请体现
     *
     * @return
     */
    suspend fun withDrawMoney(req: WithDrawMoneyReqBean): BaseResponse<Any> {
        return getService().withDrawMoney(req)
    }


}
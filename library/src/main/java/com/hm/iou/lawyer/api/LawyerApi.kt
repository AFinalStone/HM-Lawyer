package com.hm.iou.lawyer.api

import com.hm.iou.lawyer.bean.req.*
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
     * 获取律师认证失败的信息
     *
     * @return
     */
    suspend fun getLawyerAuthenticationFailedInfo(): BaseResponse<LawyerAuthenticationResBean> {
        return getService().getLawyerAuthenticationFailedInfo()
    }

    /**
     * 更新律师认证的相关信息
     *
     * @return
     */
    suspend fun updateLawyerAuthenticationInfo(req: UpdateLawyerAuthenticationInfReqBean): BaseResponse<LawyerAuthenticationResBean> {
        return getService().updateLawyerAuthenticationInfo(req)
    }

    /**
     * 更新律师服务费
     *
     * @return
     */
    suspend fun updateLawyerServicePrice(price: Int, serviceId: Int): BaseResponse<Any> {
        val req = UpdateLawyerServicePriceReqBean(price, serviceId)
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

    /**
     * 查询律师列表
     *
     * 1年~3年, yearLimit = 1, 3年~5年, yearLimit = 2, 5年~10年, yearLimit = 3, 10年以上, yearLimit = 4
     */
    suspend fun getLawyerList(
        page: Int,
        size: Int,
        yearLimit: Int,
        lawyerNameKey: String?
    ): BaseResponse<LawyerListResBean> {
        return getService().getLawyerList(page, size, yearLimit, lawyerNameKey)
    }

    /**
     * 创建律师函
     */
    suspend fun createLawyerLetter(reqBean: CreateLawyerLetterReqBean): BaseResponse<CreateLawyerLetterResBean> {
        return getService().createLawyerLetter(reqBean)
    }

    /**
     * 用户取消订单
     */
    suspend fun cancelCustLawyerLetter(billId: String): BaseResponse<Any> {
        return getService().cancelCustLawyerLetter(billId)
    }

    /**
     * 获取律师函详情
     */
    suspend fun getCustLawyerLetterDetail(billId: String): BaseResponse<CustLetterDetailResBean> {
        return getService().getCustLawyerLetterDetail(billId)
    }

    /**
     * 获取我的订单列表
     *
     * @param page 页码，从1开始
     * @param size 每页大小
     */
    suspend fun getCustOrderList(page: Int, size: Int): BaseResponse<CustOrderListResBean> {
        return getService().getCustOrderList(CustOrderPageReqBean(page, size))
    }

    /**
     * 对律师进行评价
     */
    suspend fun ratingLawyer(
        billId: String,
        content: String,
        attitudeScore: Int,
        professionalScore: Int
    ): BaseResponse<Any> {
        val reqBean = RatingLawyerReqBean()
        reqBean.billId = billId
        reqBean.content = content
        reqBean.attitudeScore = attitudeScore
        reqBean.professionalScore = professionalScore
        return getService().ratingLawyer(reqBean)
    }

    /**
     * 律师模块我的订单列表
     */
    suspend fun getLawyerMyOrderList(
        page: Int,
        size: Int,
        status: Int
    ): BaseResponse<LawyerOrderListResBean> {
        val reqBean = GetLawyerMyOrderListReqBean(page, size, status)
        return getService().getLawyerMyOrderList(reqBean)
    }

    /**
     * 律师模块律师函订单列表
     */
    suspend fun getLawyerLetterList(
        page: Int,
        size: Int
    ): BaseResponse<LawyerOrderListResBean> {
        val reqBean = GetLawyerLetterOrderListReqBean(page, size)
        return getService().getLawyerLetterList(reqBean)
    }

    /**
     * 律师模块邀请接单订单列表
     */
    suspend fun getLawyerInviteList(
        page: Int,
        size: Int
    ): BaseResponse<LawyerOrderListResBean> {
        val reqBean = GetLawyerInviteOrderListReqBean(page, size)
        return getService().getLawyerInviteList(reqBean)
    }

    /**
     * 律师函详情信息
     */
    suspend fun getLawyerLetterDetail(
        billId: String,
        relationId: Int?
    ): BaseResponse<LawyerLetterDetailBean> {
        val reqBean = GetLawyerLetterDetailReqBean(billId, relationId)
        return getService().getLawyerLetterDetail(reqBean)
    }

}
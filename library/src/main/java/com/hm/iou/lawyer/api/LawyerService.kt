package com.hm.iou.lawyer.api

import com.hm.iou.lawyer.bean.req.*
import com.hm.iou.lawyer.bean.res.*
import com.hm.iou.sharedata.model.BaseResponse
import com.hm.iou.sharedata.model.UserThirdPlatformInfo
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


/**
 * Created by syl on 2019/8/5.
 */
interface LawyerService {

    @GET("/pay/verify/v1/selectVerifyInfo")
    suspend fun getUserThirdPlatformInfo(): BaseResponse<UserThirdPlatformInfo>

    @GET("/api/lawyer/v1/lawyer/auth/process")
    suspend fun getLawyerHomeStatus(): BaseResponse<GetLawyerHomeStatusResBean>

    @GET("/api/lawyer/v1/lawyer/detail")
    suspend fun getLawyerHomeDetail(@Query("lawyerId") lawyerId: String): BaseResponse<GetLawyerHomeDetailResBean>

    @POST("/api/lawyer/v1/lawyer/auth/create")
    suspend fun lawyerAuthentication(@Body req: LawyerAuthenticationReqBean): BaseResponse<LawyerAuthenticationResBean>

    @POST("/api/lawyer/v1/lawyer/auth/update")
    suspend fun updateLawyerAuthenticationInfo(@Body req: UpdateLawyerAuthenticationInfReqBean): BaseResponse<LawyerAuthenticationResBean>

    @POST("/api/lawyer/v1/lawyer/service/price/update")
    suspend fun updateLawyerServicePrice(@Body req: UpdateLawyerServicePriceReqBean): BaseResponse<Any>

    @GET("/api/lawyer/v1/lawyer/workbench")
    suspend fun getLawyerWorkBench(): BaseResponse<GetLawyerWorkBenchResBean>

    @GET("/api/lawyer/v1/wallet")
    suspend fun getLawyerWallet(): BaseResponse<GetLawyerWalletResBean>

    @GET("/api/lawyer/v1/fee/calc")
    suspend fun calcLawyerWithDrawRate(@Query("fen") fen: Int): BaseResponse<CalcaLawyerWithDrawRateResBean>

    @POST("/api/lawyer/v1/withdraw")
    suspend fun withDrawMoney(@Body req: WithDrawMoneyReqBean): BaseResponse<Any>

    @GET("/api/lawyer/v1/lawyer/list")
    suspend fun getLawyerList(
        @Query("page") page: Int, @Query("size") size: Int,
        @Query("yearLimit") yearLimit: Int, @Query("lawyerNameKey") lawyerNameKey: String?
    ): BaseResponse<LawyerListResBean>

    @POST("/api/lawyer/v1/letter/custApplyLetter")
    suspend fun createLawyerLetter(@Body reqBean: CreateLawyerLetterReqBean): BaseResponse<CreateLawyerLetterResBean>

    @POST("/api/lawyer/v1/letter/custCancelBill")
    suspend fun cancelCustLawyerLetter(@Query("billId") billId: String): BaseResponse<Any>

    @GET("/api/lawyer/v1/letter/custLetterDetail")
    suspend fun getCustLawyerLetterDetail(@Query("billId") billId: String): BaseResponse<CustLetterDetailResBean>

    @POST("/api/lawyer/v1/custBill")
    suspend fun getCustOrderList(@Body reqBean: CustOrderPageReqBean): BaseResponse<CustOrderListResBean>

    @POST("/api/lawyer/v1/custEvaluationLawyer")
    suspend fun ratingLawyer(@Body reqBean: RatingLawyerReqBean): BaseResponse<Any>

    @POST("/api/lawyer/v1/lawyerBill")
    suspend fun getLawyerMyOrderList(@Body reqBean: LawyerMyOrderPageReqBean): BaseResponse<LawyerMyOrderListResBean>

}
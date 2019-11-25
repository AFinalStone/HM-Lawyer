package com.hm.iou.lawyer.api

import com.hm.iou.lawyer.bean.ConsultReplyItemBean
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

    @GET("/api/lawyer/v1/lawyer/auth/fail/detail")
    suspend fun getLawyerAuthenticationFailedInfo(): BaseResponse<LawyerAuthenticationResBean>

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
    suspend fun withDrawMoney(@Body req: WithDrawMoneyReqBean): BaseResponse<String>

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
    suspend fun ratingLawyer(@Body reqBean: RatingLawyerReqBean): BaseResponse<Boolean>

    @POST("/api/lawyer/v1/lawyerBill")
    suspend fun getLawyerMyOrderList(@Body reqBean: GetLawyerMyOrderListReqBean): BaseResponse<LawyerOrderListResBean>

    @POST("/api/lawyer/v1/letter/lawyerLetterWaitList")
    suspend fun getLawyerLetterList(@Body reqBean: GetLawyerLetterOrderListReqBean): BaseResponse<LawyerOrderListResBean>

    @POST("/api/lawyer/v1/letter/lawyerInviteWaitList")
    suspend fun getLawyerInviteList(@Body reqBean: GetLawyerInviteOrderListReqBean): BaseResponse<LawyerOrderListResBean>

    @POST("/api/lawyer/v1/letter/lawyerLetterDetail")
    suspend fun getLawyerLetterDetail(@Body reqBean: GetLawyerLetterDetailReqBean): BaseResponse<LawyerLetterDetailBean>

    @GET("/api/lawyer/v1/letter/canAcceptBill")
    suspend fun checkLawyerCanAcceptOrder(@Query("billId") billId: String): BaseResponse<CheckLawyerCanAcceptOrderResBean>

    @GET("/api/lawyer/v1/letter/lawyerAcceptBill")
    suspend fun lawyerAcceptOrder(@Query("billId") billId: String): BaseResponse<Any>

    @GET("/api/lawyer/v1/letter/lawyerCancelBill")
    suspend fun lawyerCancelOrder(@Query("billId") billId: String): BaseResponse<Int>

    @GET("/api/lawyer/v1/letter/lawyerRefuseBill")
    suspend fun lawyerRefuseOrder(@Query("billId") billId: String): BaseResponse<Int>

    @GET("/api/lawyer/v1/express/list")
    suspend fun getMailList(): BaseResponse<ArrayList<String>>

    @POST("/api/lawyer/v1/letter/lawyerFinishLetter")
    suspend fun lawyerFinishOrder(@Body reqBean: LawyerFinishOrderReqBean): BaseResponse<Any>

    @POST("/api/lawyer/v1/consultation/lawyerConsultationWaitList")
    suspend fun getLawyerConsultOrderList(@Body reqBean: GetLawyerConsultOrderListReqBean): BaseResponse<List<LawyerConsultOrderItemBean>>

    @POST("/api/lawyer/v1/consultation/lawyerConsultationDetail")
    suspend fun getLawyerConsultOrderDetail(@Body reqBean: LawyerConsultOrderDetailReqBean): BaseResponse<LawyerConsultOrderDetailResBean>

    @POST("/api/lawyer/v1/consultation/reply/lawyer")
    suspend fun lawyerAnswer(@Body reqBean: LawyerAnswerReqBean): BaseResponse<Any>

    @POST("/api/lawyer/v1/consultation/custApplyConsultation")
    suspend fun createLawyerConsult(@Body reqBean: CreateLawyerConsultReqBean): BaseResponse<CreateLawyerLetterResBean>

    @GET("/api/lawyer/v1/consultation/custConsultationDetail")
    suspend fun getConsultDetail(@Query("billId") billId: String): BaseResponse<LawyerConsultDetailResBean>

    @POST("/api/lawyer/v1/consultation/reply/cust")
    suspend fun addConsultQuestion(@Body reqBean: AddConsultQuestionReqBean): BaseResponse<ConsultReplyItemBean>

    @GET("/api/lawyer/v1/consultation/replies")
    suspend fun getConsultReplayList(@Query("billId") billId: String): BaseResponse<List<ConsultReplyItemBean>>

}
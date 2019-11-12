package com.hm.iou.lawyer.api

import com.hm.iou.lawyer.bean.req.LawyerAuthenticationReqBean
import com.hm.iou.lawyer.bean.res.GetLawyerHomeStatusResBean
import com.hm.iou.lawyer.bean.res.LawyerAuthenticationResBean
import com.hm.iou.sharedata.model.BaseResponse
import com.hm.iou.sharedata.model.UserThirdPlatformInfo
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


/**
 * Created by syl on 2019/8/5.
 */
interface LawyerService {

    @GET("/pay/verify/v1/selectVerifyInfo")
    suspend fun getUserThirdPlatformInfo(): BaseResponse<UserThirdPlatformInfo>

    @GET("/api/lawyer/v1/lawyer/auth/process")
    suspend fun getLawyerHomeStatus(): BaseResponse<GetLawyerHomeStatusResBean>

    @POST("/api/lawyer/v1/lawyer/auth/create")
    suspend fun lawyerAuthentication(@Body req: LawyerAuthenticationReqBean): BaseResponse<LawyerAuthenticationResBean>

}
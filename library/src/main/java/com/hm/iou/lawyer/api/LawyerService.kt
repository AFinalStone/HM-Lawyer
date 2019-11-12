package com.hm.iou.lawyer.api

import com.hm.iou.sharedata.model.BaseResponse
import com.hm.iou.sharedata.model.UserThirdPlatformInfo
import retrofit2.http.GET


/**
 * Created by syl on 2019/8/5.
 */
interface LawyerService {

    @GET("/pay/verify/v1/selectVerifyInfo")
    suspend fun getUserThirdPlatformInfo(): BaseResponse<UserThirdPlatformInfo>

}
package com.hm.iou.lawyer.api

import com.hm.iou.lawyer.bean.res.GetLawyerHomeStatusResBean
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


}
package com.hm.iou.lawyer.api

import com.hm.iou.network.HttpReqManager

/**
 * Created by syl on 2019/8/5.
 */
object LawyerApi {


    private fun getService(): LawyerService {
        return HttpReqManager.getInstance().getService(LawyerService::class.java)
    }


}
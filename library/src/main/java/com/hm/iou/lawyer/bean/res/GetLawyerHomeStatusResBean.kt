package com.hm.iou.lawyer.bean.res

/**
 * @author : 借条管家-shilei
 * @version : 0.0.1
 * @Date : 2019-11-12 14:10
 * @E-Mail : afinalstone@foxmail.com
 */
class GetLawyerHomeStatusResBean {
    /**
     * 首次审核状态：
     * 0：没有提交过审核
     * 2：首次认证审核中
     * 3：首次认证审核通过
     * 4：首次认证审核不通过
     */
    var firstAuthState: Int? = null
    /**
     * 更新执业机构状态：
     * 9：更新执业机构审核中
     * 10：更细执业机构审核通过
     * 11：更新执业机构审核不通过
     */
    var updateLawFirmState: Int? = null
    /**
     * 更新年检状态：
     * 5：需要更新年检信息
     * 6：更新年检审核中
     * 7：更新年检审核通过
     * 8：更新年检审核不通过
     */
    var updateYearCheckState: Int? = null
}
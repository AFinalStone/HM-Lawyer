package com.hm.iou.lawyer.business.comm

import android.content.Context
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hm.iou.lawyer.R

/**
 * Created by hjy on 2019-11-22
 *
 * 律师解答 adapter
 */
class ConsultAnswerAdapter : BaseQuickAdapter<IAnswer, BaseViewHolder> {

    constructor(context: Context) : super((R.layout.lawyer_item_consult_answer)) {
        mContext = context
    }

    override fun convert(helper: BaseViewHolder?, item: IAnswer?) {
        helper ?: return
        item ?: return
    }
}

interface IAnswer {

}



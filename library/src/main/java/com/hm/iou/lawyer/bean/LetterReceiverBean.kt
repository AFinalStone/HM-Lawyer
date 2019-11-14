package com.hm.iou.lawyer.bean

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

/**
 * Created by hjy on 2019/11/12
 *
 * 律师函接收者信息
 */
class LetterReceiverBean constructor() : Parcelable, Serializable {

    var receiverName: String? = null
    var receiverIdCardNum: String? = null
    var receiverMobile: String? = null
    var receiverCityDetail: String? = null
    var receiverDetailAddress: String? = null

    constructor(source: Parcel) : this() {
        receiverName = source.readString()
        receiverIdCardNum = source.readString()
        receiverMobile = source.readString()
        receiverCityDetail = source.readString()
        receiverDetailAddress = source.readString()
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(receiverName)
        writeString(receiverIdCardNum)
        writeString(receiverMobile)
        writeString(receiverCityDetail)
        writeString(receiverDetailAddress)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<LetterReceiverBean> =
            object : Parcelable.Creator<LetterReceiverBean> {
                override fun createFromParcel(source: Parcel): LetterReceiverBean =
                    LetterReceiverBean(source)

                override fun newArray(size: Int): Array<LetterReceiverBean?> = arrayOfNulls(size)
            }
    }
}
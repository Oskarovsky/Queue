package com.oskarro.queue.model

import android.os.Parcel
import android.os.Parcelable

data class Sheet(
    val id: String = "",
    val name: String = "",
    val tabName: String = "",
    val isActive: Boolean = false,
    val url: String = ""
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readByte() != 0.toByte(),
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(tabName)
        parcel.writeByte(if (isActive) 1 else 0)
        parcel.writeString(url)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Sheet> {
        override fun createFromParcel(parcel: Parcel): Sheet {
            return Sheet(parcel)
        }

        override fun newArray(size: Int): Array<Sheet?> {
            return arrayOfNulls(size)
        }
    }
}

package com.oskarro.queue.model

import android.os.Parcel
import android.os.Parcelable

data class Process(
    var title: String = "",
    val createdBy: String = "",
    val products: ArrayList<Product> = ArrayList(),
    var orderNumber: String = "",
    var amount: String = "",
    var description: String = "",
    val stage: String = ""
): Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.createTypedArrayList(Product.CREATOR)!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(title)
        writeString(orderNumber)
        writeTypedList(products)
        writeString(amount)
        writeString(description)
        writeString(createdBy)
        writeString(stage)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Process> {
        override fun createFromParcel(parcel: Parcel): Process {
            return Process(parcel)
        }

        override fun newArray(size: Int): Array<Process?> {
            return arrayOfNulls(size)
        }
    }
}

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
        writeString(createdBy)
        writeTypedList(products)
        writeString(orderNumber)
        writeString(amount)
        writeString(description)
        writeString(stage)
    }

    override fun describeContents(): Int = 0

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Process> = object : Parcelable.Creator<Process> {
            override fun createFromParcel(source: Parcel): Process = Process(source)
            override fun newArray(size: Int): Array<Process?> = arrayOfNulls(size)
        }
    }
}

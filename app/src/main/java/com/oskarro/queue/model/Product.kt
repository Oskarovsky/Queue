package com.oskarro.queue.model

import android.os.Parcel
import android.os.Parcelable
import android.os.WorkSource

data class Product (
    val name: String = "",
    val createdBy: String = "",
    val assignedTo: ArrayList<String> = ArrayList(),
    var orderNumber: String = "",
    var amount: String = "",
    var description: String = "",
    val stage: String = ""
) : Parcelable {

    constructor(source: Parcel) : this(
        source.readString()!!,
        source.readString()!!,
        source.createStringArrayList()!!,
        source.readString()!!,
        source.readString()!!,
        source.readString()!!,
        source.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) = with(parcel) {
        writeString(name)
        writeString(createdBy)
        writeStringList(assignedTo)
        writeString(orderNumber)
        writeString(amount)
        writeString(description)
        writeString(stage)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Product> = object : Parcelable.Creator<Product> {
            override fun createFromParcel(source: Parcel): Product = Product(source)
            override fun newArray(size: Int): Array<Product?> = arrayOfNulls(size)
        }
    }

    override fun describeContents(): Int = 0
}
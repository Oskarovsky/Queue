package com.oskarro.queue.model

import android.os.Parcel
import android.os.Parcelable
import android.os.WorkSource

data class Product (
    val name: String = "",
    val createdBy: String = "",
    val assignedTo: ArrayList<String> = ArrayList()
) : Parcelable {

    constructor(source: Parcel) : this(
        source.readString()!!,
        source.readString()!!,
        source.createStringArrayList()!!,
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) = with(parcel) {
        writeString(name)
        writeString(createdBy)
        writeStringList(assignedTo)
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
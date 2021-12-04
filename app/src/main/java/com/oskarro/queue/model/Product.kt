package com.oskarro.queue.model

import android.os.Parcel
import android.os.Parcelable
import android.os.WorkSource
import com.google.api.client.json.Json
import org.json.JSONObject

data class Product (
    val name: String = "",
    val createdBy: String = "",
    val assignedTo: ArrayList<String> = ArrayList(),
    var orderNumber: String = "",
    var invoiceNumber: String = "",
    var dateOfOrder: String = "",
    var amount: String = "",
    var price: String = "",
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
        writeString(invoiceNumber)
        writeString(dateOfOrder)
        writeString(amount)
        writeString(price)
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

class ProductResponse(json: String) : JSONObject(json) {
    val data = this.optJSONArray("data")
        ?.let { 0.until(it.length()).map { i -> it.optJSONObject(i) } } // returns an array of JSONObject
        ?.map { ProductDto(it.toString()) } // transforms each JSONObject of the array into Foo
}

data class ProductDto(
    var orderNumber: String = "",
    var name: String = "",
    var stage: Stage = Stage.ERROR,
    var invoiceNumber: String = "",
    var client: String = "",
    var productType: String = "",
    var quantity: String = "",
    var price: String = "",
)
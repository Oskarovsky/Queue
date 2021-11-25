package com.oskarro.queue.model

import android.os.Parcel
import android.os.Parcelable

data class Board (
    val name: String = "",
    val createdBy: String = "",
    val assignedTo: ArrayList<String> = ArrayList(),
    var documentId: String = ""
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readString()!!,
        source.readString()!!,
        source.createStringArrayList()!!,
        source.readString()!!
    ) {
    }

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest){
        dest.writeString(name)
        dest.writeString(createdBy)
        dest.writeStringList(assignedTo)
        dest.writeString(documentId)
    }

    override fun describeContents(): Int = 0

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Board> = object : Parcelable.Creator<Board> {
            override fun createFromParcel(source: Parcel): Board = Board(source)
            override fun newArray(size: Int): Array<Board?> = arrayOfNulls(size)
        }
    }
}

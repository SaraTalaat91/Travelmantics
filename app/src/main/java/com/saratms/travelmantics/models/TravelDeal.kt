package com.saratms.travelmantics.models

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Sarah Al-Shamy on 03/08/2019.
 */
data class TravelDeal(
    var id: String? = "",
    var title: String? = "",
    var description: String? = "",
    var price: String? = "",
    var imageUrl: String? = "",
    var imageName: String? = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(id)
        dest.writeString(title)
        dest.writeString(description)
        dest.writeString(price)
        dest.writeString(imageUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TravelDeal> {
        override fun createFromParcel(parcel: Parcel): TravelDeal {
            return TravelDeal(parcel)
        }

        override fun newArray(size: Int): Array<TravelDeal?> {
            return arrayOfNulls(size)
        }
    }
}
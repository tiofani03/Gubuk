package com.tegar.gubuk.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BookStore(
    val id: String? = null,
    val name: String? = null,
    val img: String? = null,
    val lat: String? = null,
    val long: String? = null,
) : Parcelable
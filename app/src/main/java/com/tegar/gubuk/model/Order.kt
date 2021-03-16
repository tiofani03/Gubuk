package com.tegar.gubuk.model

import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import kotlinx.android.parcel.Parcelize

//representasi koleksi Order dari firestore
@Parcelize
data class Order(
        @DocumentId
        var id: String? = null,
        var idUser: String? = null,
        var idBook: String? = null,
        var userName: String? = null,
        var userAddress: String? = null,
        var bookName: String? = null,
        var bookPrice: String? = null,
        val time: Timestamp? = Timestamp.now(),
        var status: String? = null,
) : Parcelable

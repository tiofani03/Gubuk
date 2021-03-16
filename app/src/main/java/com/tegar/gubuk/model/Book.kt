package com.tegar.gubuk.model

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import kotlinx.android.parcel.Parcelize

//representasi koleksi buku dari firestore
@Parcelize
data class Book(
    @DocumentId
    var id: String? = null,
    var title: String? = null,
    var category: String? = null,
    var rating: String? = null,
    var detail: String? = null,
    var author: String? = null,
    var numberPages: String? = null,
    var language: String? = null,
    var imageUrl: String? = null,
    var price: String? = null,
    var status: String? = null,
    var search: String? = null,
) : Parcelable
package com.tegar.gubuk.model

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Users(
    @DocumentId
    var id: String? = null,
    var email: String? = null,
    var name: String? = null,
    var status: String? = null,
    var imageUrl: String? = null
) : Parcelable
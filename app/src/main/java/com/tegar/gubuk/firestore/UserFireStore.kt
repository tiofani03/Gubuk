package com.tegar.gubuk.firestore

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tegar.gubuk.utils.DataSource.mapped
import com.tegar.gubuk.model.Users

object UserFireStore {
    private const val TAG = "UserService"

    fun createNewUser(user: Users, isSuccess: (Boolean) -> Unit) {
        Firebase.firestore.collection("Users")
            .document(Firebase.auth.currentUser?.uid ?: "")
            .set(user)
            .addOnSuccessListener { isSuccess(true) }
            .addOnFailureListener { isSuccess(false) }
    }

    fun getAllUsers(onResult: (List<Users>) -> Unit) {
        Firebase.firestore.collection("Users")
            .addSnapshotListener { value, error ->
                if (error != null) Log.d(TAG, "getCategory Error ${error.message}")
                if (value != null && !value.isEmpty) {
                    onResult(value.toObjects(Users::class.java))
                    Log.d(TAG, "getCategory Success")
                } else {
                    onResult(listOf())
                    if (error != null) Log.d(TAG, "getCategory Error ${error.message}")
                }
            }
    }

    fun getUser(id: String, onResult: (Users) -> Unit) {
        Firebase.firestore.collection("Users")
            .document(id)
            .addSnapshotListener { value, error ->
                if (error != null) Log.d(TAG, "getUser Error ${error.message}")
                if (value != null && value.exists()) {
                    onResult(value.toObject(Users::class.java) ?: Users())
                    Log.d(TAG, "getUser Sukses")
                } else {
                    onResult(Users())
                    if (error != null) Log.d(TAG, "getUser Error ${error.message}")
                }
            }
    }

    fun updateProfile(uid: String, user: Users, isSuccess: (Boolean) -> Unit) {
        Firebase.firestore.collection("Users")
            .document(uid)
            .update(user.mapped())
            .addOnSuccessListener { isSuccess(true) }
            .addOnFailureListener {
                Log.d(TAG, "updateProfileById: failed = ${it.message}")
                isSuccess(false)
            }
    }
}
package com.tegar.gubuk.firestore

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tegar.gubuk.model.Order


object OrderFireStore {
    private const val TAG = "OrderFireStore"

    //menambahkan orderan
    fun addOrder(order: Order, id: String, isSuccess: (Boolean) -> Unit) {
        Firebase.firestore.collection("order")
            .document(id)
            .set(order)
            .addOnSuccessListener { isSuccess(true) }
            .addOnFailureListener { isSuccess(false) }
    }

    //mendapatkan data orderan berdasarkan orderan
    fun getOrderById(idUser: String, onResult: (List<Order>) -> Unit) {
        Firebase.firestore.collection("order")
                //dengna kondisi id user dan diurutkan secara descending
            .whereEqualTo("idUser", idUser)
            .orderBy("time", Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                if (error != null) Log.d(TAG, "getOrderById Error ${error.message}")
                if (value != null && !value.isEmpty) {
                    onResult(value.toObjects(Order::class.java))
                    Log.d(TAG, "getOrderById Success")
                } else {
                    onResult(listOf())
                    if (error != null) Log.d(TAG, "getOrderById Error ${error.message}")
                }
            }
    }


}
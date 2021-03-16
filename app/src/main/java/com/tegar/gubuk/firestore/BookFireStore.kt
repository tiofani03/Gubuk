package com.tegar.gubuk.firestore

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tegar.gubuk.model.Book
import com.tegar.gubuk.model.BookStore
import com.tegar.gubuk.utils.DataSource.mapped

object BookFireStore {
    private const val TAG = "bookFireStore"

    fun addBook(book: Book, id: String, isSuccess: (Boolean) -> Unit) {
        Firebase.firestore.collection("Book")
            .document(id)
            .set(book)
            .addOnSuccessListener { isSuccess(true) }
            .addOnFailureListener { isSuccess(false) }
    }

    fun getAllBook(onResult: (List<Book>) -> Unit) {
        Firebase.firestore.collection("Book")
            .addSnapshotListener { value, error ->
                if (error != null) Log.d(TAG, "getCategory Error ${error.message}")
                if (value != null && !value.isEmpty) {
                    onResult(value.toObjects(Book::class.java))
                    Log.d(TAG, "getCategory Success")
                } else {
                    onResult(listOf())
                    if (error != null) Log.d(TAG, "getCategory Error ${error.message}")
                }
            }
    }

    fun getPopularBook(onResult: (List<Book>) -> Unit) {
        Firebase.firestore.collection("Book")
            .whereEqualTo("status", "news")
            .addSnapshotListener { value, error ->
                if (error != null) Log.d(TAG, "getCategory Error ${error.message}")
                if (value != null && !value.isEmpty) {
                    onResult(value.toObjects(Book::class.java))
                    Log.d(TAG, "getCategory Success")
                } else {
                    onResult(listOf())
                    if (error != null) Log.d(TAG, "getCategory Error ${error.message}")
                }
            }
    }

    fun searchBook(query: String, onResult: (List<Book>) -> Unit) {
        Firebase.firestore.collection("Book")
            .orderBy("search")
            .startAt(query).endAt(query.plus("\uf8ff"))
            .addSnapshotListener { value, error ->
                if (error != null) Log.d(TAG, "searchBook Error ${error.message}")
                if (value != null && !value.isEmpty) {
                    onResult(value.toObjects(Book::class.java))
                    Log.d(TAG, "searchBook Success")
                } else {
                    onResult(listOf())
                    if (error != null) Log.d(TAG, "searchBook Error ${error.message}")
                }
            }
    }


    fun updateBook(id: String, book: Book, isSuccess: (Boolean) -> Unit) {
        Firebase.firestore.collection("Book")
            .document(id)
            .update(book.mapped())
            .addOnSuccessListener { isSuccess(true) }
            .addOnFailureListener {
                Log.d(TAG, "updateProfileById: failed = ${it.message}")
                isSuccess(false)
            }
    }

    fun deleteBook(id: String, isSuccess: (Boolean) -> Unit) {
        Firebase.firestore.collection("Book")
            .document(id)
            .delete()
            .addOnSuccessListener { isSuccess(true) }
            .addOnFailureListener {
                Log.d(TAG, "deletePet: failed = ${it.message}")
                isSuccess(false)
            }
    }

    fun getBookStoreLocation(onResult: (List<BookStore>) -> Unit) {
        Firebase.firestore.collection("bookStore")
            .addSnapshotListener { value, error ->
                if (error != null) Log.d(TAG, "getCategory Error ${error.message}")
                if (value != null && !value.isEmpty) {
                    onResult(value.toObjects(BookStore::class.java))
                    Log.d(TAG, "getCategory Success")
                } else {
                    onResult(listOf())
                    if (error != null) Log.d(TAG, "getCategory Error ${error.message}")
                }
            }
    }
}
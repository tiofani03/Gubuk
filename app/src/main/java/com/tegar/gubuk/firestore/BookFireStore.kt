package com.tegar.gubuk.firestore

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tegar.gubuk.model.Book
import com.tegar.gubuk.model.BookStore
import com.tegar.gubuk.utils.DataSource.mapped

//objek untuk buku mengakses firestore
object BookFireStore {
    private const val TAG = "bookFireStore"

    //menambahkan buku dengan buku dan id sebagai parameter, dan menghasilkan data boolean
    fun addBook(book: Book, id: String, isSuccess: (Boolean) -> Unit) {
        Firebase.firestore.collection("Book")
            .document(id)
            .set(book)
            .addOnSuccessListener { isSuccess(true) }
            .addOnFailureListener { isSuccess(false) }
    }

    //mendapatkan data  buku dengan menghasilkan data list buku
    fun getAllBook(onResult: (List<Book>) -> Unit) {
        Firebase.firestore.collection("Book")
                //menggunakn snapshot agar data realtime
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

    //mendapatkan data populer dari buku
    fun getPopularBook(onResult: (List<Book>) -> Unit) {
        Firebase.firestore.collection("Book")
                //dengan kondisi status == news
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

    //mencari buku
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


    //mengupdate buku yang digunakan oleh admin
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

    //menghapus buku yang digunakan oleh admin
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

    //mendapatkan lokasi dari toko buku
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
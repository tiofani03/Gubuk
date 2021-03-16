package com.tegar.gubuk.utils

import android.content.Context
import com.tegar.gubuk.R
import com.tegar.gubuk.model.Book
import com.tegar.gubuk.model.Users
import java.util.*

object DataSource {
    fun getListData(context: Context): ArrayList<Book> {
        val bookName: Array<String> = context.resources.getStringArray(R.array.book_name)
        val bookCategory: Array<String> = context.resources.getStringArray(R.array.book_category)
        val bookRating: Array<String> = context.resources.getStringArray(R.array.book_rating)
        val bookImage: Array<String> = context.resources.getStringArray(R.array.book_image)
        val bookDetail: Array<String> = context.resources.getStringArray(R.array.book_detail)


        val listData: ArrayList<Book> = ArrayList<Book>()
        for (i in bookName.indices) {
            val book = Book(
                id = (i + 1).toString(),
                title = bookName[i],
                category = bookCategory[i],
                rating = bookRating[i],
                imageUrl = bookImage[i],
                detail = bookDetail[i],
            )
            listData.add(book)

        }
        return listData
    }

    fun Users.mapped(): Map<String, *> = mapOf(
        "name" to this.name,
        "email" to this.email,
        "imageUrl" to this.imageUrl,
        "status" to this.status,
    )


    fun Book.mapped(): Map<String, *> = mapOf(
        "title" to this.title,
        "category" to this.category,
        "rating" to this.rating,
        "detail" to this.detail,
        "author" to this.author,
        "numberPages" to this.numberPages,
        "language" to this.language,
        "imageUrl" to this.imageUrl,
        "price" to this.price,
        "status" to this.status,

        )

}


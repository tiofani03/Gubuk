package com.tegar.gubuk.ui.admin.book

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.tegar.gubuk.databinding.ActivityAddBookBinding
import com.tegar.gubuk.firestore.BookFireStore
import com.tegar.gubuk.utils.Helper.getPlainText
import com.tegar.gubuk.utils.Helper.toast
import com.tegar.gubuk.utils.Helper.toolbar
import com.tegar.gubuk.model.Book
import com.tegar.gubuk.utils.DialogHelper.showLoading
import io.github.achmadhafid.lottie_dialog.dismissLottieDialog
import kotlinx.android.synthetic.main.activity_add_book.*

class AddBookActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBookBinding
    private var status = "old"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBookBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toolbar(binding.toolbar)
        setSwitch()
        binding.btnAddBook.setOnClickListener {
            if (validate()) {
                showLoading()
                addNewBook()
            }
        }
    }

    private fun addNewBook() {
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val uid = db.collection("Book").document().id
        val book = Book(
            id = uid,
            title = edtTitle.getPlainText(),
            category = edtCategory.getPlainText(),
            rating = edtRating.getPlainText(),
            detail = edtDetail.getPlainText(),
            author = edtAuth.getPlainText(),
            numberPages = edtPage.getPlainText(),
            language = edtLanguage.getPlainText(),
            imageUrl = edtImage.getPlainText(),
            price = edtPrice.getPlainText(),
            status = status,
            search = edtTitle.getPlainText().toLowerCase()
        )
        Log.d("TestUid", "UId : $uid")

        BookFireStore.addBook(book, uid) {
            if (it) {
                toast("Penambahan berhasil")
                dismissLottieDialog()
                finish()
            } else {
                toast("Gagal, silahkan coba lagi")
                dismissLottieDialog()
            }
        }


    }

    private fun validate(): Boolean {
        var valid = true
        if (TextUtils.isEmpty(binding.edtTitle.getPlainText())) {
            binding.edtTitle.error = "required"
            valid = false
        } else {
            binding.edtTitle.error = null
        }

        if (TextUtils.isEmpty(binding.edtCategory.getPlainText())) {
            binding.edtCategory.error = "required"
            valid = false
        } else {
            binding.edtCategory.error = null
        }

        if (TextUtils.isEmpty(binding.edtDetail.getPlainText())) {
            binding.edtDetail.error = "required"
            valid = false
        } else {
            binding.edtDetail.error = null
        }

        if (TextUtils.isEmpty(binding.edtAuth.getPlainText())) {
            binding.edtAuth.error = "required"
            valid = false
        } else {
            binding.edtAuth.error = null
        }

        if (TextUtils.isEmpty(binding.edtRating.getPlainText())) {
            binding.edtRating.error = "required"
            valid = false
        } else {
            binding.edtRating.error = null
        }

        if (TextUtils.isEmpty(binding.edtPage.getPlainText())) {
            binding.edtPage.error = "required"
            valid = false
        } else {
            binding.edtPage.error = null
        }

        if (TextUtils.isEmpty(binding.edtLanguage.getPlainText())) {
            binding.edtLanguage.error = "required"
            valid = false
        } else {
            binding.edtLanguage.error = null
        }

        if (TextUtils.isEmpty(binding.edtPrice.getPlainText())) {
            binding.edtPrice.error = "required"
            valid = false
        } else {
            binding.edtPrice.error = null
        }

        if (TextUtils.isEmpty(binding.edtImage.getPlainText())) {
            binding.edtImage.error = "required"
            valid = false
        } else {
            binding.edtImage.error = null
        }

        return valid
    }

    private fun setSwitch() {
        binding.switchStatus.setOnCheckedChangeListener { _, b ->
            status = if (b) {
                "news"
            } else {
                "old"
            }
        }
    }

}
package com.tegar.gubuk.ui.admin.book

import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.tegar.gubuk.R
import com.tegar.gubuk.databinding.ActivityUpdateBookBinding
import com.tegar.gubuk.firestore.BookFireStore
import com.tegar.gubuk.model.Book
import com.tegar.gubuk.utils.DialogHelper.showLoading
import com.tegar.gubuk.utils.Helper.getPlainText
import com.tegar.gubuk.utils.Helper.toast
import com.tegar.gubuk.utils.Helper.toolbar
import io.github.achmadhafid.lottie_dialog.dismissLottieDialog
import kotlinx.android.synthetic.main.activity_add_book.*

class UpdateBookActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_UPDATE = "extra update"
    }

    private lateinit var binding: ActivityUpdateBookBinding
    private lateinit var book: Book
    private lateinit var status: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBookBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        binding.btnUpdate.setOnClickListener {
            if (validate()) {
                showLoading()
                updateBook()
            }
        }

    }

    private fun init() {
        toolbar(binding.toolbar)
        book = intent.getParcelableExtra(EXTRA_UPDATE)!!
        setData()
        setSwitch()
    }

    private fun setData() {
        with(binding) {
            edtTitle.setText(book.title)
            edtCategory.setText(book.category)
            edtDetail.setText(book.detail)
            edtAuth.setText(book.author)
            edtRating.setText(book.rating)
            edtPage.setText(book.numberPages)
            edtLanguage.setText(book.language)
            edtPrice.setText(book.price)
            edtImage.setText(book.imageUrl)
            status = book.status.toString()
            switchStatus.isChecked = book.status.equals("news")

        }
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

    private fun updateBook() {
        val bookUpdate = Book(
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
        BookFireStore.updateBook(book.id!!, bookUpdate) {
            if (it) {
                toast("Update berhasil")
                dismissLottieDialog()
                finish()
            } else {
                dismissLottieDialog()
                toast("Gagal, silahkan coba lagi")
            }
        }
    }

    private fun showDeleteDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Peringatan")
        builder.setMessage("Anda ingin menghapus buku ?")

        builder.setPositiveButton(android.R.string.yes) { _, _ ->
            deleteBook()
            Toast.makeText(this, "Berhasil dihapus", Toast.LENGTH_SHORT).show()
            finish()
        }

        builder.setNegativeButton(android.R.string.no) { dialog, which ->
            dialog.cancel()
        }


        builder.show()
    }

    private fun deleteBook() {
        BookFireStore.deleteBook(book.id!!) {
            if (it) {
                toast("Berhasil dihapus")
                finish()
            } else {
                toast("Gagal, silahkan coba lagi")
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_delete, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_delete) {
            showDeleteDialog()
        }
        return super.onOptionsItemSelected(item)

    }
}
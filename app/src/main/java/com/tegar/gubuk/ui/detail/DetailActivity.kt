package com.tegar.gubuk.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import coil.load
import com.google.android.material.appbar.AppBarLayout
import com.tegar.gubuk.R
import com.tegar.gubuk.databinding.ActivityDetailBinding
import com.tegar.gubuk.model.Book
import com.tegar.gubuk.ui.order.OrderBookActivity
import com.tegar.gubuk.utils.Helper

class DetailActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_DETAIL = "extra detail"
    }

    private lateinit var binding: ActivityDetailBinding
    private var book: Book? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)



        init()
    }

    private fun init() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        book = intent.getParcelableExtra(EXTRA_DETAIL)
        setCollapsing(book?.title)
        setDetail(book!!)
        //ketika tombol detail diklik
        binding.contentDetail.btnOrder.setOnClickListener {
            val intent = Intent(this,OrderBookActivity::class.java)
            intent.putExtra(OrderBookActivity.EXTRA_ORDER,book)
            startActivity(intent)
        }

    }

    //mengatur detail
    private fun setDetail(book: Book) {
        with(binding) {
            ivBackDrop.load(book.imageUrl)
            ivCover.load(book.imageUrl)
            tvBookAuthor.text = book.author
            tvBookLanguage.text = book.language
            tvBookPage.text = book.numberPages
            tvBookRating.text = book.rating
            tvBookTitle.text = book.title
        }
        binding.contentDetail.tvBookPrice.text = Helper.rupiahHelper(book.price)
        binding.contentDetail.tvBookDetail.text = book.detail

    }

    //mengatur ketika discroll
    private fun setCollapsing(title: String?) {
        binding.tvTitle.text = " "
        binding.collapsingToolbar.setCollapsedTitleTextColor(
            ContextCompat.getColor(
                this,
                R.color.white
            )
        )

        binding.appbar.setExpanded(true)
        val mAppBarLayout = findViewById<View>(R.id.appbar) as AppBarLayout
        mAppBarLayout.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            var isShow = false
            var scrollRange = -1


            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    binding.collapsingToolbar.title = title
                    binding.tvTitle.text = title
                    isShow = true
                } else if (isShow) {
                    binding.collapsingToolbar.title = " "
                    binding.tvTitle.text = " "
                    isShow = false
                }
            }

        })

    }

    //tombol back
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
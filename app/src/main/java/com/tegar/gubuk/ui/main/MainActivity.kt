package com.tegar.gubuk.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.tegar.gubuk.R
import com.tegar.gubuk.databinding.ActivityMainBinding
import com.tegar.gubuk.firestore.BookFireStore
import com.tegar.gubuk.firestore.UserFireStore
import com.tegar.gubuk.model.Book
import com.tegar.gubuk.ui.main.adapter.BookAdapter
import com.tegar.gubuk.ui.main.adapter.BookPopularAdapter
import com.tegar.gubuk.ui.main.adapter.BookStoreAdapter
import com.tegar.gubuk.ui.main.popular.PopularActivity
import com.tegar.gubuk.ui.profile.ProfileActivity
import com.tegar.gubuk.ui.search.SearchActivity
import com.tegar.gubuk.utils.DataSource
import com.tegar.gubuk.utils.Helper
import java.util.*


class MainActivity : AppCompatActivity() {

    private var bookList: ArrayList<Book> = ArrayList<Book>()
    private lateinit var bookAdapter: BookAdapter
    private lateinit var bookPopularAdapter: BookPopularAdapter
    private lateinit var bookStoreAdapter: BookStoreAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bookList = DataSource.getListData(this)
        init()


    }

    private fun init() {
        //menampilkan waktu
        binding.tvGreeting.text = Helper.getGreetingMessage()
        bookAdapter = BookAdapter()
        bookPopularAdapter = BookPopularAdapter()
        bookStoreAdapter = BookStoreAdapter()

        //menampilkan buku populer
        showNewsBook()

        //menampilkan semua buku
        showAllBook()

        //menampilakn
        showBookStore()

        //mengatur profile
        setProfile()
        binding.tvSeeMore.setOnClickListener {
            startActivity(Intent(this, PopularActivity::class.java))
        }
        binding.imgProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
        binding.cvSearch.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }
    }

    private fun showBookStore() {
        BookFireStore.getBookStoreLocation {
            if (it.isNotEmpty()) {
                bookStoreAdapter.setList(it)
            }
        }

        binding.rvBookStore.apply {
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = bookStoreAdapter
        }
    }

    private fun setProfile() {
        val id = Firebase.auth.currentUser!!.uid
        UserFireStore.getUser(id) {
            if (it.id != null) {
                binding.imgProfile.load(it.imageUrl) {
                    placeholder(R.drawable.ic_image)
                    transformations(CircleCropTransformation())
                    binding.tvUsername.text = it.name
                }
            }
        }
    }

    private fun showNewsBook() {
        BookFireStore.getPopularBook {
            if (it.isNotEmpty()) {
                bookPopularAdapter.setList(it)
            }
        }

        binding.rvPopular.apply {
            layoutManager = LinearLayoutManager(
                this@MainActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = bookPopularAdapter
        }
    }

    private fun showAllBook() {
        BookFireStore.getAllBook {
            if (it.isNotEmpty()) {
                bookAdapter.setList(it)
            }
        }
        binding.rvBook.apply {
            layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
//            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = bookAdapter
        }
    }


}
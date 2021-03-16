package com.tegar.gubuk.ui.main.popular

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tegar.gubuk.databinding.ActivityPopularBinding
import com.tegar.gubuk.firestore.BookFireStore
import com.tegar.gubuk.ui.main.adapter.BookAdapter
import com.tegar.gubuk.utils.Helper.toolbar

class PopularActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPopularBinding
    private lateinit var bookAdapter: BookAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPopularBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init(){
        toolbar(binding.toolbar)
        bookAdapter = BookAdapter()
        setPopularBook()
    }

    private fun setPopularBook(){
        BookFireStore.getPopularBook {
            if(it.isNotEmpty()){
                bookAdapter.setList(it)
            }
        }

        binding.rvPopular.apply {
            layoutManager = GridLayoutManager(this@PopularActivity,2,LinearLayoutManager.VERTICAL,false)
            adapter = bookAdapter
        }
    }
}
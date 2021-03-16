package com.tegar.gubuk.ui.search

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.miguelcatalan.materialsearchview.MaterialSearchView
import com.tegar.gubuk.R
import com.tegar.gubuk.databinding.ActivitySearchBinding
import com.tegar.gubuk.firestore.BookFireStore
import com.tegar.gubuk.ui.main.adapter.BookAdapter
import com.tegar.gubuk.utils.Helper.showKeyboard

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private lateinit var bookAdapter: BookAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()


    }

    private fun init() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = ""
            setDisplayHomeAsUpEnabled(true)
        }
        bookAdapter = BookAdapter()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val itemMenu = menu?.findItem(R.id.actionSearch)
        //untuk mencari buku
        binding.searchView.apply {
            setMenuItem(itemMenu)
            setHint("Cari buku...")
            showSearch()
            //ketika ada query
            setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    searchData(query)
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    //searchData(newText);
                    return false
                }
            })

            hideKeyboard(this)
        }
        showKeyboard()

        return super.onCreateOptionsMenu(menu)
    }

    //fungsi untuk mencari data
    private fun searchData(query: String) {
        supportActionBar?.title = query
        BookFireStore.searchBook(query) {
            if (it.isNotEmpty()) {
                bookAdapter.setList(it)
            }
        }

        binding.rvSearch.apply {
            layoutManager = GridLayoutManager(
                this@SearchActivity, 2,
                LinearLayoutManager.VERTICAL, false
            )
            adapter = bookAdapter
        }
    }


    //tombol back
    override fun onSupportNavigateUp(): Boolean {
        with(binding) {
            if (searchView.isSearchOpen) searchView.closeSearch()
            else super.onBackPressed()
        }
        return true
    }
}
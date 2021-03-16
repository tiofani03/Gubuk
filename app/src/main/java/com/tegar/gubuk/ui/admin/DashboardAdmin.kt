package com.tegar.gubuk.ui.admin

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.tegar.gubuk.databinding.ActivityDashboardAdminBinding
import com.tegar.gubuk.firestore.BookFireStore
import com.tegar.gubuk.firestore.UserFireStore
import com.tegar.gubuk.ui.admin.book.AddBookActivity
import com.tegar.gubuk.ui.auth.LoginActivity
import com.tegar.gubuk.utils.Helper.hide
import com.tegar.gubuk.utils.Helper.show
import kotlinx.android.synthetic.main.activity_dashboard_admin.*
import java.util.*

class DashboardAdmin : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardAdminBinding
    private lateinit var bookAdminAdapter: BookAdminAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        binding.contentAdmin.cvInnovation.setOnClickListener {
            startActivity(Intent(this, AddBookActivity::class.java))
        }

        binding.tvLogout.setOnClickListener {
            Firebase.auth.signOut()
            val currentUser = Firebase.auth.currentUser
            if (currentUser == null) {
                val intent = Intent(this, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }

    }

    private fun init() {
        bookAdminAdapter = BookAdminAdapter()
        setUsers()
        setBook()
    }

    private fun setBook() {
        BookFireStore.getAllBook {
            if (it.isNotEmpty()) {
                with(binding) {
                    rvBook.show()
                    contentAdmin.tvInnovation.text = it.size.toString()
                    bookAdminAdapter.setList(it)
                }
            } else {
                binding.contentAdmin.tvInnovation.text = "0"
                binding.rvBook.hide()
            }
        }

        with(binding.rvBook) {
            layoutManager =
                LinearLayoutManager(this@DashboardAdmin, LinearLayoutManager.HORIZONTAL, false)
            adapter = bookAdminAdapter
        }
    }

    private fun setUsers() {
        UserFireStore.getAllUsers {
            if (it.isNotEmpty()) {
                with(binding) {
                    contentAdmin.tvUsers.text = it.size.toString()
                }
            }
        }

    }


}
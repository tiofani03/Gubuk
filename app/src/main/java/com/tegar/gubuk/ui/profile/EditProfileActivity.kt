package com.tegar.gubuk.ui.profile

import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.tegar.gubuk.databinding.ActivityEditProfileBinding
import com.tegar.gubuk.firestore.UserFireStore
import com.tegar.gubuk.model.Users
import com.tegar.gubuk.utils.DialogHelper.showLoading
import com.tegar.gubuk.utils.Helper.showToast
import com.tegar.gubuk.utils.Helper.toolbar
import kotlinx.android.synthetic.main.activity_edit_profile.*

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        toolbar(binding.toolbar)
        setProfile()
    }

    //mengatur profile
    private fun setProfile() {
        val id = Firebase.auth.currentUser!!.uid
        //mendapatkan data dari firestore berdasarkan uid
        UserFireStore.getUser(id) { user ->
            //ketika id tidak null
            if (user.id != null) {
                with(binding) {
                    //mengeset data
                    edtName.setText(user.name)
                    edtEmail.setText(user.email)
                    btnUpdate.setOnClickListener {
                        if (validateForm()) {
                            showLoading()
                            updateData(user)
                        }
                    }
                }
            }
        }
    }

    //mengecek apakah formnya kosong atau tidak
    private fun validateForm(): Boolean {
        var state = true
        if (TextUtils.isEmpty(binding.edtName.text.toString())) {
            binding.edtName.error = "Required."
            state = false
        }
        return state
    }

    //mengupdate data
    private fun updateData(users: Users) {
        val id = FirebaseAuth.getInstance().currentUser!!.uid
        users.name = edtName.text.toString()
        //mengupdate data ke firestore
        UserFireStore.updateProfile(id, users) {
            if (it) {
                showToast(this, "Data berhasil diupdate")
                finish()
            }
        }

    }
}
package com.tegar.gubuk.ui.auth

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.tegar.gubuk.databinding.ActivityRegisterBinding
import com.tegar.gubuk.firestore.UserFireStore
import com.tegar.gubuk.utils.Helper.getPlainText
import com.tegar.gubuk.utils.Helper.showToast
import com.tegar.gubuk.model.Users
import com.tegar.gubuk.ui.main.MainActivity
import io.github.achmadhafid.lottie_dialog.dismissLottieDialog

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseUserID: String
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        //ketika tombol register diklik
        binding.btnRegister.setOnClickListener {
            if (validateForm()) {
                register(
                    binding.edtEmail.getPlainText(),
                    binding.edtPw.getPlainText()
                )
            }
        }

        binding.tvLogin.setOnClickListener {
            finish()
        }
    }

    //memvalidasi form
    private fun validateForm(): Boolean {
        var valid = true
        if (TextUtils.isEmpty(binding.edtEmail.getPlainText())) {
            binding.inputEmail.error = "required"
            valid = false
        } else {
            binding.inputEmail.error = null
        }

        if (TextUtils.isEmpty(binding.edtPw.getPlainText())) {
            binding.inputPw.error = "required"
            valid = false
        } else if (binding.edtPw.getPlainText().length < 6) {
            binding.inputPw.error = "Minimal 6 huruf"
            valid = false
        } else {
            binding.inputPw.error = null
        }

        if (TextUtils.isEmpty(binding.edtUserName.getPlainText())) {
            binding.inputName.error = "required"
            valid = false
        } else {
            binding.inputName.error = null
        }

        return valid

    }

    //unuk register
    private fun register(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    firebaseUserID = Firebase.auth.currentUser!!.uid
                    //mengatur objek
                    val user = Users(
                        id = firebaseUserID,
                        email = email,
                        name = binding.edtUserName.getPlainText(),
                        imageUrl = "https://firebasestorage.googleapis.com/v0/b/kms-desa.appspot.com/o/users_profile%2Fman2.png?alt=media&token=45a98abb-2cff-4a3e-88e9-5a76bc9837da",
                        status = "user"
                    )

                    //membuat akun baru
                    UserFireStore.createNewUser(user) {
                        if (it) {
                            showToast(this, "Daftar berhasil")
                            val intent = Intent(this, MainActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        }
                    }
                }
            }.addOnFailureListener {
                dismissLottieDialog()
                showToast(this, "Registrasi gagal")
            }


    }
}
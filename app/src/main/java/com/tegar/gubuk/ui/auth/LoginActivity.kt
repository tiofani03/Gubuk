package com.tegar.gubuk.ui.auth

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.auth.FirebaseAuth
import com.tegar.gubuk.R
import com.tegar.gubuk.databinding.ActivityLoginBinding
import com.tegar.gubuk.firestore.UserFireStore
import com.tegar.gubuk.utils.Helper.getPlainText
import com.tegar.gubuk.utils.Helper.showToast
import com.tegar.gubuk.ui.admin.DashboardAdmin
import com.tegar.gubuk.ui.main.MainActivity
import com.tegar.gubuk.utils.DialogHelper
import com.tegar.gubuk.utils.DialogHelper.showLoading
import com.tegar.gubuk.utils.ThemePreference
import io.github.achmadhafid.lottie_dialog.core.*
import io.github.achmadhafid.lottie_dialog.dismissLottieDialog
import io.github.achmadhafid.lottie_dialog.model.LottieDialogInput
import io.github.achmadhafid.lottie_dialog.model.LottieDialogTheme
import io.github.achmadhafid.lottie_dialog.model.LottieDialogType

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        auth = FirebaseAuth.getInstance()

        binding.btnLogin.setOnClickListener {
            showLoading()
            if (validateForm()) {
                login(
                    binding.edtEmail.getPlainText(),
                    binding.edtPw.getPlainText()
                )
            }
        }

        binding.tvForgotPw.setOnClickListener {
            showDialogForgotPassword()
        }

        binding.tvSignUp.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    //memvalidasi form jika kosong mengembalikan nilai false
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
        } else {
            binding.inputPw.error = null
        }

        return valid

    }

    //untuk login dengan menggunakan library dari firebase
    private fun login(email: String, password: String) {
        //login dengan email
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    //jika sukses
                    checkLogin()
                    showToast(this, "Login berhasil")

                } else {
                    showToast(this, "Authentication failed.")
                    dismissLottieDialog()
                }

            }

    }

    //mengecek login
    private fun checkLogin() {
        val currentUser = auth.currentUser
        //ketika user tidak kosong
        if (currentUser != null) {
            Handler(mainLooper).postDelayed({
                UserFireStore.getUser(auth.currentUser!!.uid) {
                    //jika status == user
                    if (it.status == "user") {
                        val intent = Intent(this,MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        //membuka mainactivity
                        startActivity(intent)
                        dismissLottieDialog()
                        finish()
                    } else if (it.status == "admin") {
                        //jika statusnya admin
                        val intent = Intent(this,DashboardAdmin::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        //membuka admin
                        startActivity(intent)
                        finish()
                    }
                }
            },1000)

        }
    }

    //untuk lupa password
    private fun forgotPassword(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showToast(this, "Email dikirim ke $email")
                }
            }

    }

    //menampilkan dialog lupa password
    private fun showDialogForgotPassword() {
        lottieInputDialog(Int.MAX_VALUE, DialogHelper.askSomething) {
            LottieDialogType.BOTTOM_SHEET
            theme = LottieDialogTheme.DAY_NIGHT
            withAnimation {
                fileRes = R.raw.forgot_pw
                showCloseButton = true
            }

            withTitle {
                text = "Lupa password"
            }

            withContent {
                text = "Masukan email anda"
            }
            withInputSpec {
                LottieDialogInput.Type.TEXT
                initialValue = "Email Anda"
                isValidInput { it.isNotEmpty() }
            }

            withCancelOption {
                onBackPressed = true
                onTouchOutside = true
            }


            onValidInput { input ->
                forgotPassword(input)
            }

        }
    }




}
package com.tegar.gubuk.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.auth.FirebaseAuth
import com.tegar.gubuk.R
import com.tegar.gubuk.firestore.UserFireStore
import com.tegar.gubuk.ui.admin.DashboardAdmin
import com.tegar.gubuk.ui.auth.LoginActivity
import com.tegar.gubuk.ui.main.MainActivity
import com.tegar.gubuk.utils.ThemePreference
import io.github.achmadhafid.lottie_dialog.dismissLottieDialog

class SplashActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    //untuk mengecek login
    private fun checkLogin() {
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        //cek user firebase sudah ada
        if (currentUser != null) {
            Handler(mainLooper).postDelayed({
                UserFireStore.getUser(auth.currentUser!!.uid) {
                    //ketika statusnya user
                    if (it.status == "user") {
                        val intent = Intent(this, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                        finish()

                    //ketika statusnya admin
                    } else if (it.status == "admin") {
                        val intent = Intent(this, DashboardAdmin::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                        finish()
                    }
                }
            },1000)
            //ketika tidak ada user pada firebase
        }else{
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
    }

    //untuk mengecek tema yang digunakan
    private fun checkTheme() {
        when (ThemePreference(this).theme) {
            0 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                delegate.applyDayNight()
            }
            1 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                delegate.applyDayNight()
            }
            2 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                delegate.applyDayNight()
            }
        }
    }

    //ketika activity dibuka
    public override fun onStart() {
        super.onStart()
        checkLogin()
        checkTheme()
    }
}
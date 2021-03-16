package com.tegar.gubuk.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import coil.load
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.tegar.gubuk.R
import com.tegar.gubuk.databinding.ActivityProfileBinding
import com.tegar.gubuk.firestore.OrderFireStore
import com.tegar.gubuk.firestore.UserFireStore
import com.tegar.gubuk.ui.auth.LoginActivity
import com.tegar.gubuk.utils.Helper.toTimeAgo
import com.tegar.gubuk.utils.Helper.toolbar
import com.tegar.gubuk.utils.ThemePreference
import kotlinx.android.synthetic.main.activity_register.*

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()

    }

    private fun init() {
        toolbar(binding.toolbar)
        binding.floatingActionButton.setOnClickListener {
            startActivity(Intent(this, EditProfileActivity::class.java))
        }
        setProfile()
        binding.tvThemeChange.setOnClickListener {
            setThemeDialog()
        }
        checkTheme()
        binding.tvSeeOrder.setOnClickListener {
            startActivity(Intent(this,OrderListActivity::class.java))
        }

    }

    //mendapatkan data profile dari firestore berdasarkan uid
    private fun setProfile() {
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        UserFireStore.getUser(uid) {
            if (it.id != null) {
                binding.tvEmail.text = it.email
                binding.tvName.text = it.name
                binding.ivProfile.load(it.imageUrl) {
                    placeholder(R.drawable.ic_image)
                }
            }
        }

    }

    //menu pojok kanan atas
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_logout, menu)
        return super.onCreateOptionsMenu(menu)
    }

    //mengatur toolbar ketika diklik
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_logout) {
            Firebase.auth.signOut()
            val currentUser = Firebase.auth.currentUser
            if (currentUser == null) {
                val intent = Intent(this, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //menampilkan dialog tema
    private fun setThemeDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pilih Tema")
        val theme = arrayOf("Mode siang", "Mode malam", "Default dari sistem")
        val checkTheme = ThemePreference(this).theme
        builder.setSingleChoiceItems(theme, checkTheme) { dialog, which ->
            when (which) {
                0 -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    ThemePreference(this).theme = 0
                    delegate.applyDayNight()
                    dialog.dismiss()
                }
                1 -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    ThemePreference(this).theme = 1
                    delegate.applyDayNight()
                    dialog.dismiss()
                }
                2 -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    ThemePreference(this).theme = 2
                    delegate.applyDayNight()
                    dialog.dismiss()
                }

            }
        }

        val dialog = builder.create()
        dialog.show()
    }

    //untuk mengecek tema
    private fun checkTheme() {
        when (ThemePreference(this).theme) {
            0 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                delegate.applyDayNight()
                binding.tvThemeSetting.text = "Mode Siang"
            }
            1 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                delegate.applyDayNight()
                binding.tvThemeSetting.text = "Mode Malam"
            }
            2 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                delegate.applyDayNight()
                binding.tvThemeSetting.text = "Tema Default"
            }
        }
    }
}
package com.tegar.gubuk.utils

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.github.marlonlom.utilities.timeago.TimeAgo
import com.github.marlonlom.utilities.timeago.TimeAgoMessages
import com.google.firebase.Timestamp
import java.text.DateFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

object Helper {
    fun getGreetingMessage():String{
        val c = Calendar.getInstance()

        return when (c.get(Calendar.HOUR_OF_DAY)) {
            in 0..11 -> "Selamat Pagi"
            in 12..15 -> "Selamat Siang"
            in 16..20 -> "Selamat Sore"
            in 21..23 -> "Selamat Malam"
            else -> "Hello"
        }
    }

    fun Context.toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun View.show() {
        visibility = View.VISIBLE
    }

    fun View.hide() {
        visibility = View.GONE
    }

    fun rupiahHelper(number: String?): String{
        val localeID =  Locale("in", "ID")
        val numberFormat = NumberFormat.getCurrencyInstance(localeID)
        val price = number?.toInt()
        return numberFormat.format(price).toString()
    }


    fun postDelayed(delayMillis: Long, task: () -> Unit) {
        Handler().postDelayed(task, delayMillis)
    }

    fun AppCompatActivity.toolbar(toolbar: Toolbar, titleStr: String = "") {
        setSupportActionBar(toolbar)
        supportActionBar?.title = titleStr
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.apply {
            setNavigationOnClickListener { finish() }
        }
    }

    fun EditText.getPlainText() = this.text.toString()

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun Activity.showKeyboard() {
        val imm: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    fun getCurrentDate(): String? {
        val dateFormat: DateFormat = SimpleDateFormat("yyyy/MM/ddHH:mm:ss", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }

    fun Timestamp.toTimeAgo(): String {
        val messages: TimeAgoMessages = TimeAgoMessages.Builder().withLocale(Locale("id")).build()
        return TimeAgo.using(this.toDate().time, messages)
    }
}
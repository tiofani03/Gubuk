package com.tegar.gubuk.ui.order

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.tegar.gubuk.databinding.ActivityOrderBookBinding
import com.tegar.gubuk.firestore.OrderFireStore
import com.tegar.gubuk.firestore.UserFireStore
import com.tegar.gubuk.model.Book
import com.tegar.gubuk.model.Order
import com.tegar.gubuk.ui.main.MainActivity
import com.tegar.gubuk.utils.Helper
import com.tegar.gubuk.utils.Helper.getPlainText
import com.tegar.gubuk.utils.Helper.toast
import com.tegar.gubuk.utils.Helper.toolbar
import io.github.achmadhafid.lottie_dialog.dismissLottieDialog

class OrderBookActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_ORDER = "extra order"
    }

    private lateinit var binding: ActivityOrderBookBinding
    private var bookOrder: Book? = null
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBookBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        toolbar(binding.toolbar)
        userId = Firebase.auth.currentUser!!.uid
        bookOrder = intent.getParcelableExtra(EXTRA_ORDER)
        getData()
        binding.btnBooking.setOnClickListener {
            if (validate()) {
                makeOrder()
            }
        }
    }

    private fun getData() {
        if (bookOrder != null) {
            with(binding) {
                edtBookTitle.setText(bookOrder?.title)
                tvBookPrice.text = Helper.rupiahHelper(bookOrder?.price)
            }
        }

        UserFireStore.getUser(userId) {
            if (it.id != null) {
                binding.edtName.setText(it.name)
            }
        }

    }

    private fun validate(): Boolean {
        var valid = true
        if (TextUtils.isEmpty(binding.edtName.getPlainText())) {
            binding.edtName.error = "required"
            valid = false
        } else {
            binding.edtName.error = null
        }

        if (TextUtils.isEmpty(binding.edtAddress.getPlainText())) {
            binding.edtAddress.error = "required"
            valid = false
        } else {
            binding.edtAddress.error = null
        }

        if (TextUtils.isEmpty(binding.edtBookTitle.getPlainText())) {
            binding.edtBookTitle.error = "required"
            valid = false
        } else {
            binding.edtBookTitle.error = null
        }

        return valid
    }

    private fun makeOrder() {
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val uid = db.collection("Order").document().id

        val order = Order(
            id = uid,
            idUser = userId,
            idBook = bookOrder!!.id,
            userName = binding.edtName.getPlainText(),
            userAddress = binding.edtAddress.getPlainText(),
            bookName = binding.edtBookTitle.getPlainText(),
            bookPrice = bookOrder!!.price,
            time = Timestamp.now(),
            status = "Diproses"
        )
        Log.d("TestUid", "UId : $uid")

        OrderFireStore.addOrder(order, uid) {
            if (it) {
                toast("Pesanan diproses")
                dismissLottieDialog()
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            } else {
                toast("Gagal, silahkan coba lagi")
                dismissLottieDialog()
            }
        }
    }
}
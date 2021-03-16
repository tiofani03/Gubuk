package com.tegar.gubuk.ui.profile

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.tegar.gubuk.databinding.ActivityOrderListBinding
import com.tegar.gubuk.ui.order.OrderViewModel
import com.tegar.gubuk.utils.Helper.toolbar

class OrderListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderListBinding
    private lateinit var listOrderAdapter: ListOrderAdapter
    private val viewModel: OrderViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()

    }

    private fun init(){
        toolbar(binding.toolbar)
        listOrderAdapter =  ListOrderAdapter()
        getListOrder()
    }


    //menampilkan listorderan
    private fun getListOrder(){
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        Log.d("Uid","Uid : $uid")
//        OrderFireStore.getOrderById(uid) {
//            if (it.isNotEmpty()) {
//                listOrderAdapter.setList(it)
//                Log.d("Uid","Uid : $uid jumlah : ${it.size} ")
//            }
//        }
        viewModel.getOrderById(uid)
        viewModel.grouping.observe(this, {
            if (it.isNotEmpty()) {
                listOrderAdapter.setList(it)
                Log.d("Uid", "Uid : $uid jumlah : ${it.size} ")
            }
        })

        binding.rvOrder.apply{
            layoutManager = LinearLayoutManager(this@OrderListActivity)
            adapter = listOrderAdapter
        }
    }
}
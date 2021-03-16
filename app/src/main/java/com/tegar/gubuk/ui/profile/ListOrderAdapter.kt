package com.tegar.gubuk.ui.profile

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tegar.gubuk.R
import com.tegar.gubuk.databinding.ItemOrderBookBinding
import com.tegar.gubuk.model.Order
import com.tegar.gubuk.utils.Helper
import com.tegar.gubuk.utils.Helper.toTimeAgo

class ListOrderAdapter : RecyclerView.Adapter<ListOrderAdapter.ListOrderViewHolder>() {


    private val orderList = ArrayList<Order>()

    fun setList(list: List<Order>) {
        orderList.clear()
        orderList.addAll(list)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListOrderViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_order_book, parent, false)
        return ListOrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListOrderViewHolder, position: Int) {
        holder.bindItem(orderList[position])
    }

    override fun getItemCount(): Int = orderList.size


    class ListOrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemOrderBookBinding.bind(view)
        fun bindItem(order: Order) {
            Log.d("LitWaktu","Waktu : ${order.time}")
            binding.tvDate.text = order.time!!.toTimeAgo()
            binding.tvTitle.text = order.bookName
            binding.tvBookPrice.text = Helper.rupiahHelper(order.bookPrice)
            binding.tvStatusOrder.text = order.status
        }

    }
}
package com.tegar.gubuk.ui.main.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.tegar.gubuk.R
import com.tegar.gubuk.databinding.ItemAdminGridBinding
import com.tegar.gubuk.model.BookStore
import com.tegar.gubuk.ui.maps.MapsActivity

class BookStoreAdapter : RecyclerView.Adapter<BookStoreAdapter.BookStoreViewHolder>() {
    private var listBookStore = ArrayList<BookStore>()

    fun setList(list: List<BookStore>) {
        listBookStore.clear()
        listBookStore.addAll(list)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookStoreViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_admin_grid, parent, false)
        return BookStoreViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookStoreViewHolder, position: Int) {
        holder.bindItem(listBookStore[position])
    }

    override fun getItemCount(): Int {
        return listBookStore.size
    }

    class BookStoreViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemAdminGridBinding.bind(view)
        fun bindItem(bookStore: BookStore) {
            binding.imgItemPhoto.load(bookStore.img)
            binding.tvItemName.text = bookStore.name
            with(itemView){
                setOnClickListener {
                    val intent = Intent(context,MapsActivity::class.java)
                    intent.putExtra(MapsActivity.EXTRA_MAP,bookStore)
                    context.startActivity(intent)
                }
            }
        }
    }
}
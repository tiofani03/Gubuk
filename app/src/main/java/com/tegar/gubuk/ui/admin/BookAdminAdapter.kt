package com.tegar.gubuk.ui.admin

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.tegar.gubuk.R
import com.tegar.gubuk.databinding.ItemAdminGridBinding
import com.tegar.gubuk.model.Book
import com.tegar.gubuk.ui.admin.book.UpdateBookActivity

class BookAdminAdapter : RecyclerView.Adapter<BookAdminAdapter.UserViewHolder>() {
    private val listBook = ArrayList<Book>()

    fun setList(list: List<Book>) {
        listBook.clear()
        listBook.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_admin_grid, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bindItem(listBook[position])
    }

    override fun getItemCount(): Int {
        return listBook.size
    }

    class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemAdminGridBinding.bind(view)
        fun bindItem(book: Book) {
            binding.imgItemPhoto.load(book.imageUrl)
            binding.tvItemName.text = book.title

            //ketika item book pada admin diklik
            with(itemView){
                setOnClickListener {
                    val intent = Intent(context,UpdateBookActivity::class.java)
                    intent.putExtra(UpdateBookActivity.EXTRA_UPDATE,book)
                    context.startActivity(intent)
                }
            }
        }
    }
}
package com.tegar.gubuk.ui.main.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.tegar.gubuk.R
import com.tegar.gubuk.databinding.ItemBookPopularBinding
import com.tegar.gubuk.model.Book
import com.tegar.gubuk.ui.detail.DetailActivity
import com.tegar.gubuk.ui.order.OrderBookActivity

class BookPopularAdapter : RecyclerView.Adapter<BookPopularAdapter.BookPopularViewHolder>() {

    private val bookList = ArrayList<Book>()

    fun setList(list: List<Book>) {
        bookList.clear()
        bookList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookPopularViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_book_popular, parent, false)
        return BookPopularViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookPopularViewHolder, position: Int) {
        holder.bindItem(bookList[position])
    }

    override fun getItemCount(): Int = bookList.size

    class BookPopularViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemBookPopularBinding.bind(view)
        fun bindItem(book: Book) {
            binding.ivPoster.load(book.imageUrl) {
                crossfade(true)
                placeholder(R.drawable.ic_image)
            }
            binding.tvTitle.text = book.title
            binding.tvCategory.text = book.category
            binding.tvDetail.text = book.detail
            binding.rbRating.rating = book.rating!!.toFloat()
            binding.btnOrder.setOnClickListener {
                val intent = Intent(itemView.context, OrderBookActivity::class.java)
                intent.putExtra(OrderBookActivity.EXTRA_ORDER,book)
                itemView.context.startActivity(intent)
            }


            with(itemView){
                setOnClickListener {
                    val intent = Intent(context, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.EXTRA_DETAIL,book)
                    context.startActivity(intent)
                }
            }

        }

    }
}
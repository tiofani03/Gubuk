package com.tegar.gubuk.ui.main.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.tegar.gubuk.R
import com.tegar.gubuk.databinding.ItemBookBinding
import com.tegar.gubuk.model.Book
import com.tegar.gubuk.ui.detail.DetailActivity

class BookAdapter : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {
    private val bookList = ArrayList<Book>()

    fun setList(list: List<Book>) {
        bookList.clear()
        bookList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_book, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bindItem(bookList[position])
    }

    override fun getItemCount(): Int = bookList.size

    class BookViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemBookBinding.bind(view)
        fun bindItem(book: Book) {
            with(binding){
                ivPoster.load(book.imageUrl) {
                    crossfade(true)
                    placeholder(R.drawable.ic_image)
                }
                tvTitle.text = book.title
                tvCategory.text = book.category
                tvDetail.text = book.detail
                binding.rbRating.rating = book.rating!!.toFloat()
            }

            //ketika data diklik
            with(itemView){
                setOnClickListener {
                    val intent = Intent(context,DetailActivity::class.java)
                    intent.putExtra(DetailActivity.EXTRA_DETAIL,book)
                    context.startActivity(intent)
                }
            }
        }
    }
}
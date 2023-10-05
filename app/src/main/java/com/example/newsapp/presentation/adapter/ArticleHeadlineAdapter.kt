package com.example.newsapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.data.model.Article
import com.example.newsapp.databinding.ArticleItemLayoutBinding

class ArticleHeadlineAdapter(private val callback: ArticleAdapterCallback) :
    PagingDataAdapter<Article, ArticleHeadlineAdapter.MyViewHolder>(ArticleDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ArticleItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding, callback)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    class MyViewHolder(
        private val binding: ArticleItemLayoutBinding,
        private val callback: ArticleAdapterCallback
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(article: Article) {
            binding.apply {
                Glide
                    .with(itemView.context)
                    .load(article.urlToImage)
                    .centerCrop()
                    .placeholder(R.drawable.baseline_broken_image_24)
                    .into(ivArticlePhoto)
                tvArticleTitle.text = article.title
                tvArticleDesc.text = article.description

                val bookmarkDrawable =
                    if (article.isBookmark) R.drawable.round_bookmark_24
                    else R.drawable.round_bookmark_border_24
                btnBookmark.setImageDrawable(
                    ContextCompat.getDrawable(
                        itemView.context,
                        bookmarkDrawable
                    )
                )

                btnBookmark.setOnClickListener { callback.onBookmarkClick(article) }
            }
            itemView.setOnClickListener { callback.onItemClick(article) }
        }
    }

    private class ArticleDiffCallBack : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean =
            oldItem.title == newItem.title
    }
}
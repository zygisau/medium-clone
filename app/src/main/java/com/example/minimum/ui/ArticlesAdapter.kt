package com.example.minimum.ui

import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.minimum.R
import com.example.minimum.model.Article
import com.squareup.picasso.Picasso


class ArticlesAdapter : PagingDataAdapter<Article, ArticlesViewHolder>(COMPARATOR) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticlesViewHolder {
        return ArticlesViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ArticlesViewHolder, position: Int) {
        val article = getItem(position)
        if (article != null) {
            holder.bind(article)
            bindPhoto(holder, article)
        }
    }

    private fun bindPhoto(holder: ArticlesViewHolder, item: Article) {
        Picasso.get()
                .load(item.image)
                .centerCrop()
                .fit()
                .into(holder.image)
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean =
                oldItem == newItem
        }
    }
}
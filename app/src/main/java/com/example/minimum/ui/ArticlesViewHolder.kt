package com.example.minimum.ui

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.minimum.R
import com.example.minimum.model.Article
import java.time.format.DateTimeFormatter


const val EXTRA_MESSAGE = "com.example.minimum.ARTICLE_ID"

class ArticlesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private var title: TextView = view.findViewById(R.id.article_title);
    private var subtitle: TextView = view.findViewById(R.id.subtitle);
    private var author: TextView = view.findViewById(R.id.author);
    private var date: TextView = view.findViewById(R.id.date);
    private var minutesToRead: TextView = view.findViewById(R.id.time_to_read);
    var image: ImageView = view.findViewById(R.id.iconImageView);
    var cv: CardView = view.findViewById(R.id.cv);

    private var article: Article? = null

    init {
        view.setOnClickListener {
            article?.let {
                val intent = Intent(view.context, ArticleActivity::class.java).apply {
                    putExtra(EXTRA_MESSAGE, article!!.id)
                }
                val activity = view.context as Activity
                activity.startActivity(intent)
                activity.overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        }
    }

    fun bind(article: Article?) {
        if (article == null) {
            title.text = "Loading"
            subtitle.visibility = View.GONE
            author.visibility = View.GONE
            date.visibility = View.GONE
            minutesToRead.visibility = View.GONE
        } else {
            showArticleData(article)
        }
    }

    private fun showArticleData(article: Article) {
        this.article = article
        title.text = article.title
        subtitle.text = article.subtitle
        author.text = article.user.username

        val formatter = DateTimeFormatter.ofPattern("YYYY:MM:DD HH:mm")
        date.text = article.date.format(formatter).toString()

        minutesToRead.text = article.readTimeEstimate.toString()
    }

    companion object {
        fun create(parent: ViewGroup): ArticlesViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_element, parent, false)
            return ArticlesViewHolder(view)
        }
    }
}
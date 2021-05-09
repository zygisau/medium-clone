package com.example.minimum.ui

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.minimum.R
import com.example.minimum.model.Article
import com.example.minimum.model.Comment
import java.time.format.DateTimeFormatter

class CommentsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val name: TextView = view.findViewById(R.id.comment_name);
    private val content: TextView = view.findViewById(R.id.comment);
    private val date: TextView = view.findViewById(R.id.date);

    private var comment: Comment? = null

    fun bind(comment: Comment?) {
        if (comment == null) {
            name.text = "Loading"
            content.visibility = View.GONE
            date.visibility = View.GONE
        } else {
            showCommentData(comment)
        }
    }

    private fun showCommentData(comment: Comment) {
        this.comment = comment
        name.text = comment.name
        content.text = comment.comment

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm")
        date.text = comment.date.format(formatter).toString()
    }

    companion object {
        fun create(parent: ViewGroup): CommentsViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.comments_element, parent, false)
            return CommentsViewHolder(view)
        }
    }
}
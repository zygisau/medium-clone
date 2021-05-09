package com.example.minimum.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.minimum.data.ArticleRepository
import com.example.minimum.data.BookmarksRepository
import com.example.minimum.data.CommentsRepository
import com.example.minimum.viewmodel.ArticleViewModel
import com.example.minimum.viewmodel.ArticlesViewModel
import com.example.minimum.viewmodel.BookmarksViewModel
import com.example.minimum.viewmodel.CommentsViewModel

class ViewModelFactory(
    private val articleRepository: ArticleRepository,
    private val bookmarksRepository: BookmarksRepository? = null,
    private val commentsRepository: CommentsRepository? = null
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ArticlesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ArticlesViewModel(articleRepository) as T
        }
        if (modelClass.isAssignableFrom(ArticleViewModel::class.java)) {
            if (bookmarksRepository == null) {
                throw IllegalArgumentException("This viewmodel requires bookmarksRepository")
            }

            @Suppress("UNCHECKED_CAST")
            return ArticleViewModel(articleRepository, bookmarksRepository) as T
        }
        if (modelClass.isAssignableFrom(BookmarksViewModel::class.java)) {
            if (bookmarksRepository == null) {
                throw IllegalArgumentException("This viewmodel requires bookmarksRepository")
            }

            @Suppress("UNCHECKED_CAST")
            return BookmarksViewModel(bookmarksRepository, articleRepository) as T
        }
        if (modelClass.isAssignableFrom(CommentsViewModel::class.java)) {
            if (commentsRepository == null) {
                throw IllegalArgumentException("This viewmodel requires commentsRepository")
            }

            @Suppress("UNCHECKED_CAST")
            return CommentsViewModel(commentsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
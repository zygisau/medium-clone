package com.example.minimum.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.minimum.data.ArticleRepository
import com.example.minimum.data.BookmarksRepository
import com.example.minimum.model.Article
import kotlinx.coroutines.*

class ArticleViewModel(
    private val articleRepository: ArticleRepository,
    private val bookmarksRepository: BookmarksRepository
) : ViewModel() {
    val article = MutableLiveData<Article>()

    fun initIntentData(intentArticle: Article) {
        article.value = intentArticle
    }

    fun toggleBookmark() {
        val articleId = article.value!!.id
        GlobalScope.launch(Dispatchers.IO) {
            val bookmarkExists = bookmarksRepository.exists(articleId)
            if (!bookmarkExists) {
                bookmarksRepository.insert(articleId)
            } else {
                bookmarksRepository.remove(articleId)
            }
        }
    }

    suspend fun isSavedAsBookmark(): Boolean = coroutineScope {
        val articleId = article.value?.id
        if (articleId != null) {
            return@coroutineScope withContext(Dispatchers.Default) { bookmarksRepository.exists(articleId) }
        }
        return@coroutineScope false
    }

}
package com.example.minimum.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.minimum.data.ArticleRepository
import com.example.minimum.data.BookmarksRepository
import com.example.minimum.model.Article
import com.example.minimum.model.ArticlesFilter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.withContext

class BookmarksViewModel(private val repository: BookmarksRepository,
                         private val articleRepository: ArticleRepository) : ViewModel() {

    private var currentSearchResult: Flow<PagingData<Article>>? = null

    suspend fun getArticles(): Flow<PagingData<Article>>  = coroutineScope {
        val lastResult = currentSearchResult
        if (lastResult != null) {
            return@coroutineScope lastResult
        }
        val articlesList = withContext(Dispatchers.IO) {
            repository.getArticles().map { it.articleId }
        }
//        repository.getArticles().map {
//            it.map { b -> b.articleId }
//        }.collect {
//            val filter = ArticlesFilter(it)
//            val newArticles = articleRepository.getArticlesStream(filter).cachedIn(viewModelScope)
//            currentSearchResult = newArticles
//        }
        val filter = ArticlesFilter(articlesList)
        val newArticles = articleRepository.getArticlesStream(filter).cachedIn(viewModelScope)
        currentSearchResult = newArticles
        return@coroutineScope newArticles
    }
}
package com.example.minimum.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.minimum.data.ArticleRepository
import com.example.minimum.model.Article
import com.example.minimum.model.ArticleResult
import com.example.minimum.model.ArticlesFilter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * ViewModel for the [MainActivity] screen.
 * The ViewModel works with the [ArticleRepository] to get the data.
 */
class ArticlesViewModel(private val repository: ArticleRepository) : ViewModel() {

    private var currentSearchResult: Flow<PagingData<Article>>? = null

    fun getArticles(): Flow<PagingData<Article>> {
        val lastResult = currentSearchResult
        if (lastResult != null) {
            return lastResult
        }
        val newResult: Flow<PagingData<Article>> = repository.getArticlesStream(ArticlesFilter(null))
                .cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }
}
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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * ViewModel for the [MainActivity] screen.
 * The ViewModel works with the [ArticleRepository] to get the data.
 */
class ArticlesViewModel(private val repository: ArticleRepository) : ViewModel() {

    private val result: MutableLiveData<PagingData<Article>> by lazy {
        MutableLiveData<PagingData<Article>>()
    }
    private var currentSearchResult: Flow<PagingData<Article>>? = null
    private var currentSearchQuery: String? = null

    private suspend fun load(query: String? = null) {
        val lastResult = currentSearchResult
        val lastQuery = currentSearchQuery
        if (lastResult != null && lastQuery == query) {
            lastResult.collectLatest {
                result.postValue(it)
            }
            return
        }
        val filter = ArticlesFilter(title = query)
        val newResult: Flow<PagingData<Article>> = repository.getArticlesStream(filter)
                .cachedIn(viewModelScope)
        currentSearchResult = newResult
        currentSearchQuery = query
        newResult.collectLatest {
            result.postValue(it)
        }
    }

    fun loadArticles(query: String?) {
        viewModelScope.launch(){
            load(query)
        }
    }

    fun getArticles(): LiveData<PagingData<Article>> = result
}
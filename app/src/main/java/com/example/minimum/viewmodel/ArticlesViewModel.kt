package com.example.minimum.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.minimum.data.ArticleRepository
import com.example.minimum.model.ArticleResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * ViewModel for the [MainActivity] screen.
 * The ViewModel works with the [ArticleRepository] to get the data.
 */
class ArticlesViewModel(private val repository: ArticleRepository) : ViewModel() {

    companion object {
        private const val VISIBLE_THRESHOLD = 5
    }

    private val queryLiveData = MutableLiveData<String>()
    val articleResult: LiveData<ArticleResult> = queryLiveData.switchMap {
        liveData {
            val articles = repository.getArticlesStream().asLiveData(Dispatchers.Main)
            emitSource(articles)
        }
    }

    fun getArticles() {
        queryLiveData.postValue("more")
    }

    fun listScrolled(visibleItemCount: Int, lastVisibleItemPosition: Int, totalItemCount: Int) {
        if (visibleItemCount + lastVisibleItemPosition + VISIBLE_THRESHOLD >= totalItemCount) {
            val immutableQuery = queryLiveData.value
            if (immutableQuery != null) {
                viewModelScope.launch {
                    repository.requestMore()
                }
            }
        }
    }
}
package com.example.minimum.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.minimum.data.CommentsRepository
import com.example.minimum.model.Comment
import com.example.minimum.model.CommentFilter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CommentsViewModel(private val repository: CommentsRepository) : ViewModel() {

    private val result: MutableLiveData<PagingData<Comment>> by lazy {
        MutableLiveData<PagingData<Comment>>()
    }
    private var currentSearchResult: Flow<PagingData<Comment>>? = null

    private suspend fun load(articleId: String?, force: Boolean = false) {
        val lastResult = currentSearchResult
        if (lastResult != null && !force) {
            lastResult.collectLatest {
                result.postValue(it)
            }
            return
        }
        val filter = CommentFilter(articleId, "date", "desc")
        val newResult: Flow<PagingData<Comment>> = repository.getCommentsStream(filter)
            .cachedIn(viewModelScope)
        currentSearchResult = newResult
        newResult.collectLatest {
            result.postValue(it)
        }
    }

    fun loadComments(articleId: String, force: Boolean = false) {
        viewModelScope.launch(){
            load(articleId, force)
        }
    }

    fun getComments(): LiveData<PagingData<Comment>> = result

    fun submitComment(articleId: String, commentName: String, commentContent: String) {
        viewModelScope.launch(){
            repository.submitComment(articleId, commentName, commentContent)
        }
    }
}
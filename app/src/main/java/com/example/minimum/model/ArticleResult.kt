package com.example.minimum.model

sealed class ArticleResult {
    data class Success(val data: List<Article>) : ArticleResult()
    data class Error(val error: Exception) : ArticleResult()
}
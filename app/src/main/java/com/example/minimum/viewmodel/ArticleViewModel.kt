package com.example.minimum.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.minimum.data.ArticleRepository
import com.example.minimum.model.Article

class ArticleViewModel(private val repository: ArticleRepository) : ViewModel() {
    val article = MutableLiveData<Article>()

    fun initIntentData(intentArticle: Article) {
        article.value = intentArticle
    }

}
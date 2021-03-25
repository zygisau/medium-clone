package com.example.minimum.api

import com.example.minimum.model.Article
import com.google.gson.annotations.SerializedName

data class ArticleResponse(
    @SerializedName("total_count") val total: Int = 0,
    @SerializedName("items") val items: List<Article> = emptyList(),
    val nextPage: Int? = null
)

package com.example.minimum.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class Article(
        @field:SerializedName("id") val id: String,
        @field:SerializedName("blog") val blog: Blog,
        @field:SerializedName("user") val user: User,
        @field:SerializedName("title") val title: String,
        @field:SerializedName("subtitle") val subtitle: String,
        @field:SerializedName("image") val image: String,
        @field:SerializedName("date") val date: LocalDateTime,
        @field:SerializedName("readTimeEstimate") val readTimeEstimate: Int,
        @field:SerializedName("contentMarkup") val contentMarkup: String? = null,
        @field:SerializedName("isLargePreview") val isLargePreview: Boolean = false,
        @field:SerializedName("likes") val likes: Int = 0,
        @field:SerializedName("tags") val tags: List<String>? = null) {
}
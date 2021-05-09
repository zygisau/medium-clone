package com.example.minimum.model

import com.google.gson.annotations.SerializedName

class CommentPayload (
        @SerializedName("articleId") val articleId: String,
        @SerializedName("name") val name: String,
        @SerializedName("comment") val comment: String,
        @SerializedName("date") val date: String
)
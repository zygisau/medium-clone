package com.example.minimum.model

import com.google.gson.annotations.SerializedName

class Blog(
        @field:SerializedName("id") val id: String,
        @field:SerializedName("userId") val userId: String,
        @field:SerializedName("name") val name: String,
        @field:SerializedName("description") val description: String
)
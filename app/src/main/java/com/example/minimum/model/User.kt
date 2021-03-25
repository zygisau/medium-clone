package com.example.minimum.model

import com.google.gson.annotations.SerializedName

class User(
        @field:SerializedName("id") val id: String,
        @field:SerializedName("username") val username: String,
        @field:SerializedName("avatarUrl") val avatarUrl: String,
        @field:SerializedName("bio") val bio: String
)
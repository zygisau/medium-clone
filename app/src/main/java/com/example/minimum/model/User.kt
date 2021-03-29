package com.example.minimum.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class User(
        @field:SerializedName("id") val id: String,
        @field:SerializedName("username") val username: String,
        @field:SerializedName("avatarUrl") val avatarUrl: String,
        @field:SerializedName("bio") val bio: String
) : Parcelable
package com.example.minimum.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class Blog(
        @field:SerializedName("id") val id: String,
        @field:SerializedName("userId") val userId: String,
        @field:SerializedName("name") val name: String,
        @field:SerializedName("description") val description: String
) : Parcelable
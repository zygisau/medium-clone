package com.example.minimum.storage.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Bookmark(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "article_id") val articleId: String
)
package com.example.minimum.storage.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Settings(
        @PrimaryKey val key: String,
        @ColumnInfo(name = "value") val value: String
)
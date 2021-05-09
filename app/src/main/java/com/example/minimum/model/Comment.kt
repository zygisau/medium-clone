package com.example.minimum.model

import java.time.LocalDateTime

data class Comment(
    val id: String,
    val name: String,
    val comment: String,
    val date: LocalDateTime,
)
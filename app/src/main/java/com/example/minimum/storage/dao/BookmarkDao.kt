package com.example.minimum.storage.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import com.example.minimum.storage.model.Bookmark
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkDao {
    @Query("SELECT * FROM bookmark")
    fun getAll(): List<Bookmark>

    @Query("SELECT * FROM bookmark WHERE article_id = (:articleId)")
    fun get(articleId: String): Flow<Bookmark?>

    @Query("INSERT INTO bookmark (article_id) VALUES (:articleId)")
    suspend fun set(articleId: String)

    @Query("DELETE FROM bookmark WHERE article_id = (:articleId)")
    suspend fun remove(articleId: String)

    @Query("SELECT EXISTS (SELECT 1 FROM bookmark WHERE article_id = :id)")
    fun exists(id: String): Boolean
}
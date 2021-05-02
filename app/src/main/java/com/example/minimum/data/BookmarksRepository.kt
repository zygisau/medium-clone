package com.example.minimum.data

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.minimum.model.Article
import com.example.minimum.storage.dao.BookmarkDao
import com.example.minimum.storage.model.Bookmark
import kotlinx.coroutines.flow.Flow

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class BookmarksRepository(private val bookmarksDao: BookmarkDao) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
//    val all: List<Bookmark> = bookmarksDao.getAll()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun exists(id: String): Boolean {
        return bookmarksDao.exists(id)
    }

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(articleId: String) {
        bookmarksDao.set(articleId)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun get(articleId: String): LiveData<Bookmark?> {
        return bookmarksDao.get(articleId).asLiveData()
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun remove(articleId: String) {
        return bookmarksDao.remove(articleId)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getArticles(): List<Bookmark> {
        return bookmarksDao.getAll()
    }
}
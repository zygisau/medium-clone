package com.example.minimum.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.minimum.api.ArticleService
import com.example.minimum.model.Comment
import com.example.minimum.model.CommentFilter
import com.example.minimum.model.CommentPayload
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CommentsRepository(private val service: ArticleService) {

    fun getCommentsStream(filter: CommentFilter): Flow<PagingData<Comment>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { CommentsPagingSource(service, filter) }
        ).flow
    }

    suspend fun submitComment(articleId: String, commentName: String, commentContent: String) {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        val commentDate = current.format(formatter)

        val commentPayload = CommentPayload(articleId, commentName, commentContent, commentDate)
        service.submitComment(commentPayload)
    }

    companion object {
        public const val NETWORK_PAGE_SIZE = 20
    }
}
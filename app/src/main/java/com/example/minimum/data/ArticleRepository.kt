package com.example.minimum.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.minimum.api.ArticleService
import com.example.minimum.model.Article
import com.example.minimum.model.ArticleResult
import com.example.minimum.model.ArticlesFilter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import retrofit2.HttpException
import java.io.IOException

private const val ARTICLE_STARTING_PAGE_INDEX = 0

class ArticleRepository(private val service: ArticleService) {

    fun getArticlesStream(filter: ArticlesFilter): Flow<PagingData<Article>> {
        return Pager(
                config = PagingConfig(
                        pageSize = NETWORK_PAGE_SIZE,
                        enablePlaceholders = false
                ),
                pagingSourceFactory = { ArticlePagingSource(service, filter) }
        ).flow
    }

    suspend fun getArticle(articleId: String): Article {
        return service.getArticle(articleId)
    }

    companion object {
        public const val NETWORK_PAGE_SIZE = 20
    }
}
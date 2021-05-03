package com.example.minimum.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.minimum.api.ArticleService
import com.example.minimum.data.ArticleRepository.Companion.NETWORK_PAGE_SIZE
import com.example.minimum.model.Article
import com.example.minimum.model.ArticlesFilter
import retrofit2.HttpException
import java.io.IOException

private const val ARTICLE_STARTING_PAGE_INDEX = 0

class ArticlePagingSource (
        private val service: ArticleService,
        private val filter: ArticlesFilter
) : PagingSource<Int, Article>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val position = params.key ?: ARTICLE_STARTING_PAGE_INDEX
        return try {
            val articles = service.getArticles(filter.id, filter.title, position, params.loadSize)
            val nextKey = if (articles.isEmpty()) {
                null
            } else {
                position + (params.loadSize / NETWORK_PAGE_SIZE)
            }
            LoadResult.Page(
                    data = articles,
                    prevKey = if (position == ARTICLE_STARTING_PAGE_INDEX) null else position - 1,
                    nextKey = nextKey
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
    @ExperimentalPagingApi
    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        // We need to get the previous key (or next key if previous is null) of the page
        // that was closest to the most recently accessed index.
        // Anchor position is the most recently accessed index
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                    ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

}
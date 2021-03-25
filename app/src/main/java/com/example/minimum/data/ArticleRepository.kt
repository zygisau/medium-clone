package com.example.minimum.data

import com.example.minimum.api.ArticleService
import com.example.minimum.model.Article
import com.example.minimum.model.ArticleResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import retrofit2.HttpException
import java.io.IOException

private const val ARTICLE_STARTING_PAGE_INDEX = 0

class ArticleRepository(private val service: ArticleService) {

    // keep the list of all results received
    private val inMemoryCache = mutableListOf<Article>()

    // shared flow of results, which allows us to broadcast updates so
    // the subscriber will have the latest data
    private val searchResults = MutableSharedFlow<ArticleResult>(replay = 1)

    // keep the last requested page. When the request is successful, increment the page number.
    private var lastRequestedPage = ARTICLE_STARTING_PAGE_INDEX

    // avoid triggering multiple requests in the same time
    private var isRequestInProgress = false

    /**
     * Fetch articles, exposed as a stream of data that will emit
     * every time we get more data from the network.
     */
    suspend fun getArticlesStream(): Flow<ArticleResult> {
        lastRequestedPage = ARTICLE_STARTING_PAGE_INDEX
        inMemoryCache.clear()
        requestAndSaveData()

        return searchResults
    }

    suspend fun requestMore() {
        if (isRequestInProgress) return
        val successful = requestAndSaveData()
        if (successful) {
            lastRequestedPage++
        }
    }

    suspend fun retry() {
        if (isRequestInProgress) return
        requestAndSaveData()
    }

    private suspend fun requestAndSaveData(): Boolean {
        isRequestInProgress = true
        var successful = false

        try {
            val response = service.getArticles(lastRequestedPage, NETWORK_PAGE_SIZE)
            val articles = response
            inMemoryCache.addAll(articles)
            searchResults.emit(ArticleResult.Success(inMemoryCache))
            successful = true
        } catch (exception: IOException) {
            searchResults.emit(ArticleResult.Error(exception))
        } catch (exception: HttpException) {
            searchResults.emit(ArticleResult.Error(exception))
        }
        isRequestInProgress = false
        return successful
    }

    companion object {
        public const val NETWORK_PAGE_SIZE = 20
    }
}
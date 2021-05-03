package com.example.minimum.api

import com.example.minimum.model.Article
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


interface ArticleService {
    @GET("posts")
    suspend fun getArticles(
            @Query("id") id: List<String>? = null,
            @Query("title") title: String? = null,
            @Query("_page") page: Int = 0,
            @Query("_limit") limit: Int = 20): List<Article>
    @GET("posts/{id}")
    suspend fun getArticle(@Path("id") id: String): Article

    companion object {
        private const val BASE_URL = "http://10.0.2.2:3000/"
        private const val TIMESTAMP_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"

        fun create(): ArticleService {
            val logger = HttpLoggingInterceptor()
            logger.level = Level.BASIC

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            val gson = GsonBuilder()
                .setDateFormat(TIMESTAMP_FORMAT)
                .registerTypeAdapter(
                    LocalDateTime::class.java,
                    JsonDeserializer() { json, _, _ ->
                        if (json != null) {
                            val formatter: DateTimeFormatter =
                                DateTimeFormatter.ofPattern(TIMESTAMP_FORMAT)
                            LocalDateTime.parse(json.asString, formatter);
                        }
                        LocalDateTime.now()
                    })
                .create()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(ArticleService::class.java)
        }
    }
}
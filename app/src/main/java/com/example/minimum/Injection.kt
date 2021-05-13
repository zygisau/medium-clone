package com.example.minimum

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.example.minimum.api.ArticleService
import com.example.minimum.data.ArticleRepository
import com.example.minimum.data.BookmarksRepository
import com.example.minimum.data.CommentsRepository
import com.example.minimum.data.SettingsRepository
import com.example.minimum.factory.ViewModelFactory
import com.example.minimum.storage.AppDatabase

/**
 * Class that handles object creation.
 * Like this, objects can be passed as parameters in the constructors and then replaced for
 * testing, where needed.
 */
object Injection {

    /**
     * Provides the [ViewModelProvider.Factory] that is then used to get a reference to
     * [ViewModel] objects.
     */
    fun provideArticlesViewModelFactory(): ViewModelProvider.Factory {
        return ViewModelFactory(provideArticleRepository())
    }

    fun provideBookmarksViewModelFactory(context: Context): ViewModelProvider.Factory {
        return ViewModelFactory(provideArticleRepository(), provideBookmarksRepository(context))
    }

    fun provideArticleViewModelFactory(context: Context): ViewModelProvider.Factory {
        return ViewModelFactory(provideArticleRepository(), provideBookmarksRepository(context))
    }

    fun provideCommentsViewModelFactory(): ViewModelProvider.Factory {
        return ViewModelFactory(provideArticleRepository(), null, provideCommentsRepository())
    }

    fun provideProfileViewModelFactory(context: Context): ViewModelProvider.Factory {
        return ViewModelFactory(provideArticleRepository(), null, null, provideSettingsRepository(context))
    }

    /**
     * Creates an instance of [ArticleRepository] based on the [ArticleService] and a
     * [ArticleLocalCache]
     */
    private fun provideArticleRepository(): ArticleRepository {
        return ArticleRepository(getArticleService())
    }

    private fun provideBookmarksRepository(context: Context): BookmarksRepository {
        return BookmarksRepository(AppDatabase.getInstance(context).bookmarkDao())
    }

    private fun provideCommentsRepository(): CommentsRepository {
        return CommentsRepository(getArticleService())
    }

    private fun provideSettingsRepository(context: Context): SettingsRepository {
        return SettingsRepository(AppDatabase.getInstance(context).settingsDao())
    }

    fun getArticleService(): ArticleService {
        return ArticleService.create()
    }
}
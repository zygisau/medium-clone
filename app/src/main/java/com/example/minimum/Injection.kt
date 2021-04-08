package com.example.minimum

import androidx.lifecycle.ViewModelProvider
import com.example.minimum.api.ArticleService
import com.example.minimum.data.ArticleRepository
import com.example.minimum.viewmodel.ViewModelFactory

/**
 * Class that handles object creation.
 * Like this, objects can be passed as parameters in the constructors and then replaced for
 * testing, where needed.
 */
object Injection {

    /**
     * Creates an instance of [ArticleRepository] based on the [ArticleService] and a
     * [ArticleLocalCache]
     */
    private fun provideArticleRepository(): ArticleRepository {
        return ArticleRepository(ArticleService.create())
    }

    /**
     * Provides the [ViewModelProvider.Factory] that is then used to get a reference to
     * [ViewModel] objects.
     */
    fun provideArticlesViewModelFactory(): ViewModelProvider.Factory {
        return ViewModelFactory(provideArticleRepository())
    }
}
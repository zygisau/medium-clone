package com.example.minimum.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.minimum.Injection
import com.example.minimum.Notification
import com.example.minimum.model.Article
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null) return

        val notification = Notification(context)
        val intent = getArticleIntent(context)
        notification.createNotification(intent)
        notification.sendNotification()
    }

    private fun getArticleIntent(context: Context): Intent {
        val intent = Intent(context, ArticleActivity::class.java).apply {
            putExtra(ARTICLE_ITEM_KEY, getRandomArticle())
        }
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        return intent
    }

    private fun getRandomArticle(): Article = runBlocking {
        val service = Injection.getArticleService()
        val articleId = getRandomArticleId()
        var article: Article? = null
        val job: Job = launch(context = Dispatchers.Default) {
            article = service.getArticle(articleId)
        }
        job.join()
        return@runBlocking article!!
    }

    private fun getRandomArticleId(): String {
        return (0..51).random().toString()
    }
}
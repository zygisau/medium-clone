package com.example.minimum.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.minimum.R
import com.example.minimum.api.ArticleService


class ArticleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
    }
}
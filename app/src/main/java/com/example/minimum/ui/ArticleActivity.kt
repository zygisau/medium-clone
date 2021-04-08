package com.example.minimum.ui

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import com.example.minimum.Injection
import com.example.minimum.R
import com.example.minimum.databinding.ActivityArticleBinding
import com.example.minimum.model.Article
import com.example.minimum.utils.observe
import com.example.minimum.viewmodel.ArticleViewModel
import com.squareup.picasso.Picasso
import java.time.format.DateTimeFormatter


class ArticleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityArticleBinding
    private lateinit var viewModel: ArticleViewModel
//    private var scrollLayoutHeight: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewBinding()

        viewModel = ViewModelProvider(this, Injection.provideArticlesViewModelFactory())
                .get(ArticleViewModel::class.java)

        recoverArticle()
        observeViewModel()
//        getLayoutHeight()
        startObservingContentScroll()
    }

//    private fun getLayoutHeight() {
//        binding.articleRoot.viewTreeObserver.addOnGlobalLayoutListener(
//                object : ViewTreeObserver.OnGlobalLayoutListener {
//                    override fun onGlobalLayout() {
//                        scrollLayoutHeight = binding.contentScroll.measuredHeight
//                        binding.articleRoot.viewTreeObserver.removeOnGlobalLayoutListener(this)
//                    }
//                })
//    }

    private fun initViewBinding() {
        binding = ActivityArticleBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    private fun recoverArticle() {
        val article: Article? = intent.getParcelableExtra(ARTICLE_ITEM_KEY)
        if (article != null) {
            viewModel.initIntentData(article)
        } else {
            this.finish()
        }
    }

    private fun observeViewModel() {
        observe(viewModel.article, ::initializeView)
    }

    private fun initializeView(articleItem: Article) {
        binding.articleTitle.text = articleItem.title
        binding.subtitle.text = articleItem.subtitle
        binding.author.text = articleItem.user.username

        bindDate(articleItem)
        bindPhoto(articleItem)
        bindContent(articleItem)
    }

    private fun startObservingContentScroll() {
        binding.contentContainer.viewTreeObserver.addOnScrollChangedListener{
            val screenHeight = (this as Context).resources.displayMetrics.heightPixels
            val scrollY = binding.contentScroll.scrollY
            val scrolledHeight = scrollY + screenHeight
            val newVisibility = if (scrolledHeight > binding.contentContainer.height) View.GONE else View.VISIBLE
            Log.d(Log.DEBUG.toString(), "screenHeight: $screenHeight scrollY: $scrollY scrolledHeight: $scrolledHeight newVisibility: $newVisibility")
            if (binding.contentFooterTemp.visibility != newVisibility) {
                Handler(Looper.getMainLooper()).post {
                    binding.contentFooterTemp.visibility = newVisibility
                }
            }
        }
    }

    private fun bindContent(item: Article) {
        binding.content.text = Html.fromHtml(item.contentMarkup, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    private fun bindPhoto(item: Article) {
        binding.articleImage.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.articleImage.viewTreeObserver.removeOnGlobalLayoutListener(this)

                Picasso.get()
                        .load(item.image)
                        .resize(binding.articleImage.width, 0)
                        .into(binding.articleImage)
            }
        })
    }

    private fun bindDate(item: Article) {
        val formatter = DateTimeFormatter.ofPattern("YYYY:MM:DD HH:mm")
        binding.date.text = item.date.format(formatter).toString()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
    }
}
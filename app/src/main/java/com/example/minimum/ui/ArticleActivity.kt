package com.example.minimum.ui

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.minimum.Injection
import com.example.minimum.R
import com.example.minimum.databinding.ActivityArticleBinding
import com.example.minimum.model.Article
import com.example.minimum.utils.SimpleFragmentManager
import com.example.minimum.utils.observe
import com.example.minimum.viewmodel.ArticleViewModel
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import io.noties.markwon.Markwon
import io.noties.markwon.html.HtmlPlugin
import io.noties.markwon.image.AsyncDrawable
import io.noties.markwon.image.picasso.PicassoImagesPlugin
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter


class ArticleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityArticleBinding
    private lateinit var viewModel: ArticleViewModel
    private var isFabsHidden: Boolean = false
    private val simpleFragmentManager = SimpleFragmentManager(this)

    private var loadJob: Job? = null;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewBinding()

        viewModel = ViewModelProvider(this, Injection.provideArticleViewModelFactory(this.applicationContext.applicationContext))
                .get(ArticleViewModel::class.java)

        configureBackButton()
        recoverArticle()
        observeViewModel()
        loadBookmarkIfSaved()
        startObservingContentScroll()
        setFloatingButtonsListeners()

        simpleFragmentManager.addFragment(CommentsFragment::class.java, R.id.fragment_comments_view)
    }

    private fun loadBookmarkIfSaved() {
        viewModel.article.observe(this) {
            loadJob?.cancel()
            loadJob = lifecycleScope.launch {
                binding.bookmarkButton.isChecked = viewModel.isSavedAsBookmark()
            }
        }
    }

    private fun configureBackButton() {
        binding.searchIcon.setOnClickListener {
            Handler(Looper.getMainLooper()).post {
                this.onBackPressed();
            }
        }
    }

    private fun initViewBinding() {
        binding = ActivityArticleBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setSupportActionBar(findViewById(R.id.app_toolbar));
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
        binding.articleMain.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, _ ->
            val screenHeight = (this as Context).resources.displayMetrics.heightPixels
            val scrolledHeight = scrollY + screenHeight
            val newVisibility = if (scrolledHeight > binding.contentContainer.height) View.GONE else View.VISIBLE
            var newIsFabsHidden = newVisibility == View.GONE
            if (binding.floatingLike.visibility != newVisibility && newIsFabsHidden != isFabsHidden) {
                isFabsHidden = newVisibility == View.GONE
                if (newVisibility == View.GONE) {
                    binding.floatingLike.hide()
                    binding.floatingComment.hide()
//                    binding.floatingShare.hide()
                } else {
                    binding.floatingLike.show()
                    binding.floatingComment.show()
//                    binding.floatingShare.show()
                }
            }
        })
    }

    private fun bindContent(item: Article) {
        val markwon = Markwon.builder(this)
                .usePlugin(HtmlPlugin.create())
                .usePlugin(PicassoImagesPlugin.create(this))
                .usePlugin(PicassoImagesPlugin.create(Picasso.get()))
                .usePlugin(PicassoImagesPlugin.create(object : PicassoImagesPlugin.PicassoStore {
                    override fun load(drawable: AsyncDrawable): RequestCreator {
                        return Picasso.get()
                                .load(drawable.destination)
                                .tag(drawable);
                    }

                    override fun cancel(drawable: AsyncDrawable) {
                        Picasso.get()
                                .cancelTag(drawable);
                    }
                }))
                .build();
        item.contentMarkup?.let { markwon.setMarkdown(binding.content, it) };
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
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm")
        binding.date.text = item.date.format(formatter).toString()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
    }

    private fun setFloatingButtonsListeners() {
        binding.bookmarkButton.setOnClickListener {
            viewModel.toggleBookmark()
        }
        binding.commentButton.setOnClickListener {
            scrollToComments()
        }
        binding.floatingComment.setOnClickListener {
            scrollToComments()
        }
    }

    private fun scrollToComments() {
        val target = findViewById<View>(R.id.comments_fragment_container)
        binding.articleMain.post {
            binding.articleMain.smoothScrollTo(0, target.top)
        }
    }

}
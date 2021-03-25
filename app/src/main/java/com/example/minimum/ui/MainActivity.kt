package com.example.minimum.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.minimum.Injection
import com.example.minimum.R
import com.example.minimum.databinding.ActivityMainBinding
import com.example.minimum.model.ArticleResult
import com.example.minimum.viewmodel.ArticlesViewModel


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: ArticlesViewModel
    private val adapter = ArticlesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setSupportActionBar(findViewById(R.id.app_toolbar));

        viewModel = ViewModelProvider(this, Injection.provideViewModelFactory())
            .get(ArticlesViewModel::class.java)

        val decoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        binding.listRecycleView.addItemDecoration(decoration)
        setupScrollListener()

        initAdapter()
        if (viewModel.articleResult.value == null) {
            viewModel.getArticles()
        }
    }

    private fun initAdapter() {
        binding.listRecycleView.adapter = adapter
        viewModel.articleResult.observe(this, Observer<ArticleResult> { result ->
            when (result) {
                is ArticleResult.Success -> {
                    showEmptyList(result.data.isEmpty())
                    adapter.submitList(result.data)
                }
                is ArticleResult.Error -> {
                    Toast.makeText(
                        this,
                        "\uD83D\uDE28 Wooops $result.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        })
    }

    private fun showEmptyList(show: Boolean) {
        if (show) {
            binding.emptyList.visibility = View.VISIBLE
            binding.listRecycleView.visibility = View.GONE
        } else {
            binding.emptyList.visibility = View.GONE
            binding.listRecycleView.visibility = View.VISIBLE
        }
    }

    private fun setupScrollListener() {
        val layoutManager = binding.listRecycleView.layoutManager as LinearLayoutManager
        binding.listRecycleView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = layoutManager.itemCount
                val visibleItemCount = layoutManager.childCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                viewModel.listScrolled(visibleItemCount, lastVisibleItem, totalItemCount)
            }
        })
    }
}
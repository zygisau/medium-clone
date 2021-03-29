package com.example.minimum.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.minimum.Injection
import com.example.minimum.R
import com.example.minimum.databinding.ActivityMainBinding
import com.example.minimum.model.ArticleResult
import com.example.minimum.viewmodel.ArticlesViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: ArticlesViewModel
    private val adapter = ArticlesAdapter()

    private var loadJob: Job? = null

    private fun loadData() {
        loadJob?.cancel()
        loadJob = lifecycleScope.launch {
            viewModel.getArticles().collectLatest {
                adapter.submitData(it)
            }
        }
    }

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

        initAdapter()
        loadData()
    }

    private fun initAdapter() {
        binding.listRecycleView.adapter = adapter
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
}
package com.example.minimum.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.minimum.Injection
import com.example.minimum.R
import com.example.minimum.databinding.ArticlesFragmentBinding
import com.example.minimum.viewmodel.ArticlesViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class ArticlesFragment : Fragment(R.layout.articles_fragment) {
    private lateinit var binding: ArticlesFragmentBinding
    private lateinit var viewModel: ArticlesViewModel
    private lateinit var adapter: ArticlesAdapter

    private var loadJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ArticlesFragmentBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this, Injection.provideArticlesViewModelFactory())
                .get(ArticlesViewModel::class.java)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.articles_fragment, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.listRecycleView)
        adapter = ArticlesAdapter()

        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        val decoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(decoration)

        val toolbar: Toolbar = view.findViewById(R.id.app_toolbar) as Toolbar
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadData()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun loadData() {
        loadJob?.cancel()
        loadJob = lifecycleScope.launch {
            viewModel.getArticles().collectLatest {
                adapter.submitData(it)
            }
        }
    }
}
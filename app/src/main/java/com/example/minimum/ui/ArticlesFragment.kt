package com.example.minimum.ui

import android.os.Bundle
import android.view.*
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.minimum.Injection
import com.example.minimum.R
import com.example.minimum.databinding.ArticlesFragmentBinding
import com.example.minimum.viewmodel.ArticlesViewModel
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.miguelcatalan.materialsearchview.MaterialSearchView
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class ArticlesFragment : Fragment(R.layout.articles_fragment) {
    private lateinit var binding: ArticlesFragmentBinding
    private lateinit var viewModel: ArticlesViewModel
    private lateinit var adapter: ArticlesAdapter

    private lateinit var searchView: MaterialSearchView
    private lateinit var progressBar: LinearProgressIndicator

    private var loadJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ArticlesFragmentBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this, Injection.provideArticlesViewModelFactory())
                .get(ArticlesViewModel::class.java)
        registerArticlesObserver()
        super.onCreate(savedInstanceState)
    }

    private fun registerArticlesObserver() {
        viewModel.getArticles().observe(this, { pagedList ->
            adapter.submitData(lifecycle, pagedList)
            progressBar.hide()
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.articles_fragment, container, false)
        registerRecyclerView(view)
        registerActionToolbar(view)
        registerSearchView(view)
        progressBar = view.findViewById(R.id.progress_bar)
        return view
    }

    private fun registerSearchView(view: View) {
        searchView = view.findViewById(R.id.search_view) as MaterialSearchView
        searchView.setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                loadData(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                val MINIMUM_SEARCH_LENGTH = 3
                if (newText.length >= MINIMUM_SEARCH_LENGTH) {
                    loadData(newText)
                }
                return false
            }
        })
        view.findViewById<ImageButton>(com.miguelcatalan.materialsearchview.R.id.action_up_btn).setOnClickListener {
            loadData()
            searchView.closeSearch()
        }
    }

    private fun registerActionToolbar(view: View) {
        val toolbar: Toolbar = view.findViewById(R.id.app_toolbar) as Toolbar
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
    }

    private fun registerRecyclerView(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.listRecycleView)
        adapter = ArticlesAdapter()

        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        val decoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(decoration)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadData()
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.app_bar, menu)
        val item = menu.findItem(R.id.search)
        searchView.setMenuItem(item);
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun loadData(query: String? = null) {
        progressBar.show()
        viewModel.loadArticles(query)
    }
}
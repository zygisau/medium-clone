package com.example.minimum.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat.invalidateOptionsMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.minimum.Injection
import com.example.minimum.R
import com.example.minimum.databinding.BookmarksFragmentBinding
import com.example.minimum.viewmodel.BookmarksViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class BookmarksFragment: Fragment(R.layout.bookmarks_fragment) {
    private lateinit var binding: BookmarksFragmentBinding
    private lateinit var viewModel: BookmarksViewModel
    private lateinit var adapter: ArticlesAdapter

    private var loadJob: Job? = null

    private lateinit var listListener: Unit

    override fun onAttach(context: Context) {
        viewModel = ViewModelProvider(this, Injection.provideBookmarksViewModelFactory(context))
                .get(BookmarksViewModel::class.java)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = BookmarksFragmentBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.bookmarks_fragment, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.listRecycleView)
        adapter = ArticlesAdapter()

        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        val decoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(decoration)

        val toolbar: Toolbar = view.findViewById(R.id.app_toolbar) as Toolbar
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        determineViewByItemsCount(view)
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

    private fun determineViewByItemsCount(view: View) {
        listListener = adapter.addLoadStateListener() {
            if (adapter.itemCount >= 1) {
                view.findViewById<RecyclerView>(R.id.listRecycleView).visibility = View.VISIBLE
                view.findViewById<TextView>(R.id.emptyList).visibility = View.GONE
            } else {
                view.findViewById<RecyclerView>(R.id.listRecycleView).visibility = View.GONE
                view.findViewById<TextView>(R.id.emptyList).visibility = View.VISIBLE
            }
        }
    }
}
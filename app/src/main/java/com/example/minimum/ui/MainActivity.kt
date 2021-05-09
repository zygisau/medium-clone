package com.example.minimum.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.minimum.Injection
import com.example.minimum.R
import com.example.minimum.databinding.ActivityMainBinding
import com.example.minimum.utils.SimpleFragmentManager
import com.example.minimum.viewmodel.ArticlesViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: ArticlesViewModel
    private val simpleFragmentManager = SimpleFragmentManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setBottomNavigationListeners()

        viewModel = ViewModelProvider(this, Injection.provideArticlesViewModelFactory())
            .get(ArticlesViewModel::class.java)
    }

    private fun setBottomNavigationListeners() {
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val navigationItemSelectedListener =
            BottomNavigationView.OnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.navigation_home -> {
                        simpleFragmentManager.replaceFragment(ArticlesFragment::class.java, R.id.fragment_container_view)
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.navigation_bookmarks -> {
                        simpleFragmentManager.replaceFragment(BookmarksFragment::class.java, R.id.fragment_container_view)
                        return@OnNavigationItemSelectedListener true
                    }
                }
                false
            }
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        simpleFragmentManager.addFragment(ArticlesFragment::class.java, R.id.fragment_container_view)
    }
}
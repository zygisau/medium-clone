package com.example.minimum.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.minimum.Injection
import com.example.minimum.R
import com.example.minimum.databinding.ActivityMainBinding
import com.example.minimum.viewmodel.ArticlesViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: ArticlesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
//        setSupportActionBar(findViewById(R.id.app_toolbar));
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
                        replaceFragment(ArticlesFragment::class.java)
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.navigation_bookmarks -> {
                        replaceFragment(BookmarksFragment::class.java)
                        return@OnNavigationItemSelectedListener true
                    }
                }
                false
            }
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        addFragment(ArticlesFragment::class.java)
    }

    private fun <T: Fragment> replaceFragment(fragmentClass: Class<T>) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container_view, fragmentClass, null)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun <T: Fragment> addFragment(fragmentClass: Class<T>) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fragment_container_view, fragmentClass, null)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
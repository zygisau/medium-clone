package com.example.minimum.ui

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.minimum.Injection
import com.example.minimum.R
import com.example.minimum.databinding.CommentsFragmentBinding
import com.example.minimum.viewmodel.ArticleViewModel
import com.example.minimum.viewmodel.CommentsViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout

class CommentsFragment() : Fragment(R.layout.comments_fragment) {
    private lateinit var binding: CommentsFragmentBinding
    private lateinit var adapter: CommentsAdapter
    private lateinit var viewModel: CommentsViewModel
    private lateinit var articleViewModel: ArticleViewModel
    private lateinit var articleId: String

    private var nameHasContent = false
    private var commentHasContent = false

    private lateinit var commentsListener: Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = CommentsFragmentBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this, Injection.provideCommentsViewModelFactory())
            .get(CommentsViewModel::class.java)
        articleViewModel = ViewModelProvider(requireActivity()).get(ArticleViewModel::class.java)
        registerArticleListener()
        registerCommentsListener()
        super.onCreate(savedInstanceState)
    }

    private fun registerArticleListener() {
        articleViewModel.article.observe(this) {
            it.id.let { id ->
                articleId = id
                viewModel.loadComments(id)
            }
        }
    }

    private fun registerCommentsListener() {
        viewModel.getComments().observe(this) { pagedList ->
            adapter.submitData(lifecycle, pagedList)
        }
    }

    private fun determineViewByItemsCount(view: View) {
        commentsListener = adapter.addLoadStateListener() {
            if (adapter.itemCount >= 1) {
                view.findViewById<RecyclerView>(R.id.commentsRecyclerView).visibility = View.VISIBLE
                view.findViewById<TextView>(R.id.no_comments).visibility = View.GONE
            } else {
                view.findViewById<RecyclerView>(R.id.commentsRecyclerView).visibility = View.GONE
                view.findViewById<TextView>(R.id.no_comments).visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.comments_fragment, container, false)
        registerRecyclerView(view)
        determineViewByItemsCount(view)

        onInputsEnter(view)
        onSubmit(view)
        return view
    }

    private fun registerRecyclerView(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.commentsRecyclerView)
        adapter = CommentsAdapter()

        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        val decoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(decoration)
    }

    private fun onInputsEnter(view: View) {
        view.findViewById<TextInputLayout>(R.id.comment_input_name).editText?.doOnTextChanged { inputText, _, _, _ ->
            if (inputText != null) {
                nameHasContent = inputText.isNotBlank()
                checkSubmitButton(view)
            }
        }
        view.findViewById<TextInputLayout>(R.id.comment_input_content).editText?.doOnTextChanged { inputText, _, _, _ ->
            if (inputText != null) {
                commentHasContent = inputText.isNotBlank()
                checkSubmitButton(view)
            }
        }
    }

    private fun checkSubmitButton(view: View) {
        view.findViewById<MaterialButton>(R.id.send_comment).isEnabled = nameHasContent && commentHasContent
    }

    private fun onSubmit(view: View) {
        view.findViewById<MaterialButton>(R.id.send_comment).setOnClickListener() {
            var commentName = view.findViewById<TextInputLayout>(R.id.comment_input_name).editText
            val commentContent = view.findViewById<TextInputLayout>(R.id.comment_input_content).editText
            val commentNameText = commentName?.text.toString()
            val commentContentText = commentContent?.text.toString()
            if (it != null && commentNameText.isNotBlank() && commentContentText.isNotBlank()) {
                viewModel.submitComment(articleId, commentNameText, commentContentText)

                commentName?.setText("")
                commentContent?.setText("")

                Toast.makeText(context, "Comment submitted", Toast.LENGTH_SHORT).show()

                viewModel.loadComments(articleId, force = true)
            }
        }
    }
}
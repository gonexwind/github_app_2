package com.fikky.githubuserapp.view.ui.search

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.fikky.githubuserapp.R
import com.fikky.githubuserapp.databinding.ActivitySearchBinding
import com.fikky.githubuserapp.service.model.User
import com.fikky.githubuserapp.service.util.Resource
import com.fikky.githubuserapp.service.util.StateCallback
import com.fikky.githubuserapp.view.adapter.UserAdapter
import com.fikky.githubuserapp.viewmodel.SearchViewModel

class SearchActivity : AppCompatActivity(), StateCallback<List<User>> {
    private lateinit var binding: ActivitySearchBinding
    private lateinit var searchQuery: String
    private lateinit var userAdapter: UserAdapter
    private val viewModel by viewModels<SearchViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userAdapter = UserAdapter()
        with(binding) {
            includeMainSearch.listUserRecyclerView.apply {
                adapter = userAdapter
                layoutManager =
                    LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)
            }

            searchView.apply {
                queryHint = resources.getString(R.string.search_hint)
                setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        searchQuery = query.toString()
                        clearFocus()
                        viewModel.searchUser(searchQuery).observe(this@SearchActivity) { a ->
                            when (a) {
                                is Resource.Loading -> onLoading()
                                is Resource.Success -> a.data?.let { b -> onSuccess(b) }
                                else -> onFailed(a.message)
                            }
                        }
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean = false
                })
            }
        }
    }

    override fun onSuccess(users: List<User>) {
        userAdapter.setAllUser(users)
        binding.includeMainSearch.apply {
            listUserRecyclerView.visibility = visible
            messageTextView.visibility = gone
            progressBar.visibility = gone
        }
    }

    override fun onLoading() {
        binding.includeMainSearch.apply {
            progressBar.visibility = visible
            messageTextView.visibility = gone
            listUserRecyclerView.visibility = gone
        }
    }

    override fun onFailed(message: String?) {
        binding.includeMainSearch.apply {
            if (message.isNullOrEmpty()) {
                messageTextView.apply {
                    text = resources.getString(R.string.user_not_found)
                    visibility = visible
                }
            }
            messageTextView.apply {
                text = message
                visibility = visible
            }
            progressBar.visibility = gone
            listUserRecyclerView.visibility = gone
        }
    }
}
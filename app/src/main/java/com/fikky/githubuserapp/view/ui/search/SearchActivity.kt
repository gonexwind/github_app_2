package com.fikky.githubuserapp.view.ui.search

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
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
    private lateinit var userAdapter: UserAdapter
    private val viewModel by viewModels<SearchViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userAdapter = UserAdapter()
        binding.includeMainSearch.listUserRecyclerView.apply {
            adapter = userAdapter
            layoutManager =
                LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.searchAppBar).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.searchUser(query).observe(this@SearchActivity) { a ->
                    when (a) {
                        is Resource.Loading -> onLoading()
                        is Resource.Success -> a.data?.let { b -> onSuccess(b) }
                        else -> onFailed(a.message)
                    }
                }
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(query: String): Boolean = false
        })
        return true
    }

    override fun onSuccess(data: List<User>) {
        userAdapter.setAllUser(data)
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
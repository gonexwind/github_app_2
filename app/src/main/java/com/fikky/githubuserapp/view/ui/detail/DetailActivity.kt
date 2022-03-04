package com.fikky.githubuserapp.view.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.fikky.githubuserapp.databinding.ActivityDetailBinding
import com.fikky.githubuserapp.service.model.User
import com.fikky.githubuserapp.service.util.Constant.EXTRA_USER
import com.fikky.githubuserapp.service.util.Constant.TAB_TITLES
import com.fikky.githubuserapp.service.util.Resource
import com.fikky.githubuserapp.service.util.StateCallback
import com.fikky.githubuserapp.view.adapter.DetailAdapter
import com.fikky.githubuserapp.viewmodel.DetailViewModel
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity(), StateCallback<User?> {
    private lateinit var binding: ActivityDetailBinding
    private val viewModel by viewModels<DetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra(EXTRA_USER)
        val detailAdapter = DetailAdapter(this, username.toString())

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            elevation = 0f
        }

        viewModel.getDetailUser(username!!).observe(this) {
            when (it) {
                is Resource.Error -> onFailed(it.message)
                is Resource.Loading -> onLoading()
                is Resource.Success -> onSuccess(it.data)
            }
        }

        binding.apply {
            viewPager.adapter = detailAdapter
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onSuccess(data: User?) {
        binding.apply {
            supportActionBar?.title = data?.username
            Glide.with(this@DetailActivity).load(data?.avatar).into(avatarImageView)
            nameTextView.text = data?.name
            usernameTextView.text = data?.username
            companyTextView.text = data?.company
            locationTextView.text = data?.location
            totalRepositoryTextView.text = data?.repository.toString()
            totalFollowersTextView.text = data?.follower.toString()
            totalFollowingTextView.text = data?.following.toString()
        }
    }

    override fun onLoading() {}
    override fun onFailed(message: String?) {}
}
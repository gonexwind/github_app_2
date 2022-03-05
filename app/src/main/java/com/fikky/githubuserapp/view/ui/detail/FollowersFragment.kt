package com.fikky.githubuserapp.view.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fikky.githubuserapp.R
import com.fikky.githubuserapp.databinding.FragmentFollowersBinding
import com.fikky.githubuserapp.service.model.User
import com.fikky.githubuserapp.service.util.Constant.EXTRA_USER
import com.fikky.githubuserapp.service.util.Resource
import com.fikky.githubuserapp.service.util.StateCallback
import com.fikky.githubuserapp.view.adapter.UserAdapter
import com.fikky.githubuserapp.viewmodel.DetailViewModel

class FollowersFragment : Fragment(), StateCallback<List<User>> {
    private var _binding: FragmentFollowersBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: DetailViewModel
    private lateinit var userAdapter: UserAdapter
    private var username: String? = null

    companion object {
        fun getInstance(username: String): Fragment = FollowersFragment().apply {
            arguments = Bundle().apply { putString(EXTRA_USER, username) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { username = it.getString(EXTRA_USER) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        userAdapter = UserAdapter()
        binding.listFollowersRecyclerView.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        viewModel.getUserFollowers(username.toString()).observe(viewLifecycleOwner) { a ->
            when (a) {
                is Resource.Error -> onFailed(a.message)
                is Resource.Loading -> onLoading()
                is Resource.Success -> a.data?.let { b -> onSuccess(b) }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onSuccess(data: List<User>) {
        userAdapter.setAllUser(data)
        binding.apply {
            listFollowersRecyclerView.visibility = visible
            messageTextView.visibility = gone
            progressBar.visibility = gone
        }
    }

    override fun onLoading() {
        binding.apply {
            listFollowersRecyclerView.visibility = gone
            messageTextView.visibility = gone
            progressBar.visibility = visible
        }
    }

    override fun onFailed(message: String?) {
        binding.apply {
            if (message == null) {
                messageTextView.text = resources.getString(R.string.followers_not_found)
                messageTextView.visibility = visible
            }
            messageTextView.text = message
            messageTextView.visibility = visible
            listFollowersRecyclerView.visibility = gone
            progressBar.visibility = gone
        }
    }
}
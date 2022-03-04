package com.fikky.githubuserapp.view.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fikky.githubuserapp.databinding.FragmentFollowersBinding
import com.fikky.githubuserapp.service.model.User
import com.fikky.githubuserapp.service.util.StateCallback

class FollowersFragment : Fragment(), StateCallback<List<User>> {
    private var _binding: FragmentFollowersBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val EXTRA_USERNAME = "USERNAME"

        fun getInstance(username: String): Fragment = FollowingFragment().apply {
            arguments = Bundle().apply { putString(EXTRA_USERNAME, username) }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFollowersBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onSuccess(users: List<User>) {
        TODO("Not yet implemented")
    }

    override fun onLoading() {
        TODO("Not yet implemented")
    }

    override fun onFailed(message: String?) {
        TODO("Not yet implemented")
    }
}
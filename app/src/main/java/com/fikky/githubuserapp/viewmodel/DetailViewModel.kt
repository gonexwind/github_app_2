package com.fikky.githubuserapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fikky.githubuserapp.service.model.User
import com.fikky.githubuserapp.service.repository.RetrofitService
import com.fikky.githubuserapp.service.util.Resource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel : ViewModel() {
    private val retrofit = RetrofitService.create()
    private val user = MutableLiveData<Resource<User>>()
    private val listUserFollowers = MutableLiveData<Resource<List<User>>>()
    private val listUserFollowing = MutableLiveData<Resource<List<User>>>()

    fun getDetailUser(username: String): LiveData<Resource<User>> {
        retrofit.getDetailUser(username).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                val result = response.body()
                user.postValue(Resource.Success(result))
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                user.postValue(Resource.Error(t.message))
            }
        })
        return user
    }

    fun getUserFollowers(username: String): LiveData<Resource<List<User>>> {
        listUserFollowers.postValue(Resource.Loading())
        retrofit.getUserFollowers(username).enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                val list = response.body()
                if (list.isNullOrEmpty()) {
                    listUserFollowers.postValue(Resource.Error(null))
                } else {
                    listUserFollowers.postValue(Resource.Success(list))
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                listUserFollowers.postValue(Resource.Error(t.message))
            }
        })
        return listUserFollowers
    }

    fun getUserFollowing(username: String): LiveData<Resource<List<User>>> {
        listUserFollowing.postValue(Resource.Loading())
        retrofit.getUserFollowing(username).enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                val list = response.body()
                if (list.isNullOrEmpty()) {
                    listUserFollowing.postValue(Resource.Error(null))
                } else {
                    listUserFollowing.postValue(Resource.Success(list))
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                listUserFollowing.postValue(Resource.Error(t.message))
            }
        })
        return listUserFollowing
    }

}
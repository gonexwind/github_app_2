package com.fikky.githubuserapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fikky.githubuserapp.service.model.SearchResponse
import com.fikky.githubuserapp.service.model.User
import com.fikky.githubuserapp.service.repository.RetrofitService
import com.fikky.githubuserapp.service.util.Resource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel : ViewModel() {
    private val retrofit = RetrofitService.create()
    private val listUser = MutableLiveData<Resource<List<User>>>()

    fun searchUser(query: String): LiveData<Resource<List<User>>> {
        listUser.postValue(Resource.Loading())
        retrofit.searchUsers(query).enqueue(object : Callback<SearchResponse> {
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                val list = response.body()?.items
                if (list.isNullOrEmpty()) {
                    listUser.postValue(Resource.Error(null))
                } else {
                    listUser.postValue(Resource.Success(data = list))
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                listUser.postValue(Resource.Error(t.message))
            }
        })
        return listUser
    }
}

package com.fikky.githubuserapp.service.util

import android.view.View

interface StateCallback<T> {
    fun onSuccess(users: T)

    fun onLoading()

    fun onFailed(message: String?)

    val visible: Int
        get() = View.VISIBLE

    val gone: Int
        get() = View.GONE
}
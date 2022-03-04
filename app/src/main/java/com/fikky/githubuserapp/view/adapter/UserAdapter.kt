package com.fikky.githubuserapp.view.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fikky.githubuserapp.databinding.ListUserItemBinding
import com.fikky.githubuserapp.service.model.User
import com.fikky.githubuserapp.service.util.Constant.EXTRA_USER
import com.fikky.githubuserapp.view.ui.detail.DetailActivity

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    private val listUser = ArrayList<User>()

    inner class UserViewHolder(private val view: ListUserItemBinding) :
        RecyclerView.ViewHolder(view.root) {
        fun bind(user: User) {
            with(view) {
                usernameTextView.text = user.username
                Glide.with(itemView.context).load(user.avatar).into(avatarImageView)
            }
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra(EXTRA_USER, user.username)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = ListUserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(listUser[position])
    }

    override fun getItemCount(): Int = listUser.size

    fun setAllUser(user: List<User>) {
        listUser.apply {
            clear()
            addAll(user)
        }
        notifyDataSetChanged()
    }
}
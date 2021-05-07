package com.utn.ddam.clase02.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.utn.ddam.clase02.Entities.User
import com.utn.ddam.clase02.R
import kotlinx.android.synthetic.main.user_item.view.*

class UsersAdapter(val users : List<User>) : RecyclerView.Adapter<UsersAdapter.UserHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return UserHolder(layoutInflater.inflate(R.layout.user_item, parent, false))
    }

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        holder.render(users[position])
    }

    override fun getItemCount(): Int = users.size

    class UserHolder(val view : View) : RecyclerView.ViewHolder(view) {
        fun render(user: User) {
            view.tvUsername.text = user.username;
            view.tvName.text = user.name + " " + user.lastname;
            view.tvEmail.text = user.email;
        }
    }
}
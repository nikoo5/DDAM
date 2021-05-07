package com.utn.ddam.clase03.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.utn.ddam.clase03.R
import com.utn.ddam.clase03.activities.HomeActivityArgs
import com.utn.ddam.clase03.adapters.UsersAdapter
import com.utn.ddam.clase03.entities.User

class ListUserFragment : Fragment() {
    lateinit var v : View
    var lstUsers : MutableList<User> = mutableListOf();
    lateinit var rvUsers : RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_list_user, container, false)

        rvUsers = v.findViewById(R.id.rvUsers);
        lstUsers.add(User("nfilippi", "1234", "nfilippi@hotmail.com", "Nicolás", "Filippi Farmar"))
        lstUsers.add(User("user1", "pass", "user1@mail.com", "Nombre", "Apellido 1"))
        lstUsers.add(User("user2", "pass", "user2@mail.com", "Nombre", "Apellido 2"))
        lstUsers.add(User("user3", "pass", "user3@mail.com", "Nombre", "Apellido 3"))
        lstUsers.add(User("user4", "pass", "user4@mail.com", "Nombre", "Apellido 4"))
        lstUsers.add(User("user5", "pass", "user5@mail.com", "Nombre", "Apellido 5"))
        lstUsers.add(User("user6", "pass", "user6@mail.com", "Nombre", "Apellido 6"))
        lstUsers.add(User("user7", "pass", "user7@mail.com", "Nombre", "Apellido 7"))
        lstUsers.add(User("user8", "pass", "user8@mail.com", "Nombre", "Apellido 8"))
        lstUsers.add(User("user9", "pass", "user9@mail.com", "Nombre", "Apellido 9"))
        lstUsers.add(User("user10", "pass", "user10@mail.com", "Nombre", "Apellido 10"))
        lstUsers.add(User("user11", "pass", "user11@mail.com", "Nombre", "Apellido 11"))
        lstUsers.add(User("user12", "pass", "user12@mail.com", "Nombre", "Apellido 12"))
        lstUsers.add(User("user13", "pass", "user13@mail.com", "Nombre", "Apellido 13"))
        lstUsers.add(User("user14", "pass", "user14@mail.com", "Nombre", "Apellido 14"))
        lstUsers.add(User("user15", "pass", "user15@mail.com", "Nombre", "Apellido 15"))
        lstUsers.add(User("user16", "pass", "user16@mail.com", "Nombre", "Apellido 16"))
        lstUsers.add(User("user17", "pass", "user17@mail.com", "Nombre", "Apellido 17"))
        lstUsers.add(User("user18", "pass", "user18@mail.com", "Nombre", "Apellido 18"))
        lstUsers.add(User("user19", "pass", "user19@mail.com", "Nombre", "Apellido 19"))
        lstUsers.add(User("user20", "pass", "user20@mail.com", "Nombre", "Apellido 20"))
        lstUsers.add(User("user21", "pass", "user21@mail.com", "Nombre", "Apellido 21"))
        lstUsers.add(User("user22", "pass", "user22@mail.com", "Nombre", "Apellido 22"))
        lstUsers.add(User("user23", "pass", "user23@mail.com", "Nombre", "Apellido 23"))
        lstUsers.add(User("user24", "pass", "user24@mail.com", "Nombre", "Apellido 24"))
        lstUsers.add(User("user25", "pass", "user25@mail.com", "Nombre", "Apellido 25"))

        initRecycler();

        return v;
    }

    fun initRecycler() {
        rvUsers.layoutManager = LinearLayoutManager(this.context);
        val adapter = UsersAdapter(lstUsers)
        rvUsers.adapter = adapter;
    }
}
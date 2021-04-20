package com.utn.ddam.clase02.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.utn.ddam.clase02.Adapters.UsersAdapter
import com.utn.ddam.clase02.Entities.User
import com.utn.ddam.clase02.R
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {
    lateinit var homeLayout : ConstraintLayout
    lateinit var v : View

    lateinit var user : User
    lateinit var users : Array<User>
    lateinit var rvUsers : RecyclerView
//    lateinit var layoutManager : RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_home, container, false)

        homeLayout = v.findViewById(R.id.homeLayout);
        rvUsers = v.findViewById(R.id.rvUsers);

        user = HomeFragmentArgs.fromBundle(requireArguments()).user;
        users = HomeFragmentArgs.fromBundle(requireArguments()).users;

        initRecycler();

        return v;
    }

    fun initRecycler() {
        rvUsers.layoutManager = LinearLayoutManager(this.context);
        val adapter = UsersAdapter(users.toList())
        rvUsers.adapter = adapter;
    }
}
package com.utn.ddam.clase03.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController
import com.utn.ddam.clase03.R
import com.utn.ddam.clase03.activities.HomeActivityArgs
import com.utn.ddam.clase03.entities.User


class HomeFragment : Fragment() {
    lateinit var v : View
    lateinit var btnViewUsers : Button
    lateinit var user : User
    lateinit var users : Array<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_home, container, false)
        btnViewUsers = v.findViewById(R.id.btnViewUsers)

//        user = HomeActivityArgs.fromBundle(requireArguments()).user;
//        users = HomeActivityArgs.fromBundle(requireArguments()).users;

        return v;
    }

    override fun onStart() {
        super.onStart()

        btnViewUsers.setOnClickListener {
            val action = HomeFragmentDirections.toListUsers();
            v.findNavController().navigate(action);
        }
    }
}
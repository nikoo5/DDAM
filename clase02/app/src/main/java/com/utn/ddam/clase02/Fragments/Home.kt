package com.utn.ddam.clase02.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar
import com.utn.ddam.clase02.R

/**
 * A simple [Fragment] subclass.
 * Use the [Home.newInstance] factory method to
 * create an instance of this fragment.
 */
class Home : Fragment() {
    lateinit var homeLayout : ConstraintLayout
    lateinit var v : View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_home, container, false)
        homeLayout = v.findViewById(R.id.homeLayout);
        return v;
    }

    override fun onStart() {
        super.onStart()
        val user = HomeArgs.fromBundle(requireArguments()).user

        Snackbar.make(homeLayout, user, Snackbar.LENGTH_SHORT).show();
    }
}
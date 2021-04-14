package com.utn.ddam.clase02.Fragments

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.utn.ddam.clase02.Entities.User
import com.utn.ddam.clase02.R
import kotlinx.android.synthetic.main.fragment_login.*


/**
 * A simple [Fragment] subclass.
 * Use the [Login.newInstance] factory method to
 * create an instance of this fragment.
 */
class Login : Fragment() {
    lateinit var v : View;
    lateinit var loginLayout : ConstraintLayout
    lateinit var btnLogin: Button
    lateinit var txtUsername: EditText
    lateinit var txtPassword: EditText

    var lstUsers : MutableList<User> = mutableListOf();

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v =  inflater.inflate(R.layout.fragment_login, container, false)

        loginLayout = v.findViewById(R.id.loginLayout)
        btnLogin = v.findViewById(R.id.btnLogin);
        txtUsername = v.findViewById(R.id.txtUsername);
        txtPassword = v.findViewById(R.id.txtPassword);

        lstUsers.add(
            User(
                "nfilippi",
                "1234",
                "nico.filippi@hotmail.com",
                "Nicolás",
                "Filippi Farmar"
            )
        )
        lstUsers.add(
            User(
                "abreitman",
                "1122",
                "alejandro.breitman@hotmail.com",
                "Alejandro",
                "Breitman"
            )
        )
        lstUsers.add(User("jarrieta", "4321", "jarrieta@hotmail.com", "Javier", "Arrieta"))
        lstUsers.add(User("jperez", "7890", "jperez@hotmail.com", "Josefina", "Pérez"))

        return v;
    }

    override fun onStart() {
        super.onStart()

        btnLogin.setOnClickListener {
            val username = txtUsername.text.trim().toString()
            val password = txtPassword.text.trim().toString()

            if(username.isNotEmpty() && password.isNotEmpty()) {
                var user : User? = lstUsers.find { (username.indexOf('@') >= 0 && it.email == username) || (username.indexOf(
                    '@'
                ) == -1 && it.username == username) }

                if(user != null) {
                    if(user.password == password) {
                        val action = LoginDirections.loginToHome(user.Greetings());
                        v.findNavController().navigate(action);
                    } else {
                        Snackbar.make(loginLayout, "Contraseña Incorrecta", Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    Snackbar.make(loginLayout, "Usuario / Email Incorrecto", Snackbar.LENGTH_SHORT).show();
                }
            } else {
                Snackbar.make(
                    loginLayout,
                    "Debe ingresar el usuario y contraseña",
                    Snackbar.LENGTH_SHORT
                ).show();
            }
        }
    }
}
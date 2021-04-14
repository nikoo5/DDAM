package com.utn.ddam.clase02.Entities

class User(username: String, password: String, email: String, name: String, lastname: String) {
    var username: String
    var password: String
    var email: String
    var name: String
    var lastname: String

    init {
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.lastname = lastname;
    }

    fun Greetings() : String {
        return "Bienvenido " + name + " " + lastname;
    }
}
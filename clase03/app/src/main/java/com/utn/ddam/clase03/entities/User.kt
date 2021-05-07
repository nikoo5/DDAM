package com.utn.ddam.clase03.entities

import android.os.Parcel
import android.os.Parcelable

class User(username: String, password: String, email: String, name: String, lastname: String, image : String = "") : Parcelable {
    var username: String
    var password: String
    var email: String
    var name: String
    var lastname: String
    var image : String

    init {
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.lastname = lastname;
        this.image = image;
    }

    constructor(source: Parcel) : this(
        source.readString()!!,
        source.readString()!!,
        source.readString()!!,
        source.readString()!!,
        source.readString()!!,
        source.readString()!!
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(username)
        writeString(password)
        writeString(email)
        writeString(name)
        writeString(lastname)
        writeString(image)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<User> = object : Parcelable.Creator<User> {
            override fun createFromParcel(source: Parcel): User = User(source)
            override fun newArray(size: Int): Array<User?> = arrayOfNulls(size)
        }
    }

    fun Greetings() : String {
        return "Bienvenido " + name + " " + lastname;
    }
}
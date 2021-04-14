package com.utn.ddam.clase02

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.utn.ddam.clase02.Entities.Mascota

class MainActivity : AppCompatActivity() {
    lateinit var miMascota : Mascota
    var lstMascotas : MutableList<Mascota> = mutableListOf();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        miMascota = Mascota(nombre = "Astor", tipo = Mascota.Tipos.typePerro, raza = "Boxer", edad = 4);
//
//        lstMascotas.add((Mascota("Raul", Mascota.Tipos.typeGato, "Blanco", 2)));
//        lstMascotas.add(miMascota);
//
//        lstMascotas.forEach {
//            Log.d("TEST", it.toString())
//        }
    }
}
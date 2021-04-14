package com.utn.ddam.clase02.Entities

class Mascota (nombre: String, tipo: String, raza: String, edad: Int)
{
    var nombre : String;
    var tipo : String;
    var raza : String;
    var edad : Int;

    init {
        this.nombre = nombre;
        this.tipo = tipo;
        this.raza = raza;
        this.edad = edad;
    }

    fun CalcularEdad (edad: Int) : Int {
        var nuevaEdad : Int;
        nuevaEdad = edad * 7;
        return nuevaEdad;
    }

    override fun toString(): String {
        return "Mascota(nombre='$nombre', tipo='$tipo', raza='$raza', edad=$edad)"
    }

    class Tipos {
        companion object {
            val typeGato = "GATO";
            val typePerro = "PERRO";
        }
    }


}
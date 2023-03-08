package com.example.petdaycarekotandfire

import java.io.Serializable

data class Pet(var id : String, var nombre: String, var raza: String, var genero: Genero, var constitucion: Float) : Serializable{

    enum class Genero{
        Macho,Hembra,Otro
    }
}

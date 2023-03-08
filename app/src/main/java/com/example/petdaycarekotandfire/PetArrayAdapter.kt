package com.example.petdaycarekotandfire

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PetArrayAdapter (contexto: Context, vista: Int, private val listaMascotas: ArrayList<Pet>): ArrayAdapter<Pet> (contexto,vista,listaMascotas){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val inflater = LayoutInflater.from(context)
        val currentListItem = inflater.inflate(R.layout.item_pet_list,null)
        val textoNombreMascota = currentListItem.findViewById<TextView>(R.id.textoNombreMascota)
        val textoRazaMascota = currentListItem.findViewById<TextView>(R.id.textoRazaMascota)
        val iconoEdit = currentListItem.findViewById<ImageView>(R.id.iconoEdit)
        val iconoDelete = currentListItem.findViewById<ImageView>(R.id.iconoDelete)

        textoNombreMascota.text = listaMascotas.get(position).nombre
        textoRazaMascota.text = listaMascotas.get(position).raza
        iconoEdit.setImageResource(R.drawable.edit)
        iconoDelete.setImageResource(R.drawable.garbage)

        return  currentListItem
    }

}
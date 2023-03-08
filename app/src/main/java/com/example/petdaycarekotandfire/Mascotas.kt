package com.example.petdaycarekotandfire

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.children
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import android.view.MenuItem as MenuItem1

class Mascotas : AppCompatActivity() {

    lateinit var mascotaActual : Pet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mascotas)

        volcarListaMascotasListView()

        val botonHuella = findViewById<ImageView>(R.id.imagenBoton)
        botonHuella.setOnClickListener {
            val i = Intent(applicationContext,AltaMascota::class.java)
            startActivity(i)
        }

    }

    fun volcarListaMascotasListView() {
        var listaMascotas = arrayListOf<Pet>()
        val db = Firebase.firestore
        db.collection("mascotas")
            .get()
            .addOnSuccessListener { result ->
                for (mascota in result) {
                    var mascotaTemp = Pet(
                            mascota.id,
                            mascota.get("nombre").toString(),
                            mascota.get("raza").toString(),
                            Pet.Genero.valueOf(mascota.get("genero").toString()),
                            mascota.get("constitucion").toString().toFloat()
                    )
                    listaMascotas.add(mascotaTemp)
                }
                if(listaMascotas.size == 0){
                    var textoListaVacia = findViewById<TextView>(R.id.textoListaVacia)
                    textoListaVacia.visibility = View.VISIBLE
                }else{
                    val petArrayAdapter = PetArrayAdapter(applicationContext, R.layout.item_pet_list, listaMascotas)
                    val listViewMascotas = findViewById<ListView>(R.id.listViewMascotas)
                    listViewMascotas.adapter = petArrayAdapter
                    listViewMascotas.setOnItemClickListener {parent, view, position, id ->
                        mascotaActual = listaMascotas.get(position)
                        val i= Intent(applicationContext,EditarMascota::class.java)
                        i.putExtra("mascota",mascotaActual)
                        startActivity(i)
                    }
                }
            }
            .addOnFailureListener { exception ->

            }
    }

    fun logout(){
        FirebaseAuth.getInstance().signOut()
        val i = Intent(applicationContext,MainActivity::class.java)
        startActivity(i)
    }

    fun borrarUsuario(){
        val user = Firebase.auth.currentUser!!

        user.delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(applicationContext, "La cuenta se ha eliminado correctamente", Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun mostrarConfirmacionBorrarUsuario(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("¿Seguro que quiere eliminar la cuenta de usuario?")
        builder.setMessage("Si está seguro pulse Aceptar, en caso contrario pulse Cancelar")
        builder.setPositiveButton("Aceptar", DialogInterface.OnClickListener { dialogInterface, i ->
            borrarUsuario()
            val i = Intent(applicationContext,MainActivity::class.java)
            startActivity(i)
        })
        builder.setNegativeButton("Cancelar", DialogInterface.OnClickListener { dialogInterface, i ->
            val alertDialog = builder.create()
            alertDialog.cancel()
        })
        val alertDialog = builder.create()
        alertDialog.show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem1): Boolean {
        when(item.itemId){
            R.id.menuLogOut -> logout()
            R.id.menuBorrarCuenta -> mostrarConfirmacionBorrarUsuario()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
       logout()
    }

}
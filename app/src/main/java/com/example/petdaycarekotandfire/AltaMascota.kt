package com.example.petdaycarekotandfire

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AltaMascota : AppCompatActivity() {

    lateinit var editTextNombre : EditText
    lateinit var editTextRaza : EditText
    lateinit var editTextPeso: EditText
    lateinit var spinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alta_mascota)

        editTextNombre = findViewById(R.id.editTextNombre)
        editTextRaza = findViewById(R.id.editTextRaza)
        editTextPeso = findViewById(R.id.editTextTextPeso)
        spinner = findViewById(R.id.spinnerGenero)
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, Pet.Genero.values())
        spinner.adapter = arrayAdapter

        val botonAlta = findViewById<Button>(R.id.botonNuevo)
        botonAlta.setOnClickListener {
            nuevaMascota()
        }
    }

    fun nuevaMascota(){
        val db = Firebase.firestore
        if(comprobarCampos()){
            val mascota = hashMapOf(
                "nombre" to editTextNombre.text.toString(),
                "raza" to editTextRaza.text.toString(),
                "genero" to spinner.selectedItem.toString(),
                "constitucion" to editTextPeso.text.toString()
            )
            db.collection("mascotas")
                .add(mascota)
                .addOnSuccessListener { documentReference ->
                    Toast.makeText(applicationContext, "La mascota ${editTextNombre.text} se ha dado de alta", Toast.LENGTH_LONG).show()
                    val i = Intent(applicationContext,Mascotas::class.java)
                    startActivity(i)
                }
                .addOnFailureListener { e ->
                    Toast.makeText(applicationContext, "Error durante el proceso", Toast.LENGTH_LONG).show()
                }
        }else{
            Toast.makeText(applicationContext, "Rellene todos los campos", Toast.LENGTH_SHORT).show()
        }

    }

    fun comprobarCampos():Boolean{
        var condicion = false
        if(editTextNombre.text.toString().isNotEmpty() && editTextNombre.text.toString().isNotBlank()
            && editTextRaza.text.toString().isNotEmpty() && editTextRaza.text.toString().isNotBlank()
            && editTextPeso.text.toString().isNotEmpty() && editTextPeso.text.toString().isNotBlank()
            && spinner.selectedItem.toString().isNotEmpty() && spinner.selectedItem.toString().isNotBlank()){
            condicion = true
        }
        return condicion
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menuLogOut -> logout()
            R.id.menuBorrarCuenta -> mostrarConfirmacionBorrarUsuario()
        }
        return super.onOptionsItemSelected(item)
    }

}
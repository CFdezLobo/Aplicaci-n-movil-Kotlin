package com.example.petdaycarekotandfire

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class EditarMascota : AppCompatActivity() {

    lateinit var db : FirebaseFirestore
    lateinit var editTextNombre : EditText
    lateinit var editTextRaza : EditText
    lateinit var editTextConstitucion : EditText
    lateinit var spinner : Spinner
    lateinit var mascota : Pet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_mascota)

        db = Firebase.firestore
        editTextNombre = findViewById(R.id.editTextNombre)
        editTextRaza = findViewById(R.id.editTextRaza)
        editTextConstitucion = findViewById(R.id.editTextPeso)
        spinner = findViewById(R.id.spinnerGenero)
        mascota = intent.getSerializableExtra("mascota") as Pet

        rellenarCamposMascota()

        val botonGuardar= findViewById<Button>(R.id.botonGuardar)
        botonGuardar.setOnClickListener {
            modificarMascota()
        }

        val botonBorrar = findViewById<Button>(R.id.botonBorrar)
        botonBorrar.setOnClickListener {
            mostrarConfirmacionBorrarMascota()
        }
    }

    fun rellenarCamposMascota() {
        editTextNombre.setText(mascota.nombre)
        editTextRaza.setText(mascota.raza)
        editTextConstitucion.setText(mascota.constitucion.toString())
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, Pet.Genero.values())
        spinner.adapter = arrayAdapter
        spinner.setSelection(mascota.genero.ordinal)
    }

    fun modificarMascota(){
        db.collection("mascotas").document(mascota.id).update(mapOf(
            "nombre" to editTextNombre.text.toString(),
            "raza" to editTextRaza.text.toString(),
            "genero" to spinner.selectedItem.toString(),
            "constitucion" to editTextConstitucion.text.toString()
        ))
            .addOnSuccessListener {
                Toast.makeText(applicationContext, "Mascota editada correctamente", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(applicationContext, "Error al editar la mascota", Toast.LENGTH_SHORT).show()
            }
    }

    fun borrarMascota(){
        db.collection("mascotas").document(mascota.id)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(applicationContext, "Mascota eliminada correctamente", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(applicationContext, "Error al eliminar la mascota", Toast.LENGTH_LONG).show()
            }
    }

    private fun mostrarConfirmacionBorrarMascota(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("¿Seguro que quiere eliminar la mascota?")
        builder.setMessage("Si está seguro pulse Aceptar, en caso contrario pulse Cancelar")
        builder.setPositiveButton("Aceptar", DialogInterface.OnClickListener { dialogInterface, i ->
            borrarMascota()
            val i = Intent(applicationContext,Mascotas::class.java)
            startActivity(i)
        })
        builder.setNegativeButton("Cancelar", DialogInterface.OnClickListener { dialogInterface, i ->
            val alertDialog = builder.create()
            alertDialog.cancel()
        })
        val alertDialog = builder.create()
        alertDialog.show()
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
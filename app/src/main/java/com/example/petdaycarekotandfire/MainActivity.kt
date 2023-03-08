package com.example.petdaycarekotandfire

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.regex.Pattern

class MainActivity : AppCompatActivity() {

    lateinit var editTextMail : EditText
    lateinit var editTextPass : EditText
    lateinit var passwordLayout : TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextMail = findViewById(R.id.editTextEmail)
        editTextPass = findViewById(R.id.editTextPassword)
        passwordLayout = findViewById(R.id.textFieldPassword)

        val botonRegistrar = findViewById<Button>(R.id.botonRegistrar)
        botonRegistrar.setOnClickListener {
            registrar()
        }

        val botonAcceder = findViewById<Button>(R.id.botonAcceder)
        botonAcceder.setOnClickListener {
            if(comprobarCamposEditText()){
                login()
            }else{
                Toast.makeText(applicationContext, "Rellene todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun registrar(){
        if(comprobarCamposEditText() && comprobarMail()){
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(editTextMail.text.toString(),editTextPass.text.toString()).addOnCompleteListener {
                if(it.isSuccessful){
                    mostrarAvisoExitoRegistro()
                }else{
                    mostrarAvisoErrorRegistro()
                }
            }
        }else{
            Toast.makeText(applicationContext, "Rellene todos los campos correctamente", Toast.LENGTH_SHORT).show()
        }
    }

    fun login(){
        if(comprobarCamposEditText() && comprobarMail()){
            FirebaseAuth.getInstance().signInWithEmailAndPassword(editTextMail.text.toString(),editTextPass.text.toString()).addOnCompleteListener {
                if(it.isSuccessful){
                    val i = Intent(applicationContext,Mascotas::class.java)
                    startActivity(i)
                    Toast.makeText(this, "Bienvenido ${editTextMail.text}", Toast.LENGTH_SHORT).show()
                }else{
                    mostrarAvisoErrorLogin()
                }
            }
        }else{
            Toast.makeText(applicationContext, "Rellene todos los campos correctamente", Toast.LENGTH_SHORT).show()
        }
    }

    fun comprobarCamposEditText() =
        editTextMail.text.isNotEmpty() && editTextPass.text.isNotEmpty() && editTextMail.text.isNotBlank() && editTextPass.text.isNotBlank()

    fun comprobarMail() : Boolean{
        val EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
        )
        return EMAIL_ADDRESS_PATTERN.matcher(editTextMail.text).matches()
    }

    fun logout(){
        FirebaseAuth.getInstance().signOut()
        val i = Intent(applicationContext,MainActivity::class.java)
        startActivity(i)
    }

    private fun mostrarAvisoErrorRegistro(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("No se ha podido registrar el usuario")
        builder.setPositiveButton("Aceptar", null)
        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun mostrarAvisoExitoRegistro(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Bienvenido")
        builder.setMessage("La cuenta se ha creado correctamente")
        builder.setPositiveButton("Aceptar", null)
        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun mostrarAvisoErrorLogin(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("No se ha podido realizar el login del usuario")
        builder.setPositiveButton("Aceptar", null)
        val alertDialog = builder.create()
        alertDialog.show()
    }

    override fun onBackPressed() {
        logout()
    }

}
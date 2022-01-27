package com.chema.siemprellegastarde.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.chema.siemprellegastarde.ProviderType
import com.chema.siemprellegastarde.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignUpActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()


    private  lateinit var btn_signUp: Button
    private  lateinit var ed_txt_userName: EditText
    private  lateinit var ed_txt_email: EditText
    private  lateinit var ed_txt_pwd: EditText
    private  lateinit var ed_txt_movil: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        btn_signUp = findViewById(R.id.btRegistrarSignUp)
        ed_txt_userName = findViewById(R.id.ed_txt_userName_signUp)
        ed_txt_email = findViewById(R.id.edEmailSignUp)
        ed_txt_pwd = findViewById(R.id.edPassSignUp)
        ed_txt_movil = findViewById(R.id.edMovil)

        btn_signUp.setOnClickListener{
            check_signUp()
        }

    }

    /*
    Comprueba que se pueda registrar correctamente al usuario
     */
    private fun check_signUp(){

        if(check_campos_vacios()){

            if(check_movil(ed_txt_movil.text.toString().trim())){
                check_firebase_auth()
            }else{
                Toast.makeText(this,getString(R.string.movilIncorrecto), Toast.LENGTH_SHORT).show()
            }

        }else{
            Toast.makeText(this,getString(R.string.camposVacios), Toast.LENGTH_SHORT).show()
        }
    }

    /*
    * Comprueba si alguno de los campos esta vacio
    * Devuelve true si todos los campos esta rellenos
     */
    private fun check_campos_vacios():Boolean{
        var correcto = true

        if(ed_txt_email.text.toString().trim().isEmpty()){
            correcto = false
        }

        if(ed_txt_userName.text.toString().trim().isEmpty()){
            correcto = false
        }
        if(ed_txt_pwd.text.toString().trim().isEmpty()){
            correcto = false
        }
        if(ed_txt_movil.text.toString().trim().isEmpty()){
            correcto = false
        }
        return correcto
    }

    /*
    * Comprueba si el numero de telefono es correcto
     */
    private fun check_movil(target: CharSequence?): Boolean {
        return if (target == null) {
            false
        } else {
            if (target.length < 6 || target.length > 13) {
                false
            } else {
                Patterns.PHONE.matcher(target).matches()
            }
        }
    }

    /*
    Autenticacion Firebase
     */
    private fun check_firebase_auth(){
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(ed_txt_email.text.toString(),ed_txt_pwd.text.toString()).addOnCompleteListener {
            if (it.isSuccessful){
                reg_user() //guardamos el usuario
                irHome(it.result?.user?.email?:"") //vamos a home
            } else {
                showAlert()
            }
        }
    }

    /*
    Registrar un usuario en FireStore
     */
    private fun reg_user(){
        val email = ed_txt_email.text.toString()


        //Se guardarán en modo HashMap (clave, valor).
        var user = hashMapOf(
            "provider" to ProviderType.BASIC,
            "userName" to ed_txt_userName.text.toString().trim(),
            "email" to email,
            "phone" to ed_txt_movil.text.toString().trim()
        )

        db.collection("users")
            .document(email) //Será la clave del documento.
            .set(user).addOnSuccessListener {
                Toast.makeText(this, "Almacenado", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener{
                Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show()
            }


    }

    private fun irHome(email:String){
        val homeIntent = Intent(this, HomeActivity::class.java).apply {
            putExtra("email",email)
        }
        startActivity(homeIntent)
    }

    /*
    ALERT DIALOG DE ERROR
     */
    private fun showAlert(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autenticando al usuairo")
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}
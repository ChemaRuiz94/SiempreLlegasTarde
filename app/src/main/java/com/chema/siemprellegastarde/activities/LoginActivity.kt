package com.chema.siemprellegastarde.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.BoringLayout
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.chema.siemprellegastarde.R
import com.chema.siemprellegastarde.utils.ProviderType
import com.chema.siemprellegastarde.utils.VariblesComunes
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private var RC_SIGN_IN = 1
    //private lateinit var auth: FirebaseAuth

    private lateinit var btn_login : Button
    private lateinit var btn_login_google : Button
    private lateinit var btn_signUp : Button
    private lateinit var txt_email : EditText
    private lateinit var txt_pwd : EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R    .layout.activity_login)

        //auth = FirebaseAuth.getInstance()

        btn_login = findViewById(R.id.btLogin)
        btn_login_google = findViewById(R.id.btGoogle)
        btn_signUp = findViewById(R.id.btRegistrar)
        txt_email = findViewById(R.id.edEmail)
        txt_pwd = findViewById(R.id.edPass)

        btn_signUp.setOnClickListener{
            var intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        btn_login_google.setOnClickListener{
            check_login_google()
        }

        btn_login.setOnClickListener{
            check_login()
        }

    }

    private fun check_login(){
        if(chekc_campos_vacios()){

            FirebaseAuth.getInstance().signInWithEmailAndPassword(edEmail.text.toString(),edPass.text.toString()).addOnCompleteListener {
                if (it.isSuccessful){
                    VariblesComunes.emailUsuarioActual = (it.result?.user?.email?:"")
                    irHome(it.result?.user?.email?:"")  //Esto de los interrogantes es por si está vacío el email.
                } else {
                    showAlert()
                }
            }

        }else{
            Toast.makeText(this,getString(R.string.camposVacios), Toast.LENGTH_SHORT).show()
        }
    }

    private fun check_login_google(){
        val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.request_id_token)) //Esto se encuentra en el archivo google-services.json: client->oauth_client -> client_id
            .requestEmail()
            .build()

        val googleClient = GoogleSignIn.getClient(this,googleConf) //Este será el cliente de autenticación de Google.
        googleClient.signOut() //Con esto salimos de la posible cuenta  de Google que se encuentre logueada.
        val signInIntent = googleClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Si la respuesta de esta activity se corresponde con la inicializada es que viene de la autenticación de Google.
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!

                //Ya tenemos la id de la cuenta. Ahora nos autenticamos con FireBase.
                if (account != null) {
                    val credential: AuthCredential = GoogleAuthProvider.getCredential(account.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
                        if (it.isSuccessful){
                            VariblesComunes.emailUsuarioActual = account.email?:""
                            irHome(account.email?:"")  //Esto de los interrogantes es por si está vacío el email.
                        } else {
                            showAlert()
                        }
                    }
                }
                //firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately

                showAlert()
            }
        }
    }
    private fun chekc_campos_vacios():Boolean{
        var correcto = true

        if(edEmail.text.toString().trim().isEmpty()){
            correcto = false
        }

        if(edPass.text.toString().trim().isEmpty()){
            correcto = false
        }

        return correcto
    }

    /*
    Ir al activity principal
     */
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
        builder.setTitle(getString(R.string.ERROR))
        builder.setMessage(getString(R.string.ocurridoErrorAutenticacion))
        builder.setPositiveButton(getString(R.string.aceptar),null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}
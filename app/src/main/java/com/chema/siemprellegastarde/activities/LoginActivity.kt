package com.chema.siemprellegastarde.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.chema.siemprellegastarde.R

class LoginActivity : AppCompatActivity() {

    private lateinit var btn_login : Button
    private lateinit var btn_login_google : Button
    private lateinit var btn_signUp : Button
    private lateinit var txt_email : EditText
    private lateinit var txt_pwd : EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R    .layout.activity_login)

        btn_login = findViewById(R.id.btLogin)
        btn_login_google = findViewById(R.id.btGoogle)
        btn_signUp = findViewById(R.id.btRegistrar)
        txt_email = findViewById(R.id.edEmail)
        txt_pwd = findViewById(R.id.edPass)


    }
}
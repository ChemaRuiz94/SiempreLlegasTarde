package com.chema.siemprellegastarde.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.chema.siemprellegastarde.R

class SignUpActivity : AppCompatActivity() {

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
    }
}
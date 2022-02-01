package com.chema.siemprellegastarde.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.chema.siemprellegastarde.R

class HomeActivity : AppCompatActivity() {
    private lateinit var btn_edicion : Button
    private lateinit var btn_consulta : Button
    private var email : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        btn_edicion = findViewById(R.id.btn_ventana_edicion)
        btn_consulta = findViewById(R.id.btn_ventana_consulta)

        val bundle:Bundle? = intent.extras
        email = (bundle?.getString("email"))

        btn_edicion.setOnClickListener{
            val editIntent = Intent(this, ListadoEdicionEventosActivity::class.java)
            startActivity(editIntent)
        }

        btn_consulta.setOnClickListener{
            val consulIntent = Intent(this, ListaEventosConsultaActivity::class.java)
            startActivity(consulIntent)
        }
    }
}
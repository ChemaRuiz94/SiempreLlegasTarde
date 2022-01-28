package com.chema.siemprellegastarde.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chema.siemprellegastarde.utils.ProviderType
import com.chema.siemprellegastarde.R
import com.chema.siemprellegastarde.model.User
import com.chema.siemprellegastarde.rv.AdapterRvUsers
import com.chema.siemprellegastarde.utils.Constantes
import com.chema.siemprellegastarde.utils.DatePickerFragment
import com.chema.siemprellegastarde.utils.TimePickerFragment
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_edicionn.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class EdicionnActivity : AppCompatActivity() {

    private val db = Firebase.firestore

    private lateinit var rv : RecyclerView
    var usuarios : ArrayList<User> = ArrayList<User>()
    private lateinit var miAdapter: AdapterRvUsers

    private lateinit var btn_fecha: Button
    private lateinit var ed_txt_fecha: EditText
    private lateinit var btn_hora: Button
    private lateinit var ed_txt_hora: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edicionn)

        btn_fecha = findViewById(R.id.btn_fecha)
        ed_txt_fecha = findViewById(R.id.ed_txt_fecha)
        btn_hora = findViewById(R.id.btn_hora)
        ed_txt_hora = findViewById(R.id.ed_txt_hora)

        runBlocking {
            val job : Job = launch(context = Dispatchers.Default) {
                val datos : QuerySnapshot = getDataFromFireStore() as QuerySnapshot //Obtenermos la colección
                obtenerDatos(datos as QuerySnapshot?)  //'Destripamos' la colección y la metemos en nuestro ArrayList
            }
            //Con este método el hilo principal de onCreate se espera a que la función acabe y devuelva la colección con los datos.
            job.join() //Esperamos a que el método acabe: https://dzone.com/articles/waiting-for-coroutines
        }

        cargarRV()

        btn_fecha.setOnClickListener{
            val newFragment = DatePickerFragment(ed_txt_fecha)
            newFragment.show(supportFragmentManager, "datePicker")
        }

        btn_hora.setOnClickListener{

            val newFragment = TimePickerFragment(ed_txt_hora)
            newFragment.show(supportFragmentManager, "timePicker")
        }
        btn_ubicacion.setOnClickListener{

            val mapIntent = Intent(this, MapsNewEventoActivity::class.java).apply {
                //putExtra("email",email)
            }
            startActivity(mapIntent)
        }
    }

    fun cargarRV(){

        rv = findViewById(R.id.rv_usuarios)
        rv.setHasFixedSize(true)
        rv.layoutManager = LinearLayoutManager(this)
        miAdapter = AdapterRvUsers(this, usuarios)
        rv.adapter = miAdapter

    }

    suspend fun getDataFromFireStore()  : QuerySnapshot? {
        return try{
            val data = db.collection("${Constantes.collectionUser}")
                .get()
                .await()
            data
        }catch (e : Exception){
            null
        }
    }

    private fun obtenerDatos(datos: QuerySnapshot?) {
        usuarios.clear()
        for(dc: DocumentChange in datos?.documentChanges!!){
            if (dc.type == DocumentChange.Type.ADDED){
                //miAr.add(dc.document.toObject(User::class.java))

                var prov = ProviderType.BASIC

                if(!dc.document.get("provider").toString().equals("BASIC")){prov= ProviderType.GOOGLE}

                var al = User(
                    dc.document.get("email").toString(),
                    dc.document.get("userName").toString(),
                    dc.document.get("phone").toString().toInt()
                )
                //Log.e(TAG, al.toString())
                usuarios.add(al)
            }
        }
    }
}
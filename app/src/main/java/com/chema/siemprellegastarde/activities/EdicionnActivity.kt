package com.chema.siemprellegastarde.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chema.siemprellegastarde.R
import com.chema.siemprellegastarde.model.User
import com.chema.siemprellegastarde.rv.AdapterRvUsers
import com.chema.siemprellegastarde.utils.*
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
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
    private lateinit var btn_ubicacion: Button
    private lateinit var btn_aceptar: Button
    private lateinit var ed_txt_hora: EditText
    private lateinit var ed_txt_ubicacion: EditText
    private lateinit var ed_txt_titulo_evento: EditText



    private var ubicacionActual : LatLng? = LatLng(-33.852, 151.211) //MARCADOR RANDOM EN SIDNEY PARA PROBAR EL EJERCICIO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edicionn)


        btn_fecha = findViewById(R.id.btn_fecha)
        ed_txt_fecha = findViewById(R.id.ed_txt_fecha)
        btn_hora = findViewById(R.id.btn_hora)
        btn_aceptar = findViewById(R.id.btn_aceptar)
        btn_ubicacion = findViewById(R.id.btn_ubicacion)
        ed_txt_hora = findViewById(R.id.ed_txt_hora)
        ed_txt_ubicacion = findViewById(R.id.ed_txt_ubicacion)
        ed_txt_titulo_evento = findViewById(R.id.ed_txt_titulo_evento)


        //var user = User("che@che.com","Chema",666745101)
        VariblesComunes.usuariosEventoActual.clear()
        //VaraiblesComunes.usuariosEventoActual.add("che@che.com")



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

        btn_aceptar.setOnClickListener{
            if(check_aceptar()){
                guardar_evento()
            }
        }
    }


    override fun onRestart() {
        super.onRestart()
        cambiar_UbicacionActual()
    }
    override fun onResume() {
        super.onResume()
        cambiar_UbicacionActual()
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


    /*
    public fun cambiar_UbicacionActual_Desde_Fuera(ubi : LatLng){
        ubicacionActual = ubi
    }

     */

  private fun cambiar_UbicacionActual(){
      /*
      if(ubicacionActual != VaraiblesComunes.marcadorActual){
          ubicacionActual = VaraiblesComunes.marcadorActual
          ed_txt_ubicacion.setText("${ubicacionActual}")
      }

       */

      if(VariblesComunes.latEventoActual != null && VariblesComunes.lonEventoActual != null){
          ubicacionActual = LatLng(VariblesComunes.latEventoActual.toString().toDouble(),VariblesComunes.lonEventoActual.toString().toDouble())
          ed_txt_ubicacion.setText("${ubicacionActual}")
      }
  }



    private fun guardar_evento(){
        var userLlegados = ArrayList<String>()
        var horaUserLlegados = ArrayList<String>()
        var evento = hashMapOf(

            "nombreEvento" to ed_txt_titulo_evento.text.toString(),
            "fecha" to ed_txt_fecha.text.toString().trim(),
            "hora" to ed_txt_hora.text.toString().trim(),
            "ubicacion" to ed_txt_ubicacion.text.toString().trim(),
            "latUbi" to VariblesComunes.latEventoActual,
            "lonUbi" to VariblesComunes.lonEventoActual,
            "emailAsistentes" to VariblesComunes.usuariosEventoActual,
            "emailAsistentesLlegada" to userLlegados,
            "asistentesLlegadaHora" to horaUserLlegados,
        )
        var id_evento = "${ed_txt_titulo_evento.text.toString()}"
        //var time = Timestamp(System.currentTimeMillis())
        //val rnds = (0..100).random()
        //id_evento += "_id${time}${rnds} "

        db.collection("${Constantes.collectionEvents4}")
            .document(id_evento) //Será la clave del documento.
            .set(evento).addOnSuccessListener {
                Toast.makeText(this, getString(R.string.almacenado), Toast.LENGTH_SHORT).show()
                finish()
            }.addOnFailureListener{
                Toast.makeText(this, getString(R.string.ocurridoError), Toast.LENGTH_SHORT).show()
            }
    }

    private fun check_aceptar():Boolean{
        var correcto = true

        if(ed_txt_titulo_evento.text.isEmpty()){
            correcto = false
            Toast.makeText(this,"Ponle un titulo al evento", Toast.LENGTH_SHORT).show()
        }
        if(ed_txt_hora.text.isEmpty()){
            correcto = false
            Toast.makeText(this,"Seleccione una hora", Toast.LENGTH_SHORT).show()
        }

        if(ed_txt_fecha.text.isEmpty()){
            correcto = false
            Toast.makeText(this,"Seleccione una fecha", Toast.LENGTH_SHORT).show()
        }

        if(ed_txt_ubicacion.text.isEmpty()){
            correcto = false
            Toast.makeText(this,"Seleccione una ubicacion", Toast.LENGTH_SHORT).show()
        }

        if(VariblesComunes.usuariosEventoActual.size<0){
            correcto = false
            Toast.makeText(this,"Añade por lo menos un invitado", Toast.LENGTH_SHORT).show()
        }

        return correcto
    }
}
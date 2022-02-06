package com.chema.siemprellegastarde.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chema.siemprellegastarde.R
import com.chema.siemprellegastarde.model.Evento
import com.chema.siemprellegastarde.rv.AdapterRvEventosConsulta
import com.chema.siemprellegastarde.utils.Constantes
import com.chema.siemprellegastarde.utils.ProviderType
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

class ListaEventosConsultaActivity : AppCompatActivity() {

    private val db = Firebase.firestore

    private lateinit var rv : RecyclerView
    var eventos : ArrayList<Evento> = ArrayList<Evento>()
    private lateinit var miAdapter: AdapterRvEventosConsulta

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_eventos_consulta)

        runBlocking {
            val job : Job = launch(context = Dispatchers.Default) {
                val datos : QuerySnapshot = getDataFromFireStore() as QuerySnapshot //Obtenermos la colección
                obtenerDatos(datos as QuerySnapshot?)  //'Destripamos' la colección y la metemos en nuestro ArrayList
            }
            //Con este método el hilo principal de onCreate se espera a que la función acabe y devuelva la colección con los datos.
            job.join() //Esperamos a que el método acabe: https://dzone.com/articles/waiting-for-coroutines
        }

        cargarRV()
    }

    private fun cargarRV(){

        rv = findViewById(R.id.rv_eventos_consulta)
        rv.setHasFixedSize(true)
        rv.layoutManager = LinearLayoutManager(this)
        miAdapter = AdapterRvEventosConsulta(this, eventos)
        rv.adapter = miAdapter
    }

    suspend fun getDataFromFireStore()  : QuerySnapshot? {
        return try{
            val data = db.collection("${Constantes.collectionEvents4}")
                .get()
                .await()
            data
        }catch (e : Exception){
            null
        }
    }

    private fun obtenerDatos(datos: QuerySnapshot?) {
        eventos.clear()
        for(dc: DocumentChange in datos?.documentChanges!!){
            if (dc.type == DocumentChange.Type.ADDED){
                //miAr.add(dc.document.toObject(User::class.java))

                var prov = ProviderType.BASIC

                //if(!dc.document.get("provider").toString().equals("BASIC")){prov= ProviderType.GOOGLE}

                var al = Evento(
                    dc.document.get("nombreEvento").toString(),
                    dc.document.get("fecha").toString(),
                    dc.document.get("hora").toString(),
                    dc.document.get("ubicacion").toString(),
                    dc.document.get("latUbi").toString(),
                    dc.document.get("lonUbi").toString(),
                    dc.document.get("emailAsistentes") as ArrayList<String>,
                    dc.document.get("emailAsistentesLlegada") as ArrayList<String>,
                    dc.document.get("asistentesLlegadaHora") as ArrayList<String>,
                )
                //Log.e(TAG, al.toString())
                eventos.add(al)
            }
        }
    }
}
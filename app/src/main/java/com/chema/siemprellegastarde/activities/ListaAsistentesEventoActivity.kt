package com.chema.siemprellegastarde.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chema.siemprellegastarde.R
import com.chema.siemprellegastarde.model.Evento
import com.chema.siemprellegastarde.model.User
import com.chema.siemprellegastarde.rv.AdapterRvUsers
import com.chema.siemprellegastarde.rv.AdapterRvUsersLlegada
import com.chema.siemprellegastarde.utils.Constantes
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

class ListaAsistentesEventoActivity : AppCompatActivity() {

    private val db = Firebase.firestore

    private lateinit var rv : RecyclerView
    var usuarios : ArrayList<User> = ArrayList<User>()
    private lateinit var miAdapter: AdapterRvUsersLlegada
    private var nombreEvento : String? = null
    private var evento : Evento? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_asistentes_evento)

        val bundle:Bundle? = intent.extras
        nombreEvento = (bundle?.getString("nombreEvento"))
        Log.e("preuba1",nombreEvento.toString())


        runBlocking {
            Log.e("preuba1","Prueba1")
            val job : Job = launch(context = Dispatchers.Default) {
                val datos : QuerySnapshot = getDataFromFireStore() as QuerySnapshot //Obtenermos la colección
                Log.e("preuba1",datos.toString())
                obtenerDatos(datos as QuerySnapshot?)  //'Destripamos' la colección y la metemos en nuestro ArrayList
            }
            Log.e("preuba1",job.toString())
            //Con este método el hilo principal de onCreate se espera a que la función acabe y devuelva la colección con los datos.
            job.join() //Esperamos a que el método acabe: https://dzone.com/articles/waiting-for-coroutines
        }

        runBlocking {
            Log.e("preuba2","Prueba2")
            val job2 : Job = launch(context = Dispatchers.Default) {
                val datos2 : QuerySnapshot = getDataFromFireStore2() as QuerySnapshot //Obtenermos la colección
                Log.e("preuba1",datos2.toString())
                obtenerDatos2(datos2 as QuerySnapshot?)  //'Destripamos' la colección y la metemos en nuestro ArrayList
            }
            //Con este método el hilo principal de onCreate se espera a que la función acabe y devuelva la colección con los datos.
            job2.join() //Esperamos a que el método acabe: https://dzone.com/articles/waiting-for-coroutines
        }

        cargarRV()
    }

    fun cargarRV(){

        rv = findViewById(R.id.rv_asistentes_evento)
        rv.setHasFixedSize(true)
        rv.layoutManager = LinearLayoutManager(this)
        miAdapter = AdapterRvUsersLlegada(this, usuarios, evento!!)
        rv.adapter = miAdapter

    }

    suspend fun getDataFromFireStore()  : QuerySnapshot? {
        return try{
            val data = db.collection("${Constantes.collectionEvents4}")
                .whereEqualTo("nombreEvento","${nombreEvento}")
                .get()
                .await()
            data
        }catch (e : Exception){
            null
        }
    }

    suspend fun getDataFromFireStore2()  : QuerySnapshot? {
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
        evento = null

        for(dc: DocumentChange in datos?.documentChanges!!){
            if (dc.type == DocumentChange.Type.ADDED){
                var nombreEvento = (dc.document.get("nombreEvento") as String?)
                var fecha = (dc.document.get("fecha") as String?)
                var hora = (dc.document.get("hora") as String?)
                var ubicacion = (dc.document.get("ubicacion") as String?)
                var latUbi = (dc.document.get("latUbi") as String?)
                var lonUbi = (dc.document.get("lonUbi") as String?)
                var emailAsistentes = (dc.document.get("emailAsistentes") as ArrayList<String>?)
                var emailAsistentesLlegada = (dc.document.get("emailAsistentesLlegada") as ArrayList<String>?)
                var asistentesLlegadaHora = (dc.document.get("asistentesLlegadaHora") as ArrayList<String>?)

                evento = Evento(nombreEvento,fecha,hora,ubicacion,latUbi,lonUbi,emailAsistentes,emailAsistentesLlegada,asistentesLlegadaHora)
                Log.e("preuba1",evento.toString())
            }
        }

        /*
        db.collection("${Constantes.collectionEvents}").document("${nombreEvento}").get().addOnSuccessListener {


            //Si encuentra el documento será satisfactorio este listener y entraremos en él.
            var nombreEvento = (it.get("nombreEvento") as String?)
            var fecha = (it.get("fecha") as String?)
            var hora = (it.get("hora") as String?)
            var ubicacion = (it.get("ubicacion") as String?)
            var emailAsistentes = (it.get("emailAsistentes") as ArrayList<String>?)

            evento = Evento(nombreEvento,fecha,hora,ubicacion,emailAsistentes)
            Log.e("preuba1",evento.toString())

        }.addOnFailureListener{
            Toast.makeText(this, "Algo ha ido mal al recuperar", Toast.LENGTH_SHORT).show()

        }

         */
    }


    private fun obtenerDatos2(datos: QuerySnapshot?) {
        usuarios.clear()
        for(dc: DocumentChange in datos?.documentChanges!!){
            if (dc.type == DocumentChange.Type.ADDED){

                var al = User(
                    dc.document.get("email").toString(),
                    dc.document.get("userName").toString(),
                    dc.document.get("phone").toString().toInt()
                )
                check_asistentes(al)
            }
        }
    }



    fun check_asistentes(user : User){
        val emailUser = user.email
        for(email in evento?.emailAsistentes!!){
            if(emailUser.equals(email)){
                usuarios.add(user)
            }
        }
    }
}
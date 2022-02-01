package com.chema.siemprellegastarde.rv

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.chema.siemprellegastarde.R
import com.chema.siemprellegastarde.activities.ListaAsistentesEventoActivity
import com.chema.siemprellegastarde.activities.MapsNewEventoActivity
import com.chema.siemprellegastarde.model.Evento
import com.chema.siemprellegastarde.model.User
import com.chema.siemprellegastarde.utils.Constantes
import com.google.firebase.firestore.FirebaseFirestore

class AdapterRvEventos (
    private val context: AppCompatActivity,
    private val eventos: ArrayList<Evento>
) : RecyclerView.Adapter<AdapterRvEventos.ViewHolderEvento>() {


    override fun getItemCount(): Int {
        return eventos.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterRvEventos.ViewHolderEvento {

        return AdapterRvEventos.ViewHolderEvento(
            LayoutInflater.from(context).inflate(R.layout.item_event_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: AdapterRvEventos.ViewHolderEvento, position: Int) {
        //holder?.item.text = this.valores!![position].toString()
        var evento: Evento = eventos[position]
        holder.nombre_evento.text = evento.nombreEvento
        holder.fecha_evento.text = evento.fecha




        holder.itemView.setOnClickListener {
            val asistIntent = Intent(context, MapsNewEventoActivity::class.java).apply {
                putExtra("ubicacion_evento",evento.ubicacion.toString())
            }
            context.startActivity(asistIntent)
        }

        //ELIMINAR EVENTO
        holder.itemView.setOnLongClickListener(View.OnLongClickListener {
            AlertDialog.Builder(context).setTitle("¿Desea eliminar este evento?")
                .setPositiveButton("Eliminar") { view, _ ->
                    //elimina evento
                    val db = FirebaseFirestore.getInstance()
                    db.collection("${Constantes.collectionEvents}").document("${evento.nombreEvento}").delete()
                    Toast.makeText(context, "Evento eliminada", Toast.LENGTH_SHORT)
                        .show()
                    view.dismiss()
                }.setNegativeButton("Cancelar") { view, _ ->//cancela
                    view.dismiss()
                }.create().show()
            false
        })
    }

    //************************************************************
    class ViewHolderEvento(view: View) : RecyclerView.ViewHolder(view) {

        val nombre_evento = view.findViewById<TextView>(R.id.nombreEvento_item)
        val fecha_evento = view.findViewById<TextView>(R.id.fechaEvento_item)

    }
}
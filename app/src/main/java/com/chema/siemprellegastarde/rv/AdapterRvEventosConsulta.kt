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
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.chema.siemprellegastarde.R
import com.chema.siemprellegastarde.activities.HomeActivity
import com.chema.siemprellegastarde.activities.ListaAsistentesEventoActivity
import com.chema.siemprellegastarde.model.Evento
import com.chema.siemprellegastarde.model.User
import com.chema.siemprellegastarde.utils.Constantes
import com.chema.siemprellegastarde.utils.VariblesComunes
import com.google.firebase.firestore.FirebaseFirestore

class AdapterRvEventosConsulta (
    private val context: AppCompatActivity,
    private val eventos: ArrayList<Evento>
) : RecyclerView.Adapter<AdapterRvEventosConsulta.ViewHolderEvento>() {


    override fun getItemCount(): Int {
        return eventos.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterRvEventosConsulta.ViewHolderEvento {

        return AdapterRvEventosConsulta.ViewHolderEvento(
            LayoutInflater.from(context).inflate(R.layout.item_event_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: AdapterRvEventosConsulta.ViewHolderEvento, position: Int) {
        //holder?.item.text = this.valores!![position].toString()
        var evento: Evento = eventos[position]
        holder.nombre_evento.text = evento.nombreEvento
        holder.fecha_evento.text = evento.fecha
        holder.hora_evento.text = evento.hora




        holder.itemView.setOnClickListener {

            val asistIntent = Intent(context, ListaAsistentesEventoActivity::class.java).apply {
                putExtra("nombreEvento",evento.nombreEvento)
                VariblesComunes.eventoActual = evento
            }
            context.startActivity(asistIntent)
        }

    }

    //************************************************************
    class ViewHolderEvento(view: View) : RecyclerView.ViewHolder(view) {

        val nombre_evento = view.findViewById<TextView>(R.id.nombreEvento_item)
        val fecha_evento = view.findViewById<TextView>(R.id.fechaEvento_item)
        val hora_evento = view.findViewById<TextView>(R.id.txt_hora_evento)

    }
}
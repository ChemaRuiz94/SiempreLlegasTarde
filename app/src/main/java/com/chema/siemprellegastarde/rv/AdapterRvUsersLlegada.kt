package com.chema.siemprellegastarde.rv

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.chema.siemprellegastarde.R
import com.chema.siemprellegastarde.model.Evento
import com.chema.siemprellegastarde.model.User
import com.chema.siemprellegastarde.utils.VariblesComunes

class AdapterRvUsersLlegada(
    private val context: AppCompatActivity,
    private val usuarios: ArrayList<User>,
    private val evento: Evento
) : RecyclerView.Adapter<AdapterRvUsersLlegada.ViewHolder>() {

    override fun getItemCount(): Int {
        return usuarios.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_user_llegada_evento_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //holder?.item.text = this.valores!![position].toString()
        var usuario: User = usuarios[position]
        holder.nombre.text = usuario.userName
        var n = check_llegada(usuarios[position].email)

        if(n != -1){
            holder.img_correcto.setImageResource(R.drawable.ic_baseline_check_24_yes)
            holder.hora.text = evento.asistentesLlegadaHora!![n].toString()
        }else{
            holder.img_correcto.setImageResource(R.drawable.ic_baseline_check_24_no)
            holder.hora.text = "NO HA LLEGADO"
        }

        //holder.img_correcto.setImageResource(R.drawable.ic_baseline_check_24_no)



        holder.itemView.setOnClickListener {
            //change_tick(holder,position)
            //Toast.makeText(context, "${usuario.userName} añadido", Toast.LENGTH_SHORT).show()
        }

    }

    fun check_llegada(emailUser : String):Int{

        /*
        for (email in evento.emailAsistentesLlegada.toString()){
            if(emailUser.equals(email)){
                var index = evento.emailAsistentesLlegada!!.indexOf(email.toString())
                return index
            }
        }
        return -1

         */
        if(evento.emailAsistentesLlegada!!.contains(emailUser)){
            var index = evento.emailAsistentesLlegada!!.indexOf(emailUser)
            return index
        }else{
            return -1
        }
    }



    //************************************************************
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val nombre = view.findViewById<TextView>(R.id.txt_llegada_userName_item)
        val img_correcto = view.findViewById<ImageView>(R.id.img_llegada_item_user_correct)
        val hora = view.findViewById<TextView>(R.id.txt_hora_llegada_item)

    }
}
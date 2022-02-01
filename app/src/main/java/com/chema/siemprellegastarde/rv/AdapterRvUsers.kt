package com.chema.siemprellegastarde.rv

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.chema.siemprellegastarde.R
import com.chema.siemprellegastarde.model.User
import com.chema.siemprellegastarde.utils.VaraiblesComunes

class AdapterRvUsers (
    private val context: AppCompatActivity,
    private val usuarios: ArrayList<User>
) : RecyclerView.Adapter<AdapterRvUsers.ViewHolder>() {

    override fun getItemCount(): Int {
        return usuarios.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_user_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //holder?.item.text = this.valores!![position].toString()
        var usuario: User = usuarios[position]
        holder.nombre.text = usuario.userName
        holder.img_correcto.setImageResource(R.drawable.ic_baseline_check_24_no)



        holder.itemView.setOnClickListener {
            change_tick(holder,position)
            //Toast.makeText(context, "${usuario.userName} añadido", Toast.LENGTH_SHORT).show()
        }

    }

    fun change_tick(holder: ViewHolder, position: Int){
        if (holder.txt_asiste.text.equals("Asiste")){
            holder.img_correcto.setImageResource(R.drawable.ic_baseline_check_24_no)
            holder.txt_asiste.text = "No asiste"
            VaraiblesComunes.usuariosEventoActual.remove(usuarios[position].email)
        }else{
            holder.img_correcto.setImageResource(R.drawable.ic_baseline_check_24_yes)
            holder.txt_asiste.text = "Asiste"
            VaraiblesComunes.usuariosEventoActual.add(usuarios[position].email)

        }
    }


    //************************************************************
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val nombre = view.findViewById<TextView>(R.id.txt_userName_item)
        val txt_asiste = view.findViewById<TextView>(R.id.txt_asiste_item_user)
        val img_correcto = view.findViewById<ImageView>(R.id.img_item_user_correcto)

    }
}
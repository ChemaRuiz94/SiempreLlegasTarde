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



        holder.itemView.setOnClickListener {
            Toast.makeText(context, "${usuario.userName}", Toast.LENGTH_SHORT).show()
        }
    }


    //************************************************************
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val nombre = view.findViewById<TextView>(R.id.txt_userName_item)
        val img_correcto = view.findViewById<ImageView>(R.id.img_item_user_correcto)

    }
}
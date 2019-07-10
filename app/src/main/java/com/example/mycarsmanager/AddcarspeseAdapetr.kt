package com.example.mycarsmanager

import android.app.AlertDialog
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.Navigation
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class AddcarspeseAdapetr(val carList: ArrayList<Car>): RecyclerView.Adapter<AddcarspeseAdapetr.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.item_layout, p0, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return carList.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.txtmodel?.text = carList[p1].Model
        p0.txtowner?.text = carList[p1].Owner
        val id = carList[p1].uid
        val url = carList[p1].url
        Picasso.with(p0.img_car.context).load(url).into(p0.img_car)

        val model = p0.txtmodel?.text
        val own = p0.txtowner?.text

        p0.item.setOnClickListener {
            val info = Bundle()
            info.putString("Model", "$model")
            info.putString("Owner", "$own")
            info.putString("Codice", "$id")

            Navigation.findNavController(it).navigate(R.id.action_selectcar_addspesa_to_addspesa, info)

        }
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val txtmodel = itemView.findViewById<TextView>(R.id.txtModel)
        val txtowner = itemView.findViewById<TextView>(R.id.txtOwner)
        val img_car = itemView.findViewById<CircleImageView>(R.id.img_car)
        val item = itemView.findViewById<LinearLayout>(R.id.item_body)
    }
}
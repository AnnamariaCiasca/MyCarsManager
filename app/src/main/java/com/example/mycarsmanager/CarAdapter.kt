package com.example.mycarsmanager

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class CarAdapter(val carList: ArrayList<Car>): RecyclerView.Adapter<CarAdapter.ViewHolder>(){
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.item_layout, p0, false)
        return ViewHolder(v)    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.txtmodel?.text= carList[p1].Model
        p0.txtowner?.text= carList[p1].Owner    }

    override fun getItemCount(): Int {
        return carList.size
    }


    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val txtmodel = itemView.findViewById<TextView>(R.id.txtModel)
        val txtowner = itemView.findViewById<TextView>(R.id.txtOwner)
    }

}
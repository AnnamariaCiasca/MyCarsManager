package com.example.mycarsmanager

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class CompAdapter(val compList: ArrayList<componenti>): RecyclerView.Adapter<CompAdapter.ViewHolder>(){
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.comp_layout, p0, false)
        return ViewHolder(v) }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.txtname?.text = compList[p1].nome
        val url = compList[p1].img_url
        if (url.isEmpty()){

        }else {
            Picasso.with(p0.img_url.context).load(url).into(p0.img_url)
        }
    }

    override fun getItemCount(): Int {
        return compList.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val txtname = itemView.findViewById<TextView>(R.id.txt_comp)
        val img_url = itemView.findViewById<CircleImageView>(R.id.img_comp)

    }


}
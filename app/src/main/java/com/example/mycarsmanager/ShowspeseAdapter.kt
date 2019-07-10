package com.example.mycarsmanager

import android.app.AlertDialog
import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_addspesa.*
import kotlinx.android.synthetic.main.fragment_spese.*

class ShowspeseAdapter(val carList: ArrayList<Car>, val alert: AlertDialog, val txt_id: TextView, val file: TextView, val list: RecyclerView, val c: Context): RecyclerView.Adapter<ShowspeseAdapter.ViewHolder>() {
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

        val url = carList[p1].url
        Picasso.with(p0.img_car.context).load(url).into(p0.img_car)

        val model = p0.txtmodel?.text.toString()
        val own = p0.txtowner?.text.toString()
        val id = carList[p1].uid

        p0.item.setOnClickListener {

            file.text= model+"_"+own
            txt_id.text = id

            addRecycler()

            alert.dismiss()

        }
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val txtmodel = itemView.findViewById<TextView>(R.id.txtModel)
        val txtowner = itemView.findViewById<TextView>(R.id.txtOwner)
        val img_car = itemView.findViewById<CircleImageView>(R.id.img_car)
        val item = itemView.findViewById<LinearLayout>(R.id.item_body)
    }

    private fun addRecycler(){
        list.layoutManager= LinearLayoutManager(c, LinearLayout.VERTICAL, false)

        val filename_car = file.text.toString()
        val uid = txt_id.text.toString()

        val docutente = FirebaseFirestore.getInstance().collection("Utenti").document("$uid")
        val docucar = docutente.collection("Vettura")

        val docuspese = docucar.document(filename_car).collection("Spese")

        docuspese.get()
            .addOnSuccessListener {
                val spese = ArrayList<expenses>()

                for(document in it){
                    val title = document.getString("Titolo").toString()
                    val desc= document.getString("Descrizione").toString()
                    val prezzo = document.getString("Prezzo").toString()
                    val id = document.getString("Codice").toString()

                    val spesa = expenses(title,desc,prezzo, filename_car,id)

                    spese.add(spesa)
                }


                Log.d("Garage", "$filename_car $uid")
                list.adapter= ExpensesAdapter(spese,list)

            }
    }
}
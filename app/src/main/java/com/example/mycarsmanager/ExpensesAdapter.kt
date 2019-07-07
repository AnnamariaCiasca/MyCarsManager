package com.example.mycarsmanager

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ExpensesAdapter(val expensesList: ArrayList<expenses>): RecyclerView.Adapter<ExpensesAdapter.ViewHolder>(){
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.expenses_item, p0, false)
        return ViewHolder(v) }

    override fun getItemCount(): Int {
        return expensesList.size
    }

    private val mAuth = FirebaseAuth.getInstance()
    private val utente = mAuth?.currentUser
    private val id = utente?.uid
    private val mStore = FirebaseFirestore.getInstance()
    private val docutente = mStore.collection("Utenti").document("$id")
    private val docucar = docutente.collection("Vettura")

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.txtitle?.text = expensesList[p1].Tipo
        p0.txtdesc?.text = expensesList[p1].desc
        p0.txteuro?.text = expensesList[p1].money + "€"

        p0.body.setOnClickListener {
            val type = expensesList[p1].Tipo
            val desc = expensesList[p1].desc
            val money = expensesList[p1].money
            val filename = expensesList[p1].file

            val docuspesa = docucar.document(filename).collection("Spese").document(type+desc+money)

            docuspesa.delete()
                .addOnSuccessListener {
                    Log.d("Spese", "Oggetto Eliminato, $type $desc $money $filename")

                }
        }

    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val txtitle = itemView.findViewById<TextView>(R.id.txtTitle)
        val txtdesc = itemView.findViewById<TextView>(R.id.txtDescr)
        val txteuro = itemView.findViewById<TextView>(R.id.txtEuro)
        val body = itemView.findViewById<LinearLayout>(R.id.item_body)

    }
}
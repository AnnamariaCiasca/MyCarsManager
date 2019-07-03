package com.example.mycarsmanager

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class ExpensesAdapter(val expensesList: ArrayList<expenses>): RecyclerView.Adapter<ExpensesAdapter.ViewHolder>(){
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.expenses_item, p0, false)
        return ViewHolder(v) }

    override fun getItemCount(): Int {
        return expensesList.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.txtitle?.text = expensesList[p1].Tipo
        p0.txtdesc?.text = expensesList[p1].desc
        p0.txteuro?.text = expensesList[p1].money + "€"


    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val txtitle = itemView.findViewById<TextView>(R.id.txtTitle)
        val txtdesc = itemView.findViewById<TextView>(R.id.txtDescr)
        val txteuro = itemView.findViewById<TextView>(R.id.txtEuro)
    }
}
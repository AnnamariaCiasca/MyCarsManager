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
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.delete_form.view.*

class ExpensesAdapter(val expensesList: ArrayList<expenses>, val list: RecyclerView): RecyclerView.Adapter<ExpensesAdapter.ViewHolder>(){
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.expenses_item, p0, false)
        return ViewHolder(v) }

    override fun getItemCount(): Int {
        return expensesList.size
    }

    private val mStore = FirebaseFirestore.getInstance()


    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.txtitle?.text = expensesList[p1].Tipo
        p0.txtdesc?.text = expensesList[p1].desc
        p0.txteuro?.text = expensesList[p1].money + "€"
        val id = expensesList[p1].id
        val filename = expensesList[p1].file
        val spesaname = expensesList[p1].Tipo+expensesList[p1].desc+expensesList[p1].money

        p0.body.setOnClickListener {
            showdialog(filename,spesaname,it, id)
        }

    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val txtitle = itemView.findViewById<TextView>(R.id.txtTitle)
        val txtdesc = itemView.findViewById<TextView>(R.id.txtDescr)
        val txteuro = itemView.findViewById<TextView>(R.id.txtEuro)
        val body = itemView.findViewById<LinearLayout>(R.id.item_body)
    }

    private fun showdialog(filename: String, spesaname: String, view: View, id: String) {
        val mDialogView = LayoutInflater.from(view.context).inflate(R.layout.delete_form, null)

        val mBuilder = AlertDialog.Builder(view.context)
            .setView(mDialogView)

        val mAlertDialog = mBuilder.show()

        mDialogView.txt_delete.text = "Sei sicuro di voler eliminare questo elemento?"

        mDialogView.btn_cancel_delete.setOnClickListener {
            mAlertDialog.dismiss()
        }

        mDialogView.btn_accept_delete.setOnClickListener {
            mAlertDialog.dismiss()
            cancella(filename, spesaname, view, id)
        }
    }

    private fun cancella(filename: String, spesaname: String, view: View, id: String) {
        val docutente = mStore.collection("Utenti").document("$id")
        val docucar = docutente.collection("Vettura")

        val docuspesa = docucar.document(filename).collection("Spese").document(spesaname)

        docuspesa.delete()
            .addOnSuccessListener {
                Log.d("Spese", "Oggetto Eliminato, $spesaname $filename")
                addRecycler(filename, id, view.context!!, list)
            }
    }

    private fun addRecycler(filename_car: String, uid:String, c: Context, list: RecyclerView){
        list.layoutManager= LinearLayoutManager(c, LinearLayout.VERTICAL, false)


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
                list.adapter= ExpensesAdapter(spese, list)

            }
    }


}
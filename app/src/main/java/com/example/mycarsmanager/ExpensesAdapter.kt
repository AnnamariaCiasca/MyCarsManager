package com.example.mycarsmanager

import android.app.AlertDialog
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.delete_form.view.*
import kotlinx.android.synthetic.main.fragment_description_car.*

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
        val filename = expensesList[p1].file
        val spesaname = expensesList[p1].Tipo+expensesList[p1].desc+expensesList[p1].money

        p0.body.setOnClickListener {
            showdialog(filename,spesaname,it)
        }

    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val txtitle = itemView.findViewById<TextView>(R.id.txtTitle)
        val txtdesc = itemView.findViewById<TextView>(R.id.txtDescr)
        val txteuro = itemView.findViewById<TextView>(R.id.txtEuro)
        val body = itemView.findViewById<LinearLayout>(R.id.item_body)
    }

    private fun showdialog(filename: String, spesaname: String, view: View) {
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
            cancella(filename, spesaname, view)
        }
    }

    private fun cancella(filename: String, spesaname: String, view: View) {
        val docuspesa = docucar.document(filename).collection("Spese").document(spesaname)

        docuspesa.delete()
            .addOnSuccessListener {
                Log.d("Spese", "Oggetto Eliminato, $spesaname $filename")
                Toast.makeText(view.context, "Ricarica le spese per vedere i cambiamenti", Toast.LENGTH_SHORT).show()
            }
    }


}
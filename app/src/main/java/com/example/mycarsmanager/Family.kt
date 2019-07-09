package com.example.mycarsmanager

import android.app.AlertDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.add_family_form.view.*
import kotlinx.android.synthetic.main.add_family_form.view.etxt_name_family
import kotlinx.android.synthetic.main.add_family_form.view.switch_family
import kotlinx.android.synthetic.main.fragment_family.*
import java.util.*

class Family : Fragment() {

    private val mAuth = FirebaseAuth.getInstance()
    private val mStore = FirebaseFirestore.getInstance()
    private val uid = mAuth.uid.toString()
    private val docutente = mStore.collection("Utenti").document(uid)
    private val docufamiglia = mStore.collection("Famiglia")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_family, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_menu_family.setOnClickListener { Navigation.findNavController(view).navigate(R.id.action_family_to_dashboard3) }

        docutente.get()
            .addOnCompleteListener {
                val famiglia = it.result?.getString("Famiglia").toString()

                if (famiglia == ""){
                    btn_add_family.isEnabled = true
                    btn_delete_family.isEnabled = false
                }else{
                    btn_add_family.isEnabled = false
                    btn_delete_family.isEnabled = true

                    caricarecycler(famiglia)
                }



            }

        btn_add_family.setOnClickListener {
            showdialog()
        }
        btn_delete_family.setOnClickListener {
            Log.d("Family", "Eliminata")
        }


    }

    private fun caricarecycler(famiglia: String) {
        recycler_family.layoutManager= LinearLayoutManager(activity, LinearLayout.VERTICAL, false)

        val docucomponenti = docufamiglia.document(famiglia).collection("Componenti")

        docucomponenti.get()
            .addOnSuccessListener {
                val comp = ArrayList<componenti>()

                for(document in it){
                    val nome = document.getString("Nome").toString()
                    val url = document.getString("img_url").toString()

                    val componente = componenti(nome,url)

                    comp.add(componente)
                }

                recycler_family.adapter = CompAdapter(comp)
            }
    }

    private fun showdialog() {
        val mDialogView = LayoutInflater.from(activity).inflate(R.layout.add_family_form, null)

        val mBuilder = AlertDialog.Builder(activity)
            .setView(mDialogView)

        val mAlertDialog = mBuilder.show()



        mDialogView.btn_confirm.setOnClickListener {
            val nomefamiglia = mDialogView.etxt_name_family.text.toString()
            val codicefamiglia = mDialogView.etxt_codice_family.text.toString()

            if (mDialogView.switch_family.isChecked){
                Log.d("Family", "Entra")
                aggiungicomponente(nomefamiglia,codicefamiglia, mAlertDialog)
            }else{
                Log.d("Family", "Crea $nomefamiglia $codicefamiglia")
                creafamily(nomefamiglia, codicefamiglia, mAlertDialog)

            }
        }

        mDialogView.btn_cancel.setOnClickListener {
            mAlertDialog.dismiss()
        }
    }

    private fun aggiungicomponente(nomefamiglia: String, codicefamiglia: String, alert: AlertDialog) {
        if (nomefamiglia.isEmpty()|| codicefamiglia.isEmpty()){
            Toast.makeText(context!!,"Inserire nome famiglia o codice.", Toast.LENGTH_SHORT).show()
            return
        }

        docufamiglia.get()
            .addOnSuccessListener {
                for (document in it) {
                    val nome = document.getString("Nome").toString()
                    if (nome == nomefamiglia) {
                        val codice = document.getString("Codice").toString()
                        if (codice == codicefamiglia){
                            docutente.update("Famiglia",nomefamiglia)
                            crearecomponenti(nomefamiglia)
                            alert.dismiss()
                        }else{
                            Toast.makeText(context!!,"Codice Errato", Toast.LENGTH_SHORT).show()
                        }
                        break
                    }else{
                        Toast.makeText(context!!,"Famiglia non esistente", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    private fun crearecomponenti(nomefamiglia: String) {
        docutente.get()
            .addOnCompleteListener {
                val nomecomponente = it.result?.getString("Nome").toString()
                val img_url = it.result?.getString("img_url").toString()

                val componente = hashMapOf(
                    "Nome" to nomecomponente,
                    "img_url" to img_url
                )

                val docucomponenti = docufamiglia.document(nomefamiglia).collection("Componenti").document(nomecomponente)

                docucomponenti.set(componente as Map<String, Any>)

                caricarecycler(nomefamiglia)
            }


    }

    private fun creafamily(nomefamiglia: String, codicefamiglia: String, alert: AlertDialog){

        if (nomefamiglia.isEmpty()|| codicefamiglia.isEmpty()){
            Toast.makeText(context!!,"Inserire nome famiglia o codice.", Toast.LENGTH_SHORT).show()
            return
        }

            docufamiglia.get()
                .addOnSuccessListener {
                    for (document in it) {
                        val nome = document.getString("Nome").toString()
                        if (nome == nomefamiglia) {
                            Toast.makeText(context!!, "Nome Famiglia già esistente.", Toast.LENGTH_SHORT).show()
                            return@addOnSuccessListener
                        }

                        val famiglia = hashMapOf(
                            "Admin" to mAuth.currentUser!!.email.toString(),
                            "Nome" to nomefamiglia,
                            "Codice" to codicefamiglia,
                            "Elimina" to UUID.randomUUID().toString()
                        )

                        val documentofam = docufamiglia.document(nomefamiglia)
                        documentofam.set(famiglia as Map<String, Any>)
                        docutente.update("Famiglia",nomefamiglia)
                        crearecomponenti(nomefamiglia)


                        alert.dismiss()
                    }
                }





    }
}

package com.example.mycarsmanager


import android.app.AlertDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_description_car.*


class description_car : Fragment() {

    private val mAuth = FirebaseAuth.getInstance()
    private val mStore = FirebaseFirestore.getInstance()
    private val mStorage = FirebaseStorage.getInstance()
    private val utente = mAuth?.currentUser
    private val id = utente?.uid
    private val docutente = mStore.collection("Utenti").document("$id")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_description_car, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val model = arguments?.getString("Model")
        val owner = arguments?.getString("Owner")
        val docucar = docutente.collection("Vettura").document("$model"+"_$owner")

        txt_macchina.text=model

        docucar.get()
            .addOnCompleteListener {
                val mod = it.result?.getString("Modello").toString()
                val own = it.result?.getString("Owner").toString()
                val targa = it.result?.getString("Targa").toString()
                val bol = it.result?.getString("Scadenza Bollo").toString()
                val rca = it.result?.getString("Scadenza RCA").toString()
                val rev = it.result?.getString("Ultima Rev").toString()
                val url = it.result?.getString("img_url").toString()

                Picasso.with(activity).load(url).into(img_car)

                etxt_model.setText(mod)
                etxt_owner.setText(own)
                etxt_targa.setText(targa)
                txt_bollo.setText(bol)
                txt_rca.setText(rca)
                etxt_rev.setText(rev)
            }

        btn_delete.setOnClickListener {
            dialog()
        }

    }


    private fun dialog(){
        val alert = AlertDialog.Builder(activity)
        val mod = txt_macchina.text
        alert.setTitle("Sei Sicuro?")
            .setMessage("Sicuro di voler Cancellare $mod ?")
            .setNegativeButton("Annulla") { dialog, which -> dialog.cancel()}
            .setPositiveButton("Conferma") {dialog, which -> cancella()}
        alert.show()
    }

    private fun cancella(){
        val model = txt_model.text
        val owner = txt_owner.text
        val docucar = docutente.collection("Vettura").document("$model"+"_$owner")
        docucar.delete()
            .addOnCompleteListener { Navigation.findNavController(this!!.view!!).navigate(R.id.action_description_car_to_garage) }

    }


}

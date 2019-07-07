package com.example.mycarsmanager


import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import android.provider.CalendarContract.Events.*
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.delete_form.view.*
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
        val filename = "$model"+"_$owner"
        val docucar = docutente.collection("Vettura").document(filename)

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
                etxt_bollo.setText(bol)
                etxt_rca.setText(rca)
                etxt_rev.setText(rev)
            }

        btn_delete.setOnClickListener {
            val mDialogView = LayoutInflater.from(it.context).inflate(R.layout.delete_form, null)

            val mBuilder = AlertDialog.Builder(it.context)
                .setView(mDialogView)
                .setTitle("Conferma Eliminazione")

            val mAlertDialog = mBuilder.show()

            mDialogView.txt_delete.text = "Sei sicuro di voler eliminare $model ?"

            mDialogView.btn_cancel_delete.setOnClickListener {
                mAlertDialog.dismiss()
            }

            mDialogView.btn_accept_delete.setOnClickListener {
                mAlertDialog.dismiss()
                cancella(filename, view)
            }


        }

        btn_back.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_description_car_to_garage)
        }

        img_calend_boll.setOnClickListener {
            val intent = Intent(Intent.ACTION_INSERT)
                .setData(CONTENT_URI)
                .putExtra(TITLE, "Scadenza Bollo")
                .putExtra(EVENT_LOCATION, "")
            startActivity(intent)
        }

    }

    private fun cancella(file: String, view: View){
        val docucar = docutente.collection("Vettura").document(file)
        docucar.delete()
            .addOnSuccessListener {
                Navigation.findNavController(view).navigate(R.id.action_description_car_to_garage)
            }

    }


}

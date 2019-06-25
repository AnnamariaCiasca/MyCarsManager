package com.example.mycarsmanager


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_addcar.*


class addcar : Fragment() {

    private val mAuth = FirebaseAuth.getInstance()
    private val mStore = FirebaseFirestore.getInstance()
    private val mStorage = FirebaseStorage.getInstance()
    private val utente = mAuth?.currentUser
    private val id = utente?.uid
    private val docutente = mStore.collection("Utenti").document("$id")
    private val docucar = docutente.collection("Vettura")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_addcar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_add.setOnClickListener {
            val modello = etxt_model.text.toString()
            val owner = etxt_owner.text.toString()
            val targa = etxt_targa.text.toString()
            val scadRCA = etxt_rca.text.toString()
            val scadBollo = etxt_bollo.text.toString()
            val lastRev = etxt_rev.text.toString()

            val Vettura = hashMapOf(
                "Modello" to modello,
                "Owner" to owner,
                "Targa" to targa,
                "Scadenza RCA" to scadRCA,
                "Scadenza Bollo" to scadBollo,
                "Ultima Rev." to lastRev
            )

            val doc_name = docucar.document("$modello _ $owner")

            doc_name.set(Vettura as Map<String, Any>)

            Navigation.findNavController(view).navigate(R.id.action_addcar_to_garage)

        }

        btn_cancel.setOnClickListener { Navigation.findNavController(view).navigate(R.id.action_addcar_to_garage) }
    }

}

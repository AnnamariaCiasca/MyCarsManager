package com.example.mycarsmanager

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_dashboard.*


class dashboard : Fragment() {

    private val mAuth = FirebaseAuth.getInstance()
    private val utente = mAuth?.currentUser
    private val id = utente?.uid
    private val mStore = FirebaseFirestore.getInstance()
    private val docutente = mStore.collection("Utenti").document("$id")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        btn_park.setOnClickListener {Toast.makeText(activity, "Questa funzionalità verrà implementata in futuro", Toast.LENGTH_SHORT).show()}
        btn_service.setOnClickListener {Toast.makeText(activity, "Questa funzionalità verrà implementata in futuro", Toast.LENGTH_SHORT).show()}
        btn_security.setOnClickListener {Toast.makeText(activity, "Questa funzionalità verrà implementata in futuro", Toast.LENGTH_SHORT).show()}
        btn_option.setOnClickListener {Toast.makeText(activity, "Questa funzionalità verrà implementata in futuro", Toast.LENGTH_SHORT).show()}

        docutente.get()
            .addOnCompleteListener {
                val nome = it.result?.getString("Nome").toString()

                if(FirebaseAuth.getInstance().currentUser == null){
                    btn_account.setOnClickListener{Navigation.findNavController(view).navigate(R.id.action_dashboard3_to_registro)}
                    btn_family.setOnClickListener {Toast.makeText(activity, "Per usare questa funzionalità effettuare il Login", Toast.LENGTH_SHORT).show()}
                    btn_garage.setOnClickListener {Toast.makeText(activity, "Per usare questa funzionalità effettuare il Login", Toast.LENGTH_SHORT).show()}
                    btn_spese.setOnClickListener {Toast.makeText(activity, "Per usare questa funzionalità effettuare il Login", Toast.LENGTH_SHORT).show()}
                }else{
                    if (nome.isEmpty()){
                        btn_account.setOnClickListener {Navigation.findNavController(view).navigate(R.id.action_dashboard3_to_account)}
                        btn_garage.setOnClickListener { Navigation.findNavController(view).navigate(R.id.action_dashboard3_to_garage)}
                        btn_spese.setOnClickListener { Navigation.findNavController(view).navigate(R.id.action_dashboard3_to_spese) }
                        btn_family.setOnClickListener {Toast.makeText(activity, "Per usare questa funzionalità completa l'Account con Nome", Toast.LENGTH_SHORT).show()}
                    }else {
                        btn_account.setOnClickListener { Navigation.findNavController(view).navigate(R.id.action_dashboard3_to_account) }
                        btn_garage.setOnClickListener { Navigation.findNavController(view).navigate(R.id.action_dashboard3_to_garage) }
                        btn_spese.setOnClickListener { Navigation.findNavController(view).navigate(R.id.action_dashboard3_to_spese) }
                        btn_family.setOnClickListener { Navigation.findNavController(view).navigate(R.id.action_dashboard3_to_family) }
                    }
                }
            }




        }

    }
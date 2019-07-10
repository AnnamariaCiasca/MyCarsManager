package com.example.mycarsmanager


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_selectcar_addspesa.*


class selectcar_addspesa : Fragment() {

    private val mAuth = FirebaseAuth.getInstance()
    private val mStore = FirebaseFirestore.getInstance()
    private val utente = mAuth?.currentUser
    private val id = utente?.uid
    private val docutente = mStore.collection("Utenti").document("$id")
    private val docucar = docutente.collection("Vettura")
    private val docufamiglia = mStore.collection("Famiglia")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_selectcar_addspesa, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        docutente.get()
            .addOnCompleteListener {
                val famiglia = it.result!!.getString("Famiglia").toString()
                recyclercar_spese.layoutManager= LinearLayoutManager(activity, LinearLayout.VERTICAL, false)

                if (famiglia == ""){

                    docucar.get()
                        .addOnSuccessListener {
                            val cars = ArrayList<Car>()

                            for(document in it){
                                val model = document.getString("Modello").toString()
                                val owner= document.getString("Owner").toString()
                                val url = document.getString("img_url").toString()
                                val id = document.getString("codice").toString()

                                val macchina= Car("$model","$owner", "$url", "$id")

                                cars.add(macchina)
                            }
                            Log.d("Garage","$cars")
                            recyclercar_spese.adapter= AddcarspeseAdapetr(cars)
                        }
                }else{
                    Log.d("Garage", "$famiglia")
                    val docucomponent = docufamiglia.document(famiglia).collection("Componenti")

                    docucomponent.get()
                        .addOnSuccessListener {
                            val cars = ArrayList<Car>()
                            for (document in it){
                                val uid = document.getString("codice").toString()

                                val docutenticar = mStore.collection("Utenti").document("$uid").collection("Vettura")


                                docutenticar.get()
                                    .addOnSuccessListener {


                                        for(document in it){
                                            val model = document.getString("Modello").toString()
                                            val owner= document.getString("Owner").toString()
                                            val url = document.getString("img_url").toString()
                                            val id = document.getString("codice").toString()

                                            Log.d("Garage", "$uid $docutenticar $model $owner $url")

                                            val macchina= Car("$model","$owner", "$url", "$id")

                                            cars.add(macchina)
                                        }
                                        Log.d("Garage","$cars")
                                        recyclercar_spese.adapter= AddcarspeseAdapetr(cars)

                                    }
                            }


                        }

                }
            }

        btn_indietro_spese.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_selectcar_addspesa_to_spese)
        }

    }


}

package com.example.mycarsmanager


import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_piechart.*


class piechart : Fragment() {


    private val mAuth = FirebaseAuth.getInstance()
    private val mStore = FirebaseFirestore.getInstance()
    private val utente = mAuth?.currentUser
    private val id = utente?.uid
    private val docutente = mStore.collection("Utenti").document("$id")
    private val docucar = docutente.collection("Vettura")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_piechart, container, false)
    }


    //var meccanico = 0
    //var carrozziere = 0
    //var multe = 0
    //var altro = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val file = arguments?.getString("File").toString()
        val docuspesa = docucar.document("$file").collection("Spese")

        docuspesa.get()
            .addOnSuccessListener {

                var carburante = 0
                var meccanico = 0
                var carrozziere = 0
                var multe = 0
                var altro = 0

                for(document in it){
                    val titolo = document.getString("Titolo").toString()
                    val prezzo = document.getString("Prezzo")!!.toInt()

                    when (titolo) {
                        "Carburante" -> carburante += prezzo

                    }

                    if ( titolo == "Carburante") {
                        carburante += prezzo
                    }
                    if ( titolo == "Meccanico"){
                        meccanico += prezzo
                    }
                    if ( titolo == "Carrozziere"){
                        carrozziere += prezzo
                    }
                    if ( titolo == "Multe"){
                        multe += prezzo
                    }
                    if ( titolo == "Altro"){
                        altro += prezzo
                    }

                    }

                val dati_torta = ArrayList<PieEntry>()
                dati_torta.add(PieEntry(carburante.toFloat()))
                dati_torta.add(PieEntry(meccanico.toFloat()))
                dati_torta.add(PieEntry(carrozziere.toFloat()))
                dati_torta.add(PieEntry(multe.toFloat()))
                dati_torta.add(PieEntry(altro.toFloat()))




                val dataSet = PieDataSet(dati_torta,"")

                val data = PieData(dataSet)

                pichart_spese.data = data

                

                pichart_spese.notifyDataSetChanged()
                pichart_spese.invalidate()

                }




                btn_indietro.setOnClickListener {
                    Navigation.findNavController(view).navigate(R.id.action_piechart_to_spese)
                }
            }
    }
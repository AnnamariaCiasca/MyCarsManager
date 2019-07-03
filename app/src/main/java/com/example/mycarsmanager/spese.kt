package com.example.mycarsmanager

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_spese.*
import kotlinx.android.synthetic.main.fragment_spese.menu


class spese : Fragment() {

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
        return inflater.inflate(R.layout.fragment_spese, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        menu.setOnClickListener { Navigation.findNavController(view).navigate(R.id.action_spese_to_dashboard3) }
        add_spese.setOnClickListener { Navigation.findNavController(view).navigate(R.id.action_spese_to_addspesa) }

        addSpinner()

        spese_list.layoutManager = LinearLayoutManager(activity, LinearLayout.VERTICAL, false)

        spin_car?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                file.text = parent?.selectedItem.toString()
            }

        }

        btn_show_spese.setOnClickListener {
            addRecycler()
        }

        btn_totale.setOnClickListener {
            showTotal()
        }

        btn_grafico.setOnClickListener {
            val auto = file.text.toString()

            val info = Bundle()
            info.putString("File", "$auto")

            Navigation.findNavController(view).navigate(R.id.action_spese_to_piechart,info)
        }


    }

    private fun addSpinner(){
        docucar.get()
            .addOnSuccessListener {
                val cars = ArrayList<String>()

                for(document in it){
                    val model = document.getString("Modello").toString()
                    val owner = document.getString("Owner").toString()

                    cars.add(model+"_"+owner)
                }

                val adap = ArrayAdapter(activity, android.R.layout.simple_spinner_item,cars)

                adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                spin_car?.adapter= adap

            }
    }

    private fun addRecycler(){
        spese_list.layoutManager= LinearLayoutManager(activity, LinearLayout.VERTICAL, false)

        val filename_spese = file.text.toString()

        val docuspese = docucar.document("$filename_spese").collection("Spese")

        docuspese.get()
            .addOnSuccessListener {
                val spese = ArrayList<expenses>()

                for(document in it){
                    val title = document.getString("Titolo").toString()
                    val desc= document.getString("Descrizione").toString()
                    val prezzo = document.getString("Prezzo").toString()

                    val spesa = expenses(title,desc,prezzo)

                    spese.add(spesa)
                }

                spese_list.adapter= ExpensesAdapter(spese)
            }
    }

    private fun showTotal(){
        val filename_spese = file.text.toString()

        val docuspese = docucar.document("$filename_spese").collection("Spese")

        docuspese.get()
            .addOnSuccessListener {
                var somma = 0

                for(document in it){
                    val prezzo = document.getString("Prezzo")!!.toInt()

                    somma += prezzo

                    totale.text = somma.toString()
                }

                val totale = totale.text.toString()
                Toast.makeText(activity,"La somma è: $totale€", Toast.LENGTH_LONG).show()

            }

    }

}

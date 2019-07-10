package com.example.mycarsmanager

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat.getSystemService
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_spese.*
import kotlinx.android.synthetic.main.fragment_spese.menu
import kotlinx.android.synthetic.main.showspesa_select.view.*


class spese : Fragment() {

    private val mAuth = FirebaseAuth.getInstance()
    private val mStore = FirebaseFirestore.getInstance()
    private val utente = mAuth?.currentUser
    private val id = utente?.uid
    private val docufamiglia = mStore.collection("Famiglia")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_spese, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hide()

        menu.setOnClickListener { Navigation.findNavController(view).navigate(R.id.action_spese_to_dashboard3) }
        add_spese.setOnClickListener { Navigation.findNavController(view).navigate(R.id.action_spese_to_selectcar_addspesa) }

        spese_list.layoutManager = LinearLayoutManager(activity, LinearLayout.VERTICAL, false)


        btn_show_spese.setOnClickListener {
            val docutente = mStore.collection("Utenti").document("$id")
            val docucar = docutente.collection("Vettura")

            val mDialogView = LayoutInflater.from(activity).inflate(R.layout.showspesa_select, null)

            val mBuilder = AlertDialog.Builder(activity)
                .setView(mDialogView)

            val mAlertDialog = mBuilder.show()

            docutente.get()
                .addOnCompleteListener {
                    val famiglia = it.result!!.getString("Famiglia").toString()
                    mDialogView.recycler_show_spesa.layoutManager= LinearLayoutManager(activity, LinearLayout.VERTICAL, false)

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
                                mDialogView.recycler_show_spesa.adapter= ShowspeseAdapter(cars,mAlertDialog,txt_id,file, spese_list,context!!)
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
                                            mDialogView.recycler_show_spesa.adapter= ShowspeseAdapter(cars,mAlertDialog,txt_id,file, spese_list, context!!)

                                        }
                                }


                            }

                    }
                }



           // addRecycler()
        }

        btn_totale.setOnClickListener {
            showTotal()
        }

        btn_grafico.setOnClickListener {
            val auto = file.text.toString()
            val id = txt_id.text.toString()

            val info = Bundle()
            info.putString("File", "$auto")
            info.putString("Codice", "$id")


            Navigation.findNavController(view).navigate(R.id.action_spese_to_piechart,info)
        }


    }


    /*private fun addRecycler(){
        spese_list.layoutManager= LinearLayoutManager(activity, LinearLayout.VERTICAL, false)

        val filename_car = file.text.toString()
        val uid = txt_id.text.toString()

        val docutente = mStore.collection("Utenti").document("$uid")
        val docucar = docutente.collection("Vettura")

        val docuspese = docucar.document(filename_car).collection("Spese")

        docuspese.get()
            .addOnSuccessListener {
                val spese = ArrayList<expenses>()

                for(document in it){
                    val title = document.getString("Titolo").toString()
                    val desc= document.getString("Descrizione").toString()
                    val prezzo = document.getString("Prezzo").toString()

                    val spesa = expenses(title,desc,prezzo, filename_car,)

                    spese.add(spesa)
                }


                Log.d("Garage", "$filename_car $uid")
                spese_list.adapter= ExpensesAdapter(spese)

            }
    }*/

    private fun showTotal(){
        val filename_spese = file.text.toString()
        val uid = txt_id.text.toString()

        val docutente = mStore.collection("Utenti").document("$uid")
        val docucar = docutente.collection("Vettura")

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
                if (totale == "TextView") {
                    Toast.makeText(activity, "Il totale speso su quest'auto è: 0€", Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(activity, "Il totale speso su quest'auto è: $totale€", Toast.LENGTH_LONG).show()
                }
            }

    }

    private fun hide(){
        val imm: InputMethodManager = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
        //imm.hideSoftInputFromInputMethod(view?.windowToken, 0)
    }



}

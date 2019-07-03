package com.example.mycarsmanager


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_addspesa.*


class addspesa : Fragment() {

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
        return inflater.inflate(R.layout.fragment_addspesa, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addSpinnerAuto()
        addSpinnerTitle()

        spin_car_add?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                filename.text = parent?.selectedItem.toString()
            }

        }

        spinner_title_add?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                title.text =parent?.selectedItem.toString()
            }

        }

        btn_add_spesa.setOnClickListener {
            val title = title.text.toString()
            val desc = etxt_desc.text.toString()
            val prezzo = etxt_spesa.text.toString()
            val file = filename.text.toString()

            if (title.isEmpty() || desc.isEmpty() || prezzo.isEmpty()){
                Toast.makeText(activity,"Compila tutti i Campi.", Toast.LENGTH_SHORT).show()
            }

            val docuspesa = docucar.document(file).collection("Spese").document(title+desc+prezzo)

            val spesa = hashMapOf(
                "Titolo" to title,
                "Descrizione" to desc,
                "Prezzo" to prezzo
            )

            docuspesa.set(spesa as Map<String, Any>)

            Navigation.findNavController(view).navigate(R.id.action_addspesa_to_spese)

        }

        btn_cancel_spesa.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_addspesa_to_spese)
        }

    }

    private fun addSpinnerAuto(){
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

                spin_car_add?.adapter= adap

            }
    }

    private fun addSpinnerTitle(){
        val type = ArrayList<String>()

        type.add("Carburante")
        type.add("Meccanico")
        type.add("Carrozziere")
        type.add("Multe")
        type.add("Altro")

        val adapter_spese = ArrayAdapter(activity, android.R.layout.simple_spinner_item,type)

        adapter_spese.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner_title_add?.adapter=adapter_spese
    }


}

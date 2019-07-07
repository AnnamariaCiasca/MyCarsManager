package com.example.mycarsmanager


import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_piechart.*


class piechart : Fragment(), OnChartValueSelectedListener {



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

    var carburante = 0
    var meccanico = 0
    var carrozziere = 0
    var multe = 0
    var altro = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val file = arguments?.getString("File").toString()
        val docuspesa = docucar.document("$file").collection("Spese")

        docuspesa.get()
            .addOnSuccessListener {



                for(document in it){
                    val titolo = document.getString("Titolo").toString()
                    val prezzo = document.getString("Prezzo")!!.toInt()

                    when (titolo) {
                        "Carburante" -> carburante += prezzo
                        "Meccanico" -> meccanico += prezzo
                        "Carrozziere" -> carrozziere += prezzo
                        "Multe" -> multe += prezzo
                        "Altro" -> altro += prezzo
                    }

                    }

                val dati_torta = ArrayList<PieEntry>()
                if (carburante != 0) dati_torta.add(PieEntry(carburante.toFloat()))
                if (meccanico != 0) dati_torta.add(PieEntry(meccanico.toFloat()))
                if (carrozziere != 0) dati_torta.add(PieEntry(carrozziere.toFloat()))
                if (multe != 0) dati_torta.add(PieEntry(multe.toFloat()))
                if (altro != 0) dati_torta.add(PieEntry(altro.toFloat()))

                val dataSet = PieDataSet(dati_torta,"")

                val colori = ArrayList<Int>()
                if (carburante != 0) colori.add(Color.GREEN)
                if (meccanico != 0) colori.add(Color.BLUE)
                if (carrozziere != 0) colori.add(Color.RED)
                if (multe != 0) colori.add(Color.YELLOW)
                if (altro != 0) colori.add(Color.MAGENTA)
                dataSet.setColors(colori)


                val data = PieData(dataSet)

                data.setValueTextSize(14F)
                data.setValueTextColor(Color.BLACK)
                data.setValueFormatter(PercentFormatter(pichart_spese))


                pichart_spese.data = data

                pichart_spese.description.text="Nessuna Spesa Selezionata"
                pichart_spese.description.textSize = 18F
                pichart_spese.centerText = "Spese in €"
                pichart_spese.setCenterTextSize(18F)
                pichart_spese.legend.isEnabled = false

                pichart_spese.notifyDataSetChanged()
                pichart_spese.invalidate()

                }

                pichart_spese.setOnChartValueSelectedListener(this)




                btn_indietro.setOnClickListener {
                    Navigation.findNavController(view).navigate(R.id.action_piechart_to_spese)
                }

    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        Log.i("entry",e.toString())
        val prez = e!!.y
        if (prez==carburante.toFloat()) pichart_spese.description.text="Carburante \n €$prez"
        if (prez==meccanico.toFloat()) pichart_spese.description.text="Meccanico \n €$prez"
        if (prez==carrozziere.toFloat()) pichart_spese.description.text="Carrozziere \n €$prez"
        if (prez==multe.toFloat()) pichart_spese.description.text="Multe \n €$prez"
        if (prez==altro.toFloat()) pichart_spese.description.text="Altro \n €$prez"
    }

    override fun onNothingSelected() {
        pichart_spese.description.text="Nessuna Spesa Selezionata"
    }


}

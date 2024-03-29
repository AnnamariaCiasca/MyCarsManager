package com.example.mycarsmanager


import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import android.provider.CalendarContract.Events.*
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.delete_form.view.*
import kotlinx.android.synthetic.main.fragment_description_car.*
import kotlinx.android.synthetic.main.fragment_description_car.etxt_model
import kotlinx.android.synthetic.main.fragment_description_car.etxt_owner
import kotlinx.android.synthetic.main.fragment_description_car.etxt_targa
import kotlinx.android.synthetic.main.fragment_description_car.img_car
import java.text.SimpleDateFormat
import java.util.*


class description_car : Fragment() {

    private val mAuth = FirebaseAuth.getInstance()
    private val mStore = FirebaseFirestore.getInstance()
    private val mStorage = FirebaseStorage.getInstance()
    private val utente = mAuth?.currentUser
    private val id = utente?.uid
    private val c = Calendar.getInstance()
    private val year = c.get(Calendar.YEAR)
    private val month = c.get(Calendar.MONTH)
    private val day = c.get(Calendar.DAY_OF_MONTH)


    companion object{
        val Annomilli = 31540000000
    }

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
        val uid = arguments?.getString("Codice")
        val filename = "$model"+"_$owner"
        val docutente = mStore.collection("Utenti").document("$uid")
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
            showdialog(filename, view, uid)
        }

        btn_back.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_description_car_to_garage)
        }

        img_calend_boll.setOnClickListener {
            val mydata = etxt_bollo.text.toString()
            val parser = SimpleDateFormat("dd/MM/yyyy")
            val date = parser.parse(mydata)
            val millis = date.time + Annomilli

            val intent = Intent(Intent.ACTION_INSERT)
                .setData(CONTENT_URI)
                .putExtra(TITLE, "Scadenza Bollo")
                .putExtra(EVENT_LOCATION, "")
                .putExtra(ALL_DAY, true)
                .putExtra(DESCRIPTION, "Rinnova Bollo")
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, millis)
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, millis)
            startActivity(intent)
        }

        img_calend_rca.setOnClickListener {
            val mydata = etxt_rca.text.toString()
            val parser = SimpleDateFormat("dd/MM/yyyy")
            val date = parser.parse(mydata)
            val millis = date.time + Annomilli

            val intent = Intent(Intent.ACTION_INSERT)
                .setData(CONTENT_URI)
                .putExtra(TITLE, "Scadenza Assicurazione")
                .putExtra(EVENT_LOCATION, "")
                .putExtra(ALL_DAY, true)
                .putExtra(DESCRIPTION, "Rinnova Assicurazione")
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, millis)
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, millis)
            startActivity(intent)
        }

        img_calend_rev.setOnClickListener {
            val mydata = etxt_rev.text.toString()
            val parser = SimpleDateFormat("dd/MM/yyyy")
            val date = parser.parse(mydata)
            val millis = date.time + Annomilli


            val intent = Intent(Intent.ACTION_INSERT)
                .setData(CONTENT_URI)
                .putExtra(TITLE, "Scadenza Revisione")
                .putExtra(EVENT_LOCATION, "")
                .putExtra(ALL_DAY, true)
                .putExtra(DESCRIPTION, "Effettua Revisione")
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, millis)
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, millis)
            startActivity(intent)
        }

        etxt_bollo.setOnClickListener {
            val dpd = DatePickerDialog(context!!,
                DatePickerDialog.OnDateSetListener { _, mYear, mMonth, mDay ->
                    etxt_bollo.text = (""+mDay+"/"+mMonth+"/"+mYear)}, year, month, day)

            dpd.show()
        }

        etxt_rca.setOnClickListener {
            val dpd = DatePickerDialog(context!!,
                DatePickerDialog.OnDateSetListener { _, mYear, mMonth, mDay ->
                    etxt_rca.text = (""+mDay+"/"+mMonth+"/"+mYear)}, year, month, day)

            dpd.show()
        }

        etxt_rev.setOnClickListener {
            val dpd = DatePickerDialog(context!!,
                DatePickerDialog.OnDateSetListener { _, mYear, mMonth, mDay ->
                    etxt_rev.text = (""+mDay+"/"+mMonth+"/"+mYear)}, year, month, day)

            dpd.show()
        }

        btn_change.setOnClickListener {
            val scadRCA = etxt_rca.text.toString()
            val scadBollo = etxt_bollo.text.toString()
            val lastRev = etxt_rev.text.toString()

            val Vettura = hashMapOf(
                "Scadenza RCA" to scadRCA,
                "Scadenza Bollo" to scadBollo,
                "Ultima Rev" to lastRev
            )

            docucar.update(Vettura as Map<String, Any>)
        }

    }

    private fun showdialog(filename: String, view: View, uid: String?) {
        val mDialogView = LayoutInflater.from(activity).inflate(R.layout.delete_form, null)

        val mBuilder = AlertDialog.Builder(activity)
            .setView(mDialogView)

        val mAlertDialog = mBuilder.show()

        val model = txt_macchina.text.toString()
        mDialogView.txt_delete.text = "Sei sicuro di voler eliminare $model ?"

        mDialogView.btn_cancel_delete.setOnClickListener {
            mAlertDialog.dismiss()
        }

        mDialogView.btn_accept_delete.setOnClickListener {
            mAlertDialog.dismiss()
            if (uid==id){
                cancella(filename, view, uid)
            }else{
                Toast.makeText(context!!,"Non puoi eliminare una macchina non creata da te.", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun cancella(file: String, view: View, uid: String?){
        val docutente = mStore.collection("Utenti").document("$uid")
        val docucar = docutente.collection("Vettura").document(file)
        val imgcar = mStorage.getReference("/img_car/$file")
        imgcar.delete()
            .addOnSuccessListener {
                Log.d("Desc", "Immagine eliminata!")
            }
        docucar.delete()
            .addOnSuccessListener {
                Navigation.findNavController(view).navigate(R.id.action_description_car_to_garage)
            }

    }


}

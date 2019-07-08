package com.example.mycarsmanager


import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_addcar.*
import java.util.*


class addcar : Fragment() {

    private val mAuth = FirebaseAuth.getInstance()
    private val mStore = FirebaseFirestore.getInstance()
    private val mStorage = FirebaseStorage.getInstance()
    private val utente = mAuth?.currentUser
    private val id = utente?.uid
    private val docutente = mStore.collection("Utenti").document("$id")
    private val docucar = docutente.collection("Vettura")
    private var imgURI: Uri? =null
    private val c = Calendar.getInstance()
    private val year = c.get(Calendar.YEAR)
    private val month = c.get(Calendar.MONTH)
    private val day = c.get(Calendar.DAY_OF_MONTH)


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

            if(txt_img.alpha == 0f) {


                val modello = etxt_model.text.toString()
                val owner = etxt_owner.text.toString()
                val targa = etxt_targa.text.toString()
                val scadRCA = txt_rca_add.text.toString()
                val scadBollo = txt_bollo_add.text.toString()
                val lastRev = txt_rev_add.text.toString()

                val filename = modello + "_" + owner

                val Vettura = hashMapOf(
                    "Modello" to modello,
                    "Owner" to owner,
                    "Targa" to targa,
                    "Scadenza RCA" to scadRCA,
                    "Scadenza Bollo" to scadBollo,
                    "Ultima Rev" to lastRev
                )

                val doc_name = docucar.document("$filename")

                doc_name.set(Vettura as Map<String, Any>)

                caricafoto()

                Navigation.findNavController(view).navigate(R.id.action_addcar_to_garage)

            }else{
                Toast.makeText(activity, "Inserisci fotografia", Toast.LENGTH_SHORT).show()
            }

        }

        btn_cancel.setOnClickListener { Navigation.findNavController(view).navigate(R.id.action_addcar_to_garage) }

        txt_rca_add.setOnClickListener {
            val dpd = DatePickerDialog(context!!,DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                txt_rca_add.text = (""+mDay+"/"+mMonth+"/"+mYear)}, year, month, day)

            dpd.show()
        }

        txt_bollo_add.setOnClickListener {
            val dpd = DatePickerDialog(context!!,DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                txt_bollo_add.text = (""+mDay+"/"+mMonth+"/"+mYear)}, year, month, day)

            dpd.show()
        }

        txt_rev_add.setOnClickListener {
            val dpd = DatePickerDialog(context!!, DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                txt_rev_add.text = (""+mDay+"/"+mMonth+"/"+mYear)}, year, month, day)

            dpd.show()
        }

        img_car.setOnClickListener {
                selectphoto()
        }
    }

    private fun selectphoto(){
        val galleryIntent = Intent(Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == 1){
            if (data != null){
                imgURI = data.data
                val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, imgURI)
                img_car.setImageBitmap(bitmap)
                txt_img.alpha=0f

            }
        }
    }

    private fun caricafoto(){
        val progressDialog = ProgressDialog(activity)
        progressDialog.setTitle("Caricamento")
        progressDialog.setMessage("Caricamento Immagine in corso...")
        progressDialog.setCancelable(false)

        val modello = etxt_model.text.toString()
        val owner = etxt_owner.text.toString()

        val filename = "$modello"+"_$owner"

        val ref = mStorage.getReference("/img_car/$filename")

        ref.putFile(imgURI!!)
            .addOnProgressListener {
                progressDialog.show()
            }
            .addOnCompleteListener {
                progressDialog.dismiss()

            }
            .addOnSuccessListener {
                val result = it.metadata!!.reference!!.downloadUrl
                result.addOnSuccessListener {
                    val link= it.toString()

                    val urlimg = hashMapOf(
                        "img_url" to "$link"
                    )

                    val doc_name = docucar.document("$filename")

                    doc_name.update(urlimg as Map<String, Any>)

                }
            }

    }



}

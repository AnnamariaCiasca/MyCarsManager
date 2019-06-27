package com.example.mycarsmanager


import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_account.*



/**
 * A simple [Fragment] subclass.
 *
 */
class account : Fragment() {

    private val mAuth = FirebaseAuth.getInstance()
    private val mStore = FirebaseFirestore.getInstance()
    private val mStorage = FirebaseStorage.getInstance()
    private val utente = mAuth?.currentUser
    private val id = utente?.uid
    private val docutente = mStore.collection("Utenti").document("$id")
    private val filename= "img_profile_$id"
    private val ref = mStorage.getReference("/img_profile/$filename")
    private var imgURI: Uri? =null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        docutente.get()
            .addOnCompleteListener {
                if (it.result?.exists()!!) {
                    val username = it.result?.getString("Nome").toString()
                    val phone = it.result?.getString("Telefono").toString()
                    val photo = it.result?.getString("img_url").toString()
                    val mail = it.result?.getString("Email").toString()

                    if ( photo != ""){
                        //download e carico foto
                        downloadphoto(photo)
                        img_profile.alpha=0f
                    }

                    etxt_mail.setText(mail)
                    etxt_name.setText(username)
                    etxt_phone.setText(phone)
                } else {
                    val mail = utente?.email

                    val user = hashMapOf(
                        "Email" to "$mail",
                        "Nome" to "",
                        "Telefono" to "",
                        "img_url" to ""
                    )
                    docutente.set(user as Map<String, Any>)
                    etxt_mail.setText(mail)
                }
            }

        menu3.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_account_to_dashboard3)
        }
        btn_out.setOnClickListener {
            mAuth.signOut()
            Navigation.findNavController(view).navigate(R.id.action_account_to_login2)
        }
        btn_change.setOnClickListener {
            updateinfo()
        }

        img_profile.setOnClickListener {
            selectphoto()
        }
    }

    private fun updateinfo(){
        val username = etxt_name.text.toString()
        val phone = etxt_phone.text.toString()
        val user = hashMapOf(
            "Nome" to "$username",
            "Telefono" to "$phone"
        )
        docutente.update(user as Map<String, Any>)
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
                profile_round.setImageBitmap(bitmap)
                img_profile.alpha=0f
                caricafoto()
                }
        }
    }

    private fun caricafoto(){
        val progressDialog = ProgressDialog(activity)
        progressDialog.setTitle("Caricamento")
        progressDialog.setMessage("Caricamento Immagine in corso...")
        progressDialog.setCancelable(false)



        ref.putFile(imgURI!!)
            .addOnProgressListener {
                progressDialog.show()
            }
            .addOnCompleteListener {
                progressDialog.dismiss()
            }
            .addOnSuccessListener {
                val  result = it.metadata!!.reference!!.downloadUrl
                result.addOnSuccessListener {
                    val link = it.toString()

                    val urlimg = hashMapOf(
                        "img_url" to "$link"
                    )

                    docutente.update(urlimg as Map<String, Any>)
                }

            }

    }

    private fun downloadphoto(url:String){
        Picasso.with(activity).load(url).into(profile_round)
    }

}
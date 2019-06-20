package com.example.mycarsmanager


import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
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
    private val mail = utente?.email
    private val docutente = mStore.collection("Utenti").document("$id")


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

                    etxt_name.setText(username)
                    etxt_phone.setText(phone)
                } else {
                    val user = hashMapOf(
                        "Email" to "$mail",
                        "Nome" to "",
                        "Telefono" to ""
                    )
                    docutente.set(user as Map<String, Any>)
                }
            }
        etxt_mail.setText(mail)

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
            showDialogPic()
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

    private fun showDialogPic(){
        val dialog = Dialog(activity)
        dialog.setContentView(R.layout.dialog_camera_layout)
        dialog.show()

        val cancel = dialog.findViewById<Button>(R.id.btn_cancel)
        val gall = dialog.findViewById<Button>(R.id.btn_gallery)
        val cam = dialog.findViewById<Button>(R.id.btn_camera)

        cancel.setOnClickListener { dialog.dismiss() }

        gall.setOnClickListener {  }

        cam.setOnClickListener {  }

        }

}
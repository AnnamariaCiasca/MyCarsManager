package com.example.mycarsmanager


import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_registro.*

/**
 * A simple [Fragment] subclass.
 *
 */
class registro : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registro, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        menu.setOnClickListener { Navigation.findNavController(view).navigate(R.id.action_registro_to_dashboard3) }
        txt_already.setOnClickListener { Navigation.findNavController(view).navigate(R.id.action_registro_to_login) }

        btn_registra.setOnClickListener {
            registra(view)
        }

    }

    private fun registra(view: View){

        val email = etxt_mail.text.toString()
        val psw = etxt_psw.text.toString()

        if (email.isEmpty() || psw.isEmpty()){
            Toast.makeText(activity, "Inserire E-mail e Password", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("Main", "E-mail: $email")
        Log.d("Main", "Password: $psw")

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, psw)
            .addOnCompleteListener {
                if (!it.isSuccessful){
                    return@addOnCompleteListener
                }
                it.result?.user?.sendEmailVerification()
                Navigation.findNavController(view).navigate(R.id.action_registro_to_login)
            }
            .addOnFailureListener {
                Toast.makeText(activity, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
            }

    }

}

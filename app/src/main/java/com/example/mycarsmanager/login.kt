package com.example.mycarsmanager


import android.app.ProgressDialog
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.etxt_mail


class login : Fragment() {

    private val mStore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        menu2.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_login_to_dashboard3)
        }

        btn_login.setOnClickListener {

            entra(view)
        }

        txt_regist.setOnClickListener { Navigation.findNavController(view).navigate(R.id.action_login_to_registro) }
    }

    private fun entra(view: View){
        val email = etxt_mail.text.toString()
        val psw = etxt_pass.text.toString()

        if (email.isEmpty() || psw.isEmpty()){
            Toast.makeText(activity, "Inserire E-mail e Password", Toast.LENGTH_SHORT).show()
            return
        }

        showDialog()

        Log.d("Main", "E-mail: $email")
        Log.d("Main", "Password: $psw")

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, psw)
            .addOnSuccessListener {
                val acc = it.user?.isEmailVerified
                if (acc == false) {
                    Toast.makeText(activity, "Error: Verify mail", Toast.LENGTH_SHORT).show()
                    FirebaseAuth.getInstance().signOut()
                }else{
                    Navigation.findNavController(view).navigate(R.id.action_login_to_account)
                }
            }
            .addOnFailureListener {
                Toast.makeText(activity, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun showDialog(){
        val progressDialog = ProgressDialog(activity)
        progressDialog.setTitle("Login")
        progressDialog.setMessage("Login in corso...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        Handler().postDelayed({progressDialog.dismiss()}, 2000)
    }

}

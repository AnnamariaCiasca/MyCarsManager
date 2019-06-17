package com.example.mycarsmanager

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_dashboard.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 */
class dashboard : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        btn_cerca.setOnClickListener {Navigation.findNavController(view).navigate(R.id.action_dashboard3_to_cerca)}
        btn_park.setOnClickListener {Navigation.findNavController(view).navigate(R.id.action_dashboard3_to_park)}
        btn_service.setOnClickListener {Navigation.findNavController(view).navigate(R.id.action_dashboard3_to_service)}
        btn_security.setOnClickListener {Navigation.findNavController(view).navigate(R.id.action_dashboard3_to_security)}
        btn_option.setOnClickListener {Navigation.findNavController(view).navigate(R.id.action_dashboard3_to_option)}


        if(FirebaseAuth.getInstance().currentUser != null){
            btn_account.setOnClickListener{Navigation.findNavController(view).navigate(R.id.action_dashboard3_to_account)}
            btn_garage.setOnClickListener {Navigation.findNavController(view).navigate(R.id.action_dashboard3_to_garage)}
            btn_spese.setOnClickListener {Navigation.findNavController(view).navigate(R.id.action_dashboard3_to_spese)}
        }else{
            btn_account.setOnClickListener{Navigation.findNavController(view).navigate(R.id.action_dashboard3_to_registro)}
            btn_garage.setOnClickListener {Toast.makeText(activity, "Per usare questa funzionalità effettuare il Login", Toast.LENGTH_SHORT).show()}
            btn_spese.setOnClickListener {Toast.makeText(activity, "Per usare questa funzionalità effettuare il Login", Toast.LENGTH_SHORT).show()}
        }

    }
}

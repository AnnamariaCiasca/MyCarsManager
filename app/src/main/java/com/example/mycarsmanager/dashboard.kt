package com.example.mycarsmanager

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
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
        account2.setOnClickListener{Navigation.findNavController(view).navigate(R.id.action_dashboard3_to_account)}
        garage2.setOnClickListener {Navigation.findNavController(view).navigate(R.id.action_dashboard3_to_garage)}
        spese2.setOnClickListener {Navigation.findNavController(view).navigate(R.id.action_dashboard3_to_spese)}
        cerca2.setOnClickListener {Navigation.findNavController(view).navigate(R.id.action_dashboard3_to_cerca)}
        park2.setOnClickListener {Navigation.findNavController(view).navigate(R.id.action_dashboard3_to_park)}
        service2.setOnClickListener {Navigation.findNavController(view).navigate(R.id.action_dashboard3_to_service)}
        security2.setOnClickListener {Navigation.findNavController(view).navigate(R.id.action_dashboard3_to_security)}
        options2.setOnClickListener {Navigation.findNavController(view).navigate(R.id.action_dashboard3_to_option)}
    }
}

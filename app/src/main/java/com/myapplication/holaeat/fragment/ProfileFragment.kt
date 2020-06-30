package com.myapplication.holaeat.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.myapplication.holaeat.R
import com.myapplication.holaeat.activity.RegisterYourselfActivity


class ProfileFragment : Fragment() {

    lateinit var txtUserName : TextView
    lateinit var txtUserMobile : TextView
    lateinit var txtUserEmail : TextView
    lateinit var txtUserAddress : TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)



        txtUserName = view.findViewById(R.id.txtUserName)
        txtUserMobile = view.findViewById(R.id.txtUserMobile)
        txtUserEmail = view.findViewById(R.id.txtUserEmail)
        txtUserAddress = view.findViewById(R.id.txtUserAddress)

        var sharedPrefs: SharedPreferences =
            (activity as FragmentActivity).getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)


                txtUserName.text = sharedPrefs.getString("user_name", null)
                val phoneText = "+91-${sharedPrefs.getString("user_mobile_number", null)}"
                txtUserMobile.text = phoneText
                txtUserEmail.text = sharedPrefs.getString("user_email", null)
                val address = sharedPrefs.getString("user_address", null)
                txtUserAddress.text = address
                return view

            }




}


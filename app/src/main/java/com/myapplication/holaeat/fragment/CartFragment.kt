package com.myapplication.holaeat.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.myapplication.holaeat.R
import com.myapplication.holaeat.activity.PlaceOrderActivity
import com.myapplication.holaeat.adapter.CartItemAdapter
import com.myapplication.holaeat.database.CartDatabase
import com.myapplication.holaeat.database.CartEntity
import org.json.JSONArray
import org.json.JSONObject

class CartFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_cart, container, false)

        return view

    }
}
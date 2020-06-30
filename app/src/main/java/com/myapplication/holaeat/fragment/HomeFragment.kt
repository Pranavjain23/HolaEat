package com.myapplication.holaeat.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.myapplication.holaeat.R
import com.myapplication.holaeat.adapter.HomeRecyclerAdapter
import com.myapplication.holaeat.model.Restaurants
import com.myapplication.holaeat.util.ConnectionManager
import org.json.JSONException

class HomeFragment : Fragment() {

    lateinit var recyclerHome : RecyclerView

    lateinit var layoutManager : RecyclerView.LayoutManager

    lateinit var recyclerAdapter: HomeRecyclerAdapter

    lateinit var progressLayout : RelativeLayout
    lateinit var progressBar: ProgressBar


    val restaurantsInfoList = arrayListOf<Restaurants>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        setHasOptionsMenu(true)

        recyclerHome = view.findViewById(R.id.recyclerHome)

        progressLayout = view.findViewById(R.id.progressLayout)
        progressBar = view.findViewById(R.id.progressBar)

        progressLayout.visibility = View.VISIBLE

        layoutManager = LinearLayoutManager(activity)


        val queue = Volley.newRequestQueue(activity as Context)

        val url = "http://13.235.250.119/v2/Restaurants/fetch_result/"

        if(ConnectionManager().checkConnectivity(activity as Context)){
            val jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET,url,null, Response.Listener {response ->

                try{
                    progressLayout.visibility = View.GONE
                    //here we will handle the response

                    val data = response.getJSONObject("data")
                    val success = data.getBoolean("success")

                    if(success){
                        val resArray = data.getJSONArray("data")
                        for(i in 0 until resArray.length()){
                            val resObject = resArray.getJSONObject(i)
                            val restaurant = Restaurants(
                                resObject.getString("id").toInt(),
                                resObject.getString("name"),
                                resObject.getString("rating"),
                                resObject.getString("cost_for_two").toInt(),
                                resObject.getString("image_url")
                            )
                            restaurantsInfoList.add(restaurant)
                            recyclerAdapter = HomeRecyclerAdapter(activity as Context, restaurantsInfoList)

                            recyclerHome.adapter = recyclerAdapter

                            recyclerHome.layoutManager = layoutManager


                        }
                    } else{
                        Toast.makeText(activity as Context, "Some Error has occured!!", Toast.LENGTH_SHORT).show()
                    }
                } catch(e: JSONException){
                    Toast.makeText(activity as Context,"Some Unexpected error occurred!!!", Toast.LENGTH_SHORT).show()
                }

            }, Response.ErrorListener {

                //here we will handle the response
                if (activity != null) {

                    Toast.makeText(
                        activity as Context,
                        "Volley error occurred!!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }){
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String,String>()
                    headers["Content-Type"] = "application/json"
                    headers["token"] = "d8fe3166127045"
                    return headers
                }

            }

            queue.add(jsonObjectRequest)
        }else{
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection Not Found")
            dialog.setPositiveButton("Open Settings"){ text,listener ->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                activity?.finish()
            }

            dialog.setNegativeButton("Cancel"){ text, listener ->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()
        }


        return view

    }


}
package com.myapplication.holaeat.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.myapplication.holaeat.R
import com.myapplication.holaeat.activity.CartActivity
import com.myapplication.holaeat.adapter.HomeRecyclerAdapter
import com.myapplication.holaeat.adapter.RestaurantMenuAdapter
import com.myapplication.holaeat.database.FoodDatabase
import com.myapplication.holaeat.database.OrderEntity
import com.myapplication.holaeat.model.FoodItem
import com.myapplication.holaeat.model.Restaurants
import com.myapplication.holaeat.util.ConnectionManager
import kotlinx.android.synthetic.main.fragment_menu.*
import org.json.JSONException

class MenuFragment : Fragment() {


    lateinit var recyclerMenu : RecyclerView

    lateinit var layoutManager : RecyclerView.LayoutManager

    lateinit var recyclerAdapter: RestaurantMenuAdapter
    private var orderList = arrayListOf<FoodItem>()
    lateinit var progressLayout : RelativeLayout
    lateinit var progressBar: ProgressBar
    val restaurantsMenuList = arrayListOf<FoodItem>()


    lateinit var sharedPreferences: SharedPreferences
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var goToCart: Button
        var resId: Int? = 0
        var resName: String? = ""
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_menu, container, false)

     //New Code
        sharedPreferences =
            activity?.getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE) as SharedPreferences
        progressLayout = view?.findViewById(R.id.progressLayout) as RelativeLayout
        progressLayout.visibility = View.VISIBLE
        resId = arguments?.getInt("id", 0)
        resName = arguments?.getString("name", "")
  //Part1



            setHasOptionsMenu(true)


//Newcode2
        goToCart = view.findViewById(R.id.btnGoToCart) as Button
        goToCart.visibility = View.GONE
        goToCart.setOnClickListener {
            proceedToCart()
        }


            recyclerMenu = view.findViewById(R.id.recyclerMenu)

            progressLayout = view.findViewById(R.id.progressLayout)
            progressBar = view.findViewById(R.id.progressBar)

            progressLayout.visibility = View.VISIBLE

            layoutManager = LinearLayoutManager(activity)

            val resId = arguments?.getInt("id",0)
            val resName = arguments?.getString("name")
            println(resId)
            println(resName)

             var str:String = resId.toString()

            val queue = Volley.newRequestQueue(activity as Context)

            val url = "http://13.235.250.119/v2/restaurants/fetch_result/$str"

            if(ConnectionManager().checkConnectivity(activity as Context)){
                val jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET,url,null, Response.Listener { response ->

                    try{
                        progressLayout.visibility = View.GONE
                        //here we will handle the response

                        val data = response.getJSONObject("data")
                        val success = data.getBoolean("success")

                        if(success){
                            val menuArray = data.getJSONArray("data")
                            for(i in 0 until menuArray.length()){
                                val menuObject = menuArray.getJSONObject(i)
                                val menuItem = FoodItem(
                                    menuObject.getString("id").toInt(),
                                    menuObject.getString("name"),
                                    menuObject.getString("cost_for_one").toInt()
                                )
                                restaurantsMenuList.add(menuItem)
                                recyclerAdapter = RestaurantMenuAdapter(activity as Context,restaurantsMenuList, object : RestaurantMenuAdapter.OnItemClickListener {
                                    override fun onAddItemClick(foodItem: FoodItem) {
                                        orderList.add(foodItem)
                                        if (orderList.size > 0) {
                                            goToCart.visibility = View.VISIBLE
                                            RestaurantMenuAdapter.isCartEmpty = false
                                        }
                                    }


                                    override fun onRemoveItemClick(foodItem: FoodItem) {
                                        orderList.remove(foodItem)
                                        if (orderList.isEmpty()) {
                                            goToCart.visibility = View.GONE
                                            RestaurantMenuAdapter.isCartEmpty = true
                                        }
                                    }
                                })

                                recyclerMenu.adapter = recyclerAdapter

                                recyclerMenu.layoutManager = layoutManager


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

    private fun proceedToCart() {

        /*Here we see the implementation of Gson.
        * Whenever we want to convert the custom data types into simple data types
        * which can be transferred across for utility purposes, we will use Gson*/
        val gson = Gson()

        /*With the below code, we convert the list of order items into simple string which can be easily stored in DB*/
        val foodItems = gson.toJson(orderList)

        val async = ItemsOfCart(activity as Context, resId.toString(), foodItems, 1).execute()
        val result = async.get()
        if (result) {
            val data = Bundle()
            data.putInt("resId", resId as Int)
            data.putString("resName", resName)
            val intent = Intent(activity, CartActivity::class.java)
            intent.putExtra("data", data)
            startActivity(intent)
        } else {
            Toast.makeText((activity as Context), "Some unexpected error", Toast.LENGTH_SHORT)
                .show()
        }

    }


    class ItemsOfCart(
        context: Context,
        val restaurantId: String,
        val foodItems: String,
        val mode: Int
    ) : AsyncTask<Void, Void, Boolean>() {
        val db = Room.databaseBuilder(context, FoodDatabase::class.java, "res-db").build()


        override fun doInBackground(vararg params: Void?): Boolean {
            when (mode) {
                1 -> {
                    db.orderDao().insertOrder(OrderEntity(restaurantId, foodItems))
                    db.close()
                    return true
                }

                2 -> {
                    db.orderDao().deleteOrder(OrderEntity(restaurantId, foodItems))
                    db.close()
                    return true
                }
            }

            return false
        }

    }

}







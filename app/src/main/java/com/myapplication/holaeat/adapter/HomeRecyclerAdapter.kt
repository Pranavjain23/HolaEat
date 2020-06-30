package com.myapplication.holaeat.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.myapplication.holaeat.R
import com.myapplication.holaeat.activity.MainActivity
import com.myapplication.holaeat.fragment.HomeFragment
import com.myapplication.holaeat.fragment.MenuFragment
import com.myapplication.holaeat.fragment.ProfileFragment
import com.myapplication.holaeat.model.Restaurants
import com.squareup.picasso.Picasso

class HomeRecyclerAdapter(val context: Context, val itemList: ArrayList<Restaurants>) : RecyclerView.Adapter<HomeRecyclerAdapter.HomeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_home_single_row,parent,false)

        return HomeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size

    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val restaurants = itemList[position]

        holder.txtRestaurantName.text = restaurants.restaurantsName
        holder.txtRestaurantRating.text = restaurants.restaurantsRating
        holder.txtRestaurantPrice.text = restaurants.restaurantsPrice.toString()
        Picasso.get().load(restaurants.restaurantsImage).error(R.drawable.user).into(holder.imgRestaurantImage);


        holder.llContent.setOnClickListener{
            val fragment = MenuFragment()
            val args = Bundle()
            args.putInt("id",restaurants.restaurantsId)
            args.putString("name",restaurants.restaurantsName)
            fragment.arguments = args
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(
                    R.id.frame,
                    fragment
                ).commit()
        }
    }

    class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view){

        val imgRestaurantImage: ImageView = view.findViewById(R.id.imgRestaurantImage)
        val txtRestaurantName: TextView = view.findViewById(R.id.txtRestaurantName)
        val txtRestaurantPrice: TextView = view.findViewById(R.id.txtRestaurantPrice)
        val txtRestaurantFav: ImageView = view.findViewById(R.id.txtRestaurantFav)
        val txtRestaurantRating: TextView = view.findViewById(R.id.txtRestaurantRating)
        val llContent: LinearLayout = view.findViewById(R.id.llContent)
    }




}
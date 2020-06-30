package com.myapplication.holaeat.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="Food")
data class FoodEntity(
    @PrimaryKey val foodId:Int,
    @ColumnInfo(name = "name") val foodName:String,
    @ColumnInfo(name = "rating") val foodRating:String,
    @ColumnInfo(name = "cost_for_two") val foodPrice: String,
    @ColumnInfo(name = "image_url") val foodImage:String
)
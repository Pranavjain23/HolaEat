package com.myapplication.holaeat.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FoodDao {
    @Insert
    fun insertFood(foodEntity: FoodEntity)

    @Delete
    fun deleteFood(foodEntity: FoodEntity)

    @Query("SELECT * FROM Food")
    fun getAllFood():List<FoodEntity>

    @Query("SELECT * FROM Food WHERE foodId=:foodId")
    fun getFoodById(foodId:String):FoodEntity
}


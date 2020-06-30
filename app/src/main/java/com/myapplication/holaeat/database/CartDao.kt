package com.myapplication.holaeat.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CartDao {
    @Insert
    fun insertCart(vararg item: CartEntity)

    @Delete
    fun deleteFood(vararg item: CartEntity)

    @Query("SELECT * FROM carts")
    fun getAllFood():List<CartEntity>

}
package com.myapplication.holaeat.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities =[CartEntity::class,FoodEntity::class],version = 1)
abstract class CartDatabase: RoomDatabase() {

    abstract fun CartDao(): CartDao
    abstract fun FoodDao(): FoodDao
}

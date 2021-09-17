package com.example.foodwastemanagmentapp.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ModelClasses::class], version = 1, exportSchema = false)
abstract class ApplicationDatabase: RoomDatabase() {

    abstract fun itemDao(): Dao

    companion object {
        var INSTANCE:ApplicationDatabase? = null
        fun getDatabase(context: Context): ApplicationDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, ApplicationDatabase::class.java,"item_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance


            }
        }
    }


}
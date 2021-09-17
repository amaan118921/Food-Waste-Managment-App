package com.example.foodwastemanagmentapp.room

import androidx.room.*
import androidx.room.Dao


@Dao
interface Dao {

    @Query("SELECT * FROM MODELCLASSES ORDER BY title ASC")
    fun getAllData(): kotlinx.coroutines.flow.Flow<MutableList<ModelClasses>>

@Insert
suspend fun insert(data: ModelClasses)

@Delete
suspend fun delete(data: ModelClasses)

@Update
suspend fun update(data: ModelClasses)

}
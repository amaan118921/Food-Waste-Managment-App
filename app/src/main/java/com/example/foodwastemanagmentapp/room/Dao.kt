package com.example.foodwastemanagmentapp.room

import androidx.room.*
import androidx.room.Dao


@Dao
interface Dao {

    @Query("SELECT * FROM MODELCLASSES ORDER BY title ASC")
    fun getAllData(): kotlinx.coroutines.flow.Flow<MutableList<ModelClasses>>

    @Query("SELECT * FROM USERINFO")
    fun getUser(): List<UserInfo>

    @Query("SELECT * FROM NGOINFO")
    fun getNgoUser(): List<NgoInfo>

    @Insert
    suspend fun insertUser(user: UserInfo)

@Insert
suspend fun insert(data: ModelClasses)

@Delete
suspend fun delete(data: ModelClasses)

@Insert
suspend fun insertNgoUser(ngo: NgoInfo)

@Update
suspend fun update(data: ModelClasses)

}
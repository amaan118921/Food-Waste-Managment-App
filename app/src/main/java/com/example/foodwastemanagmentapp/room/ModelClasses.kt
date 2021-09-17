package com.example.foodwastemanagmentapp.room

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ModelClasses (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo (name = "title") @NonNull val title: String = "",
    @ColumnInfo(name = "address") @NonNull val address: String = "",
    @ColumnInfo(name="description")@NonNull val desc: String = "",
    @ColumnInfo(name="date")@NonNull val date: String = ""
        )

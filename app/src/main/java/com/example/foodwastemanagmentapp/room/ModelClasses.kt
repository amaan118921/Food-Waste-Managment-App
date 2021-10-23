package com.example.foodwastemanagmentapp.room

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ModelClasses (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name="userId") val userId: String = "",
    @ColumnInfo (name = "title") @NonNull val title: String = "",
    @ColumnInfo(name = "address") @NonNull val address: String = "",
    @ColumnInfo(name="description")@NonNull val desc: String = "",
    @ColumnInfo(name="date")@NonNull val date: String = "",
    @ColumnInfo(name="contact")@NonNull val contact: String = "",
    @ColumnInfo(name="status")@NonNull val status: String ="",
    @ColumnInfo(name="lat")@NonNull val lat: String = "",
    @ColumnInfo(name="lon")@NonNull val lon: String = "",
    @ColumnInfo(name="bookmark")@NonNull val bookMark: Int = 0

        )

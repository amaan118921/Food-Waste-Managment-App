package com.example.foodwastemanagmentapp.room

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserInfo(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "name") @NonNull val name: String = "",
    @ColumnInfo(name="phone")@NonNull val phone: String = "",
    @ColumnInfo(name = "uri")@NonNull val uri: String = "",
    @ColumnInfo(name="email") @NonNull val email: String = "",
    @ColumnInfo(name="unique")@NonNull val unique: String =""
)
package com.example.foodwastemanagmentapp.application

import android.app.Application
import com.example.foodwastemanagmentapp.room.ApplicationDatabase

class MyApplication: Application() {
val database: ApplicationDatabase by lazy { ApplicationDatabase.getDatabase(this) }
}
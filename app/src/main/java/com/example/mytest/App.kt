package com.example.mytest

import android.app.Application
import androidx.room.Room
import com.example.mytest.room.MainDB

class App: Application() {
    lateinit var database: MainDB
        private set

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            this,
            MainDB::class.java,
            "database-for-saved-tests"
        ).build()
    }
}
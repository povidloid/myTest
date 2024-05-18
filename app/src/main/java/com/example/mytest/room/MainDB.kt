package com.example.mytest.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database (entities = [TestEntity::class], version = 1)
abstract class MainDB : RoomDatabase(){
    abstract fun dao(): Dao

}
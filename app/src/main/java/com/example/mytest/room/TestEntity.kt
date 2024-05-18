package com.example.mytest.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mytest.screens.QuestionAndAnswers

@Entity(tableName = "SavedTests")
data class TestEntity(
    @PrimaryKey (autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo (name = "testTittle")
    val tittle: String,
    @ColumnInfo (name = "creationDate")
    val creationDate: String,
    @ColumnInfo (name = "testBody")
    val testBody: String
)
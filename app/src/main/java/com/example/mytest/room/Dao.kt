package com.example.mytest.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {
    @Insert
    suspend fun insertTest(testEntity: TestEntity)
    @Query ("SELECT * FROM savedTests")
    fun getAllTests(): Flow<List<TestEntity>>

}
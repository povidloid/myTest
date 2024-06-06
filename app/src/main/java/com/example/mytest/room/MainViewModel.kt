package com.example.mytest.room

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytest.App
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val dao: Dao = (application as App).database.dao()
    lateinit var tests: Flow<List<TestEntity>>

    fun insertTest(test: TestEntity) {
        viewModelScope.launch {
            dao.insertTest(test)
            loadTests()
        }
    }

    fun loadTests() {
        viewModelScope.launch {
            tests = dao.getAllTests()
        }
    }

    fun clearDatabase() {
        viewModelScope.launch {
            dao.clearDataBase()
            try {
                tests = dao.getAllTests()
            } catch (e: Exception) {
                Log.d("myTag", e.toString())
            }
        }
    }
}

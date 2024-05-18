package com.example.mytest.room

import android.app.Application
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.mytest.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(application: Application): AndroidViewModel(application) {
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

    fun clearDatabase(){
        viewModelScope.launch {
            dao.clearDataBase()
            try {
                tests = dao.getAllTests()
            }catch (e: Exception) {
                Log.d("myTag", e.toString())
            }
        }
    }
}

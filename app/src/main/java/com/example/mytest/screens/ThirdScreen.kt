package com.example.mytest.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.LinkedList
import java.util.Queue

data class User(
    val id: Int,
    val username: String,
    val password: String,
    val email: String
)

interface MainAPI {
    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: Int): User
}

@Composable
fun ThirdScreen() {
    SearchModule()
}

class ViewModelSearchText : ViewModel() {
    val searchText = mutableStateOf("")
}

object searchHistory {
    var queue: Queue<String> = LinkedList()
    fun addToQueue(num: String) {
        if (queue.size == 10) {
            queue.remove()
        }
        queue.add(num)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchModule() {
    val vModel: ViewModelSearchText = viewModel()
    var isActive by remember { mutableStateOf(false) }

    val apiUrl = "https://dummyjson.com"
    var isError by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val retrofit = Retrofit.Builder()
        .baseUrl(apiUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val userAPI = retrofit.create(MainAPI::class.java)

    var userInfo by remember { mutableStateOf("") }
    var refreshTrigger by remember { mutableStateOf(0) }
    var firstOpen by remember {
        mutableStateOf(true)
    }

    Column {
        SearchBar(
            query = vModel.searchText.value,
            onQueryChange = { text ->
                vModel.searchText.value = text
                refreshTrigger++
            },
            onSearch = {},
            active = isActive,
            onActiveChange = {
                isActive = it
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "search"
                )
            },
            placeholder = {
                Text(text = "enter the number between 1 and 100: ")
            },
            trailingIcon = {
                if (vModel.searchText.value != "") {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "check",
                        modifier = Modifier.clickable {
                            refreshTrigger++
                        }
                    )
                }
            }) {
            Column {
                searchHistory.queue.forEach { text ->
                    Text(text = text, modifier = Modifier.clickable {
                        vModel.searchText.value = text
                        refreshTrigger++
                    })
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (!firstOpen) {
            when {
                isLoading -> CircularProgressIndicator()
                isError -> Text(text = "error")

                else -> {
                    if (userInfo.isNotEmpty()) {
                        Column {
                            Text(text = userInfo)
                        }
                    } else {
                        Text(text = "No results found")
                    }
                }
            }
        }

        LaunchedEffect(refreshTrigger) {
            if (vModel.searchText.value != "") {
                delay(2000)
                searchHistory.addToQueue(vModel.searchText.value)
                isActive = false
                firstOpen = false
                isLoading = true
                try {
                    isError = false
                    userInfo = ""
                    val user = userAPI.getUserById(vModel.searchText.value.toInt())
                    userInfo = "id: ${user.id} \n" +
                            "username: ${user.username} \n" +
                            "password: ${user.password} \n" +
                            "email: ${user.email}"
                } catch (e: Exception) {
                    isError = true
                    Log.d("myTag", e.toString())
                } finally {
                    isLoading = false
                }

            }
        }

    }
}
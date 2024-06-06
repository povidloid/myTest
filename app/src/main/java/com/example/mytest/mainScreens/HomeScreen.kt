package com.example.mytest.mainScreens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.mytest.SavedTheme
import com.example.mytest.retrofit.RetrofitObject
import com.example.mytest.sharedPreferences.ThemeManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.LinkedList
import java.util.Queue

data class Test(
    val id: Int,
    val testBody: String
)

class ViewModelSearchText : ViewModel() {
    val searchText = mutableStateOf("")
}

object SearchHistory  {
    var queue: Queue<String> = LinkedList()
    fun addToQueue(num: String) {
        if (queue.size == 10) {
            queue.remove()
        }
        if (!queue.contains(num))
            queue.add(num)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navHostController: NavHostController) {
    val hmContext = LocalContext.current
    val themeManager = ThemeManager(hmContext)

    val vModel: ViewModelSearchText = viewModel()
    var isActive by remember { mutableStateOf(false) }

    var isTestSearching by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column {
            SearchBar(
                modifier = Modifier.fillMaxWidth(),
                query = vModel.searchText.value,
                onQueryChange = { text ->
                    vModel.searchText.value = text
                },
                onSearch = {},
                active = isActive,
                onActiveChange = { isActive = it },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "search"
                    )
                },
                placeholder = {
                    Text(text = "Enter test's id:")
                },
                trailingIcon = {
                    if (vModel.searchText.value != "") {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "check",
                            modifier = Modifier.clickable {
                                if (vModel.searchText.value != "") {
                                    CoroutineScope(Dispatchers.IO).launch {
                                        isActive = false
                                        isTestSearching = true

                                        var testBod = ""

                                        SearchHistory.addToQueue(vModel.searchText.value)

                                        try {
                                            testBod =
                                                RetrofitObject.userApi.getTest(vModel.searchText.value.toInt()).testBody
                                        } catch (e: Exception) {
                                            Log.d("myTag", e.toString())
                                        }


                                        withContext(Dispatchers.Main) {
                                            isTestSearching = false
                                            if (testBod != "") {
                                                SelectedTest.testBody = testBod
                                                navHostController.navigate("completeTest")
                                            }
                                            else {
                                                Toast.makeText(
                                                    hmContext,
                                                    "Can't find this test...",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }


                                    }
                                }
                            }
                        )
                    }
                }
            ) {
                Column {
                    SearchHistory.queue.reversed().forEach { text ->
                        if(text != "") {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(2.dp)
                            ) {
                                Text(text = text, modifier = Modifier
                                    .clickable {
                                        vModel.searchText.value = text
                                    }
                                    .weight(1f))
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "clear",
                                    modifier = Modifier.clickable {
                                        isActive = false
                                        SearchHistory.queue.remove(text)
                                    }
                                )
                            }
                        }

                    }
                }
            }

            Text(
                text = "Create your test",
                fontSize = 16.sp,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable {
                    navHostController.navigate("createTest")
                }
            )

            if(isTestSearching){
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator()

                    Text(modifier = Modifier.padding(4.dp), text = "searching test...")
                }

            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        if (SavedTheme.theme) {
            Icon(
                imageVector = Icons.Default.FavoriteBorder,
                contentDescription = "entered dark theme",
                modifier = Modifier
                    .clickable {
                        SavedTheme.theme = !SavedTheme.theme
                        themeManager.setDarkTheme(SavedTheme.theme)
                    }
                    .size(64.dp)
                    .padding(2.dp)
            )
        } else {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "entered light theme",
                modifier = Modifier
                    .clickable {
                        SavedTheme.theme = !SavedTheme.theme
                        themeManager.setDarkTheme(SavedTheme.theme)
                    }
                    .size(64.dp)
                    .padding(2.dp)
            )
        }
    }
}
package com.example.mytest.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.mytest.room.MainViewModel

data class TestInfo(
    val title: String,
    val date: String
)

object Utils {
    val TestsList = listOf(
        TestInfo(title = "test 1", date = "27.03.2024")
    )
    val originTestList = TestsList.map { it.title }.toList()
    fun search(query: String): List<String> {
        return originTestList.filter { it.contains(query, ignoreCase = true) }
    }
}

@Composable
fun ListOfTests(navHostController: NavHostController,
                mainViewModel: MainViewModel = viewModel()) {
    val tests by mainViewModel.tests.collectAsState(initial = emptyList())
    if (SavedTests.savedTestsList.isEmpty()) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "List of tests is empty...")
            Text(text = "Create your own test in 'home' screen! :)")
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn {
                itemsIndexed(tests) { index, test ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                navHostController.navigate("completeTest")
                            }
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(8.dp),
                            text = index.toString(),
                            fontSize = 20.sp
                        )
                        Row(
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text(
                                text = "Date of creation: ",
                                fontSize = 16.sp
                            )
                            Text(
                                text = "placeholder",
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }

    }
}


@Composable
fun listOfTests(navHostController: NavHostController) {
    Scaffold(
        topBar = {
            BarSearch()
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding)
        ) {
            items(Utils.TestsList) { test ->
                TestView(test, navHostController)
            }
        }
    }
}

class SearchTextviewModel : ViewModel() {
    val searchText = mutableStateOf("")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarSearch() {
    val vModel: SearchTextviewModel = viewModel()
    val isActive = remember {
        mutableStateOf(false)
    }
    val mainList = remember {
        mutableStateOf(Utils.originTestList)
    }
    SearchBar(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        query = vModel.searchText.value,
        onQueryChange = { text ->
            vModel.searchText.value = text
            mainList.value = Utils.search(text)
        },
        onSearch = {
        },
        active = isActive.value,
        onActiveChange = {
            isActive.value = it
        },
        placeholder = {
            Text(text = "Search", fontSize = 20.sp)
        },
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "search")
        },
        trailingIcon = {
            if (vModel.searchText.value != "") {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "clear",
                    modifier = Modifier.clickable {
                        vModel.searchText.value = ""
                        mainList.value = Utils.search(vModel.searchText.value)
                        isActive.value = false
                    },
                )
            }
        },
    ) {
        LazyColumn {
            items(mainList.value) { item ->
                Box(
                    modifier = Modifier
                        .clickable {
                            vModel.searchText.value = item
                            mainList.value = Utils.search(item)
                        }
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Text(text = item)
                }
            }
        }
    }
}

@Composable
fun TestView(test: TestInfo, navHostController: NavHostController) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                navHostController.navigate("completeTest")
            }
    ) {
        Text(
            modifier = Modifier
                .padding(8.dp),
            text = test.title,
            fontSize = 20.sp
        )
        Row(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = "Date of creation: ",
                fontSize = 16.sp
            )
            Text(
                text = test.date,
                fontSize = 16.sp
            )
        }
    }
}
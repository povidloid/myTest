package com.example.mytest

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mytest.navigation.NavGraph
import com.example.mytest.room.MainViewModel
import com.example.mytest.ui.theme.MyTestTheme

@Composable
fun BottomNavigationBar(
    mainViewModel: MainViewModel = viewModel()
) {
    mainViewModel.loadTests()

    val navController = rememberNavController()
    var showBottomBar by rememberSaveable {
        mutableStateOf(true)
    }
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    showBottomBar = when (navBackStackEntry?.destination?.route) {
        "createTest" -> false
        "completeTest" -> false
        "resultScreen" -> false
        else -> true
    }

    MyTestTheme(SavedTheme.theme) {

        val items = listOf(
            BottomBarItem(
                title = "home",
                selectedIcon = Icons.Filled.Home,
                unselectedIcon = Icons.Outlined.Home,
            ),
            BottomBarItem(
                title = "tests",
                selectedIcon = Icons.Filled.List,
                unselectedIcon = Icons.Outlined.List,
            )
        )
        var selectedItemIndex by rememberSaveable {
            mutableIntStateOf(0)
        }
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Scaffold(
                bottomBar = {
                    if (showBottomBar) NavigationBar {
                        items.forEachIndexed { index, bottomBarItem ->
                            NavigationBarItem(
                                selected = selectedItemIndex == index,
                                onClick = {
                                    selectedItemIndex = index
                                    navController.navigate(bottomBarItem.title) {
                                        popUpTo(navController.graph.startDestinationId) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                icon = {
                                    Icon(
                                        imageVector = if (index == selectedItemIndex) {
                                            bottomBarItem.selectedIcon
                                        } else bottomBarItem.unselectedIcon,
                                        contentDescription = bottomBarItem.title
                                    )
                                },
                                label = {
                                    Text(text = bottomBarItem.title)
                                })
                        }
                    }
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                )
                {
                    NavGraph(navHostController = navController)
                }
            }
        }
    }
}
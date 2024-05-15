package com.example.mytest

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mytest.screens.searchHistory
import com.example.mytest.ui.theme.MyTestTheme

data class BottomBarItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

class ThemeManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
    var isDarkTheme by mutableStateOf(true)

    init {
        isDarkTheme = sharedPreferences.getBoolean(KEY_THEME, false)
    }

    internal fun setDarkTheme(isDark: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_THEME, isDark).apply()
        isDarkTheme = isDark
    }

    companion object {
        private const val KEY_THEME = "theme"
        private const val FILE_NAME = "savedTheme"
    }
}

class HistoryManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)

    internal fun setHistory(history: String) {
        sharedPreferences.edit().putString(KEY_HISTORY, history).apply()
    }

    internal fun getHistory(): String? {
        return sharedPreferences.getString(KEY_HISTORY, "")
    }

    companion object {
        private const val KEY_HISTORY = "history"
        private const val FILE_NAME = "savedHistory"
    }
}

object SavedTheme {
    var theme by
    mutableStateOf(false)
}


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        SavedTheme.theme = ThemeManager(this).isDarkTheme
        val savedHistory = HistoryManager(this).getHistory()?.split(", ")
        savedHistory?.let { searchHistory.queue.addAll(it) }

        setContent {
            BottomNavigationBar()
        }
    }

    override fun onStop() {
        super.onStop()
        val savedHistory = searchHistory.queue.toString().replace("[", "").replace("]", "")
        HistoryManager(this).setHistory(savedHistory)
    }
}

@Composable
fun BottomNavigationBar() {
    val navController = rememberNavController()
    var showBottomBar by rememberSaveable {
        mutableStateOf(true)
    }
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    showBottomBar = when (navBackStackEntry?.destination?.route) {
        "createTest" -> false
        "completeTest" -> false
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
            ),
            BottomBarItem(
                title = "API",
                selectedIcon = Icons.Filled.Search,
                unselectedIcon = Icons.Outlined.Search,
            ),
        )
        var selectedItemIndex by rememberSaveable {
            mutableStateOf(0)
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

@Composable
fun HomeScreen(navHostController: NavHostController) {
    var eLink by rememberSaveable {
        mutableStateOf("")
    }
    val themeManager = ThemeManager(LocalContext.current)

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            // horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            OutlinedTextField(
                value = eLink,
                onValueChange = { eLink = it },
                placeholder = {
                    Text(text = "link: ", fontSize = 20.sp)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                trailingIcon = {
                    if (eLink != "") {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "check",
                            modifier = Modifier.clickable {
                                Log.d("myTag", eLink)
                            })
                    }
                }
            )

            Text(
                text = "Create your test",
                fontSize = 16.sp,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable {
                    navHostController.navigate("createTest")
                })
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
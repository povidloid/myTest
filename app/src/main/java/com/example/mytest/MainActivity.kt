package com.example.mytest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.mytest.mainScreens.SearchHistory
import com.example.mytest.sharedPreferences.HistoryManager
import com.example.mytest.sharedPreferences.ThemeManager

data class BottomBarItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

object SavedTheme {
    var theme by
    mutableStateOf(false)
}

class MainActivity() : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        SavedTheme.theme = ThemeManager(this).isDarkTheme
        val savedHistory = HistoryManager(this).getHistory()?.split(", ")
        savedHistory?.let { SearchHistory.queue.addAll(it) }

        setContent {
            BottomNavigationBar()
        }
    }

    override fun onStop() {
        super.onStop()
        val savedHistory = SearchHistory.queue.toString().replace("[", "").replace("]", "")
        HistoryManager(this).setHistory(savedHistory)
    }
}
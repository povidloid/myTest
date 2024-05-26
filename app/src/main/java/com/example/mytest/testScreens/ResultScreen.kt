package com.example.mytest.testScreens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.navigation.NavHostController

@Composable
fun ResultScreen(navController: NavHostController){
    Box(
        contentAlignment = Alignment.Center
    ){
        Column {
            Text(text = "Test completed\n" +
                    "Your result:\n" +
                    "${FinalScore.value}/${FinalScore.maxValue}"
            )
            Button(onClick = {
                navController.navigate("home") {
                    popUpTo("resultTest") {
                        inclusive = true
                    }
                }
            }) {
                Text(text = "Return to home screen")
            }
        }
    }
}
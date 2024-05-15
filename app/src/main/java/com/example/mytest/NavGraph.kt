package com.example.mytest

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mytest.screens.CompleteTest
import com.example.mytest.screens.CreateTest
import com.example.mytest.screens.ListOfTests
import com.example.mytest.screens.ThirdScreen

@Composable
fun NavGraph(
    navHostController: NavHostController
){
    NavHost(navController = navHostController, startDestination = "home"){
        composable("tests"){
            ListOfTests(navHostController)
        }
        composable("home"){
            HomeScreen(navHostController)
        }
        composable("createTest"){
            CreateTest(navHostController)
        }
        composable("api"){
            ThirdScreen()
        }
        composable("completeTest"){
            CompleteTest()
        }
    }
}
package com.example.mytest.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mytest.mainScreens.HomeScreen
import com.example.mytest.testScreens.CompleteTest
import com.example.mytest.testScreens.CreateTest
import com.example.mytest.mainScreens.ListOfTests
import com.example.mytest.testScreens.ResultScreen

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
//        composable("api"){
//            ThirdScreen()
//        }
        composable("completeTest"){
            CompleteTest(navHostController)
        }
        composable("resultScreen"){
            ResultScreen(navHostController)
        }
    }
}
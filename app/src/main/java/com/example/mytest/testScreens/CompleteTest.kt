package com.example.mytest.testScreens

import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.mytest.mainScreens.SelectedTest
import com.google.gson.Gson

object FinalScore{
    var value = 0
    var maxValue = 0
}

@Composable
fun CompleteTest(navController: NavHostController) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            val testBody =
                Gson().fromJson(SelectedTest.testBody, Array<QuestionAndAnswers>::class.java)
                    .toList()
            QuestionToComplete(testBody, navController)
        }
    }
}

@Composable
fun QuestionToComplete(testBody: List<QuestionAndAnswers>, navController: NavHostController) {
    val selectedOptionIndexList = remember {
        MutableList(testBody.size) { mutableStateOf(-1) }
    }
    val score = remember {
        MutableList(testBody.size){0}
    }
    testBody.forEachIndexed { index1, test ->
        Text(text = test.question)
        test.answers.forEachIndexed { index2, answer ->
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (index2 == selectedOptionIndexList[index1].value),
                    onClick = {
                        selectedOptionIndexList[index1].value = index2
                        score[index1] = test.scores[index2]
                        Log.d("myTag", score.toString())
                    }

                )
                Text(text = answer)
            }
        }
    }
    Button(onClick = {
        FinalScore.value = score.sum()
        FinalScore.maxValue = score.size
        navController.navigate("resultScreen") {
            popUpTo("completeTest") {
                inclusive = true
            }
        }
    }) {
        Text(text = "Complete the test")
    }
}
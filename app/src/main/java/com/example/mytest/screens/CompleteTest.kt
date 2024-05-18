package com.example.mytest.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mytest.room.MainViewModel
import com.example.mytest.room.TestEntity
import com.google.gson.Gson
import kotlinx.coroutines.flow.first

@Composable
fun CompleteTest() {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            val testBody =
                Gson().fromJson(SelectedTest.testBody, Array<QuestionAndAnswers>::class.java)
                    .toList()
            Log.d("myTag", testBody.size.toString())
            QuestionToComplete(testBody)
        }
    }
}

@Composable
fun QuestionToComplete(testBody: List<QuestionAndAnswers>) {
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
}
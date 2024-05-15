package com.example.mytest.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun CompleteTest() {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            QuestionToComplete()
        }
    }
}
@Preview
@Composable
fun QuestionToComplete() {
    val answersList = listOf("answer 1", "answer 2", "answer 3", "answer 4")
    Column {
        Text(
            text = "Some question"
        )
        var selectedAnswer by remember {
            mutableIntStateOf(0)
        }
        answersList.forEachIndexed { index, answer ->
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedAnswer == index,
                    onClick = { selectedAnswer = index })
                Text(text = answer)
            }
        }
    }
}
package com.example.mytest.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class QuestionAndAnswers(
    val id: Int, var question: String, var answers: List<String>, var scores: List<Int>
)

@Composable
fun test() {
    var textList by remember { mutableStateOf(listOf("Первая строка текста")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Выводим все строки текста
        for (text in textList) {
            Text(text = text, style = MaterialTheme.typography.bodyMedium)
        }

        // Кнопка для добавления новой строки текста
        Button(
            onClick = {
                textList = textList + "Новая строка текста"
            },
            modifier = Modifier
                .padding(top = 16.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Добавить строку текста")
        }
    }
}

@Composable
fun CreateTest() {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Questions()
        }
    }
}

@Composable
fun Questions() {
    val defaultAnswers = listOf("answer1", "answer2", "answer3", "answer4")
    val defaulScores = listOf(0, 0, 0, 0)
    var questionList by remember{
        mutableStateOf(
            listOf(QuestionAndAnswers(
                id = 1,
                question = "placeholder",
                answers = defaultAnswers,
                scores = defaulScores
                )
            )

        )
    }
    questionList.forEach { question ->
        Question(question)
    }
    Icon(imageVector = Icons.Default.Add,
        contentDescription = "add",
        modifier = Modifier.clickable {
            val s = questionList.size + 1
            val newQuestion = QuestionAndAnswers(
                id = s,
                question = "placeholder",
                answers = defaultAnswers,
                scores = defaulScores
            )
            questionList += newQuestion
        }
    )
}

@Composable
fun Question(q: QuestionAndAnswers) {
    Log.d("myTag", q.toString())
    var selectedOptionIndex by remember {
        mutableStateOf(-1467)
    }
    var isEditing by remember {
        mutableStateOf(false)
    }
    var questionText by remember {
        mutableStateOf("")
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ){
        if(isEditing) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = questionText,
                onValueChange = {
                    questionText = it
                },
                placeholder = {
                    Text(text = "Enter your question here: ")
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "check",
                        modifier = Modifier
                            .clickable {
                                isEditing = false
                                q.question = questionText
                                Log.d("myTag", q.toString())
                            }
                    )
                }
            )
        }else{
            Text(
                text = "Click on 'edit' icon and enter your question and answers ->",
                modifier = Modifier.padding(end = 4.dp).weight(1f)
            )

            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "edit",
                modifier = Modifier
                    .clickable {
                        isEditing = true
                    }
            )

        }
    }


    q.answers.forEachIndexed { index, key ->
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = (index == selectedOptionIndex),
                onClick = {
                    selectedOptionIndex = index

                    val updatedScores = q.scores.toMutableList()
                    for (i in updatedScores.indices) {
                        if (i == index) {
                            updatedScores[i] = 1
                        } else
                            updatedScores[i] = 0
                    }

                    q.scores = updatedScores
                })
            if(!isEditing){
                Text(text = key)
            }else{
                var enteredAnswer by remember {
                    mutableStateOf(key)
                }

                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = enteredAnswer,
                    onValueChange = {
                        enteredAnswer = it

                        val updatedAnswers = q.answers.toMutableList()
                        updatedAnswers[index] = it
                        q.answers = updatedAnswers
                    },
                    placeholder = {
                        Text(
                            text = key
                        )
                    })
            }
        }
    }
}

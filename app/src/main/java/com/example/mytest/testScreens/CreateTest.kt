package com.example.mytest.testScreens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.mytest.mainScreens.Test
import com.example.mytest.retrofit.RetrofitObject

import com.example.mytest.room.MainViewModel
import com.example.mytest.room.TestEntity
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class QuestionAndAnswers(
    val id: Int,
    var question: String,
    var answers: List<String>,
    var scores: List<Int>
)

@Composable
fun CreateTest(navController: NavHostController) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            TestConstructor(navController)
        }
    }
}

object SavedTest {
    var testTittle: String = ""
    var questionsAndAnswersList: List<QuestionAndAnswers> = listOf()
}

@Composable
fun TestConstructor(
    navController: NavHostController,
    mainViewModel: MainViewModel = viewModel()
) {
    val qContext = LocalContext.current
    var enteredTestTittle by remember {
        mutableStateOf("")
    }
    var isTestTittleEditing by remember {
        mutableStateOf(false)
    }
    var iconIsSelected by remember {
        mutableStateOf(false)
    }

    val defaultAnswers = listOf("answer1", "answer2", "answer3", "answer4")
    val defaultScores = listOf(0, 0, 0, 0)
    var questionList by remember {
        mutableStateOf(
            listOf(
                QuestionAndAnswers(
                    id = 1,
                    question = "placeholder",
                    answers = defaultAnswers,
                    scores = defaultScores
                )
            )

        )
    }
    if (!isTestTittleEditing) {
        if (enteredTestTittle == "") {
            Text(
                text = "Click on me to rename your test",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .clickable {
                        isTestTittleEditing = true
                    }
            )
        } else {
            Text(
                text = enteredTestTittle,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .clickable {
                        isTestTittleEditing = true
                    }
            )
        }
    } else {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = enteredTestTittle,
            onValueChange = { enteredTestTittle = it },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "check",
                    modifier = Modifier.clickable {
                        SavedTest.testTittle = enteredTestTittle
                        isTestTittleEditing = false
                    })
            },
            placeholder = {
                Text(
                    text = "Rename your test: ",
                    style = MaterialTheme.typography.titleLarge
                )
            })
    }
    Spacer(modifier = Modifier.size(16.dp))

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
                scores = defaultScores
            )
            questionList += newQuestion
        }
    )


    Box(
        Modifier.fillMaxSize(),
//            .clip(CircleShape)
//            .border(2.dp, colorScheme.primary, CircleShape),
        contentAlignment = Alignment.BottomEnd,

        ) {
        if (!iconIsSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "check",
                modifier = Modifier
                    .clickable {
                        iconIsSelected = true
                        var isScoresNotNull = true

                        questionList.forEach { questionAndAnswers ->
                            if (questionAndAnswers.scores.sum() == 0)
                                isScoresNotNull = false
                        }

                        if (isScoresNotNull) {
                            SavedTest.questionsAndAnswersList += questionList

                            mainViewModel.insertTest(
                                TestEntity(
                                    tittle = SavedTest.testTittle,
                                    creationDate = LocalDate
                                        .now()
                                        .format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                                    testBody = Gson().toJson(SavedTest.questionsAndAnswersList)
                                )
                            )

                            Log.d("myTag", Gson().toJson(SavedTest.questionsAndAnswersList))

                            CoroutineScope(Dispatchers.IO).launch {
                                var receivedId = 0

                                try {
                                    receivedId = RetrofitObject.userApi.saveTest(
                                        Test(
                                            0,
                                            Gson().toJson(SavedTest.questionsAndAnswersList)
                                        )
                                    )
                                    Log.d("myTag", receivedId.toString())

                                } catch (e: Exception) {
                                    Log.d("myTag", e.toString())
                                }

                                withContext(Dispatchers.Main) {
                                    SavedTest.testTittle = ""
                                    SavedTest.questionsAndAnswersList = listOf()

                                    if (receivedId != 0)
                                        Toast
                                            .makeText(
                                                qContext,
                                                "Your test's id: $receivedId",
                                                Toast.LENGTH_LONG
                                            )
                                            .show()
                                    else
                                        Toast
                                            .makeText(
                                                qContext,
                                                "Your test was successfully created",
                                                Toast.LENGTH_LONG
                                            )
                                            .show()

                                    navController.navigate("home") {
                                        popUpTo("createTest") {
                                            inclusive = true
                                        }
                                    }
                                }
                            }
                        } else {
                            iconIsSelected = false
                            Toast
                                .makeText(
                                    qContext,
                                    "Your test has question without correct answer. Enter the correct answer and try again",
                                    Toast.LENGTH_LONG
                                )
                                .show()
                        }
                    }
                    .size(32.dp)
            )
        } else{
            CircularProgressIndicator()
        }
    }

}

@Composable
fun Question(q: QuestionAndAnswers) {
    var selectedOptionIndex by remember {
        mutableIntStateOf(-1467)
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
    ) {
        if (isEditing) {
            OutlinedTextField(
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
                            }
                    )
                }
            )
        } else {
            if (questionText == "") {
                Text(
                    text = "Click on 'edit' icon and enter your question and answers ->",
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .weight(1f)
                )
            } else {
                Text(
                    text = questionText,
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .weight(1f)
                )
            }

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
            if (!isEditing) {
                Text(text = key)
            } else {
                var enteredAnswer by remember {
                    mutableStateOf("")
                }

                OutlinedTextField(
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

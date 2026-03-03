package com.example.quizapp_room.ui.views.quiz

import android.R
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.quizapp_room.data.QuizItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    navController: NavController,
    currentQuizItem: QuizItem?,
    quizAnswers: List<String>,
    isAnswered: Boolean,
    onSubmitAnswer: (String) -> Unit,
    score: Int,
    onRestart: () -> Unit,
    onNextQuestion: () -> Unit
    ) {

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Quiz") },
                actions = {
                    Text(
                        text = "Score: $score",
                        modifier = Modifier.padding(8.dp)
                    )
                }

            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onRestart() }
            ){
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = "RESTART")
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(scrollState)
        ) {
            if (currentQuizItem != null) {
                QuizCard(
                    quizItem = currentQuizItem,
                    quizAnswers = quizAnswers,
                    isAnswered = isAnswered,
                    onSubmitAnswer = onSubmitAnswer,
                    onNextQuestion = onNextQuestion
                )
            }

        }

    }

}

@Composable
fun QuizCard(
    quizItem: QuizItem,
    quizAnswers: List<String>,
    isAnswered: Boolean,
    onSubmitAnswer: (String) -> Unit,
    onNextQuestion: () -> Unit
){

    // if I rotate phone, it will trigger recomposistion and this value will be lost.
    // use viewmodel instead
    var yourAnswer by remember { mutableStateOf("") }

    Card(
        modifier = Modifier
            .padding(8.dp)
    ) {
        AsyncImage(
            model = quizItem.imageUri ?: quizItem.imageRes,
            contentDescription = "",
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .height(200.dp)
        )
        Column(
            modifier = Modifier
                .padding(8.dp)
        ) {
            for (answer in quizAnswers) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    enabled = !isAnswered,
                    onClick = {
                        onSubmitAnswer(answer)
                        yourAnswer = answer
                        }
                ) {
                    Text(
                        text = answer,
                        fontWeight = FontWeight(24),
                        fontSize = 16.sp,
                        modifier = Modifier.padding(4.dp)
                    )

                }
            }
        }

        if(isAnswered){
            Column(
                modifier = Modifier
                    .padding(top = 32.dp, bottom = 32.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "You answered: $yourAnswer",
                    modifier = Modifier
                        .align(alignment = Alignment.CenterHorizontally)

                )
                Text(
                    text = "Correct answer: ${quizItem.answer}",
                    modifier = Modifier
                        .align(alignment = Alignment.CenterHorizontally)
                )
                Button(
                    onClick = onNextQuestion,
                    modifier = Modifier
                        .padding(4.dp)
                        .align(alignment = Alignment.CenterHorizontally)
                        .fillMaxWidth()
                ) {
                    Text(text = "Next Question")
                }

            }
        }

    }

}

package com.example.quizapp_room.ui.views.scoreBoard

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.quizapp_room.data.score.Score
import androidx.compose.foundation.lazy.items
import com.example.quizapp_room.ui.components.ScoreCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoreBoardScreen(
    navController: NavController,
    scores: List<Score>
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Scoreboard") }
            )
        }
    ) {
        paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues),
            contentPadding = paddingValues
        ) {
            items(scores) { scores ->
                ScoreCard(scores.playerName, scores.score, scores.timeStamp)
            }
        }

    }
}
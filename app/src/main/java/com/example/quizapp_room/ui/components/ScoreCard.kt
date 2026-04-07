package com.example.quizapp_room.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat

@Composable
fun ScoreCard(
    playerName: String,
    score: Int,
    timeStamp: Long
){

    val formatTime = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
    val time = formatTime.format(timeStamp)

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
        ) {
            Text(
                modifier = Modifier
                    .padding(8.dp),
                text = "Score: $score"
            )
            Text(
                modifier = Modifier
                    .padding(8.dp),
                text = "Player: $playerName"
            )
            Text(
                modifier = Modifier
                    .padding(8.dp),
                text = "Time: $time"
            )
        }
    }

}
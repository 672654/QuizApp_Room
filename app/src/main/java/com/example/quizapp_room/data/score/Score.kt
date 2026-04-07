package com.example.quizapp_room.data.score

data class Score(
    val playerName: String = "",
    val score: Int = 0,
    val timeStamp: Long = System.currentTimeMillis()
)
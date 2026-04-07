package com.example.quizapp_room.data.score

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class ScoreRepository {
    private val fireStore = FirebaseFirestore.getInstance()
    private val scoreCollection = fireStore.collection("scores")

    suspend fun saveScore(playerName: String, score: Int){
        val scoreData = Score(playerName, score, System.currentTimeMillis())
        scoreCollection.add(scoreData).await()

    }

    suspend fun getScores(): List<Score> {
        return try {
            scoreCollection
                .orderBy("score", Query.Direction.DESCENDING)
                .limit(20)
                .get()
                .await()
                .toObjects(Score::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }
}
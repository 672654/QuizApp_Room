package com.example.quizapp_room.data

import kotlinx.coroutines.flow.Flow

class QuizRepository(private val quizDao: QuizDao) {

    fun getAllQuizItems(): Flow<List<QuizItem>> {
        return quizDao.getAllQuizItems()
    }

    suspend fun insertQuizItem(quizItem: QuizItem) {
        quizDao.insertQuizItem(quizItem)
    }

    suspend fun deleteQuizItem(id: Int) {
        quizDao.deleteQuizItem(id)
    }
}

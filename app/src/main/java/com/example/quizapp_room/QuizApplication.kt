package com.example.quizapp_room

import android.app.Application
import com.example.quizapp_room.data.QuizDatabase
import com.example.quizapp_room.data.QuizRepository

/**
 * this is Application class.
 * first one to be created when the app starts
 * last one to close when the app closes
 * we use this to create the database and the repository
 * all other classes can then use the database and the repository
 */

class QuizApplication : Application() {
    // 'by lazy' betyr at databasen og repository-et kun lages
    // første gang de blir brukt..
    val database by lazy { QuizDatabase.getDatabase(this) }
    val repository by lazy { QuizRepository(database.quizDao()) }
}
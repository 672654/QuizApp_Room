package com.example.quizapp_room.data

import android.content.Context
import androidx.compose.ui.geometry.isEmpty
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.quizapp_room.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


@Database(entities = [QuizItem::class], version = 1, exportSchema = false)
abstract class QuizDatabase : RoomDatabase() {

    abstract fun quizDao(): QuizDao

    companion object {
        @Volatile
        internal var INSTANCE: QuizDatabase? = null

        fun getDatabase(context: Context): QuizDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    QuizDatabase::class.java,
                    "quiz_database"
                )
                    // 2. Send med context til callbacken
                    .addCallback(QuizDatabaseCallback(context))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

private class QuizDatabaseCallback(private val context: Context) : RoomDatabase.Callback(){

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)

        CoroutineScope(Dispatchers.IO).launch {
            populateDatabase(QuizDatabase.getDatabase(context).quizDao())
        }
    }

    override fun onOpen(db: SupportSQLiteDatabase) {
        super.onOpen(db)

        CoroutineScope(Dispatchers.IO).launch {
            val dao = QuizDatabase.getDatabase(context).quizDao()

            if (dao.getAllQuizItems().first().isEmpty()) {
                // Hvis listen er tom, legg til quiztimes på nytt.
                populateDatabase(dao)
            }
        }

    }

    suspend fun populateDatabase(quizDao: QuizDao) {
        quizDao.insertQuizItem(
            QuizItem(id = 0, answer = "Panda", imageRes = R.drawable.panda)
        )
        quizDao.insertQuizItem(
            QuizItem(id = 0, answer = "Lion", imageRes = R.drawable.lion)
        )
        quizDao.insertQuizItem(
            QuizItem(id = 0, answer = "Elephant", imageRes = R.drawable.elephant)
        )
    }
}

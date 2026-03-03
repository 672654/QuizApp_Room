package com.example.quizapp_room.data

import android.database.Cursor
import android.service.carrier.CarrierService
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface QuizDao {

    @Insert
    suspend fun insertQuizItem(quizItem: QuizItem)

    @Query("SELECT * FROM quiz_items")
    fun getAllQuizItems(): Flow<List<QuizItem>>

    @Query("DELETE FROM quiz_items WHERE id = :id")
    suspend fun deleteQuizItem(id: Int)



    @Query("""
        SELECT correct_answer AS name,
        CASE
            WHEN imageUri IS NOT NULL THEN imageUri
            ELSE 'android.resource://com.example.quizapp_room/' || CAST(image_res_id AS TEXT)
        END AS URI
        FROM quiz_items
        """)
    fun getAllQuizItemsCursor(): Cursor
}

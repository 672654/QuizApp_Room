package com.example.quizapp_room.data

import androidx.annotation.DrawableRes
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz_items")
data class QuizItem(

    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "correct_answer") val answer: String,

    //nullable if no image from resources
    @ColumnInfo(name = "image_res_id")
    @DrawableRes val imageRes: Int? = null,

    //nullable if no image from uri
    @ColumnInfo(name = "imageUri")
    val imageUri: String? = null,





    )
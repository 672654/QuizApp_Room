package com.example.quizapp_room.data

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri

class QuizContentProvider: ContentProvider() {


    private lateinit var database: QuizDatabase

    override fun onCreate():Boolean {
        database = QuizDatabase.getDatabase(context!!)
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String?>?,
        selection: String?,
        selectionArgs: Array<out String?>?,
        sortOrder: String?
    ): Cursor? {

        val cursor = database.quizDao().getAllQuizItemsCursor()

        cursor.setNotificationUri(context?.contentResolver, uri)
        return cursor
    }



    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String?>?
    ): Int {
        return 0
    }
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String?>?): Int {
        return 0
    }
}
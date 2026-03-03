package com.example.quizapp_room.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log

fun Context.takePersistableUriPermission(uri: Uri) {
    try {
        contentResolver.takePersistableUriPermission(
            uri,
            Intent.FLAG_GRANT_READ_URI_PERMISSION
        )
    } catch (e: Exception) {
        Log.e("URI_READ_PERMISSION", "Kunne ikke gi tillatelse: $e")
    }
}
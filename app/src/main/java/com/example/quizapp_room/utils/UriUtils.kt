package com.example.quizapp_room.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log

//extension function that can be used on all context objects to give permission to read the uri.
fun Context.giveUriPermission(uri: Uri) {
    try {
        contentResolver.takePersistableUriPermission(
            uri,
            Intent.FLAG_GRANT_READ_URI_PERMISSION
        )
    } catch (e: Exception) {
        Log.e("URI_READ_PERMISSION", "Kunne ikke gi tillatelse: $e")
    }
}
package com.example.quizapp_room

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.quizapp_room.navigation.AppNavigation
import com.example.quizapp_room.ui.theme.QuizApp_roomTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuizApp_roomTheme {
                AppNavigation()
            }
        }
    }
}

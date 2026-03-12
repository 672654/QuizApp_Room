package com.example.quizapp_room.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalGraphicsContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.quizapp_room.QuizApplication
import com.example.quizapp_room.ui.views.gallery.GalleryViewModel
import com.example.quizapp_room.ui.views.gallery.GalleryScreen
import com.example.quizapp_room.ui.views.gallery.GalleryViewModelFactory
import com.example.quizapp_room.ui.views.home.HomeScreen
import com.example.quizapp_room.ui.views.quiz.QuizScreen
import com.example.quizapp_room.ui.views.quiz.QuizViewModel
import com.example.quizapp_room.ui.views.quiz.QuizViewModelFactory

@Composable
fun AppNavigation() {

    //get repo from application class thru localcontext (access to android system)
    val repository = (LocalContext.current.applicationContext as QuizApplication).repository

    val navController = rememberNavController()

    val quizViewModel: QuizViewModel = viewModel(
        factory = QuizViewModelFactory(repository)
    )

    NavHost(navController = navController, startDestination = "home") {

        composable("home") {
            HomeScreen(navController = navController)
        }

        //Consider creating a QuizUiState data class to contain all the states needed in QuizScreen
        //instead of sending so many parameters.
        composable("quiz") {
            QuizScreen(
                navController = navController,
                quizAnswers = quizViewModel.quizAnswers,
                currentQuizItem = quizViewModel.currentQuizItem.collectAsStateWithLifecycle().value,
                isAnswered = quizViewModel.isAnswered,
                onSubmitAnswer = { quizViewModel.checkAnswer(it) },
                score = quizViewModel.score,
                notEnoughQuizItems = quizViewModel.notEnoughQuizItems,
                isQuizDone = quizViewModel.isQuizDone,
                maximumScorePossible = quizViewModel.maximumScorePossible,
                onRestart = { quizViewModel.startNewQuiz() },
                onNextQuestion = {quizViewModel.loadNextQuestion()}
            )
        }

        composable(route = "gallery"){
            val galleryViewModel: GalleryViewModel = viewModel(
                factory = GalleryViewModelFactory(repository)
                )
            val quizItems by galleryViewModel.allItems.collectAsStateWithLifecycle()
            GalleryScreen(
                navController = navController,
                quizItems = quizItems,
                onAddButtonClick = { galleryViewModel.addQuizItem(it) },
                onSortButtonClick = {galleryViewModel.sortAscending(it)},
                onDeleteButtonClick = {galleryViewModel.deleteQuizItem(it.id)}
                )
        }
    }
}

package com.example.quizapp_room.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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

    val repository = (LocalContext.current.applicationContext as QuizApplication).repository

    val navController = rememberNavController()

    val quizViewModel: QuizViewModel = viewModel(
        factory = QuizViewModelFactory(repository)
    )

    NavHost(navController = navController, startDestination = "home") {

        composable("home") {
            HomeScreen(navController = navController)
        }

        composable("quiz") {
            QuizScreen(
                navController = navController,
                quizAnswers = quizViewModel.quizAnswers,
                currentQuizItem = quizViewModel.currentQuizItem.collectAsStateWithLifecycle().value,
                isAnswered = quizViewModel.isAnswered,
                onSubmitAnswer = { quizViewModel.checkAnswer(it) },
                score = quizViewModel.score,
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

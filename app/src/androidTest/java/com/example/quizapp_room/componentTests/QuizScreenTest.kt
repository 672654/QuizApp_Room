package com.example.quizapp_room.componentTests

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import com.example.quizapp_room.data.QuizItem
import com.example.quizapp_room.ui.views.quiz.QuizScreen
import org.junit.Rule
import org.junit.Test

class QuizScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testScoreUpdatesOnCorrectAndWrongAnswer() {
        // lag et falsk QuizItem som skal sendes til QuizScreen.
        val testItem = QuizItem(id = 1, answer = "Oslo", imageRes = 0)
        val answers = listOf("Oslo", "Bergen", "Trondheim")

        // Bruker states for å simulere hva ViewModel normalt ville gjort.
        var scoreState by mutableIntStateOf(0)
        var isAnsweredState by mutableStateOf(false)

        composeTestRule.setContent {
            val navController = rememberNavController()
            QuizScreen(
                navController = navController,
                currentQuizItem = testItem,
                quizAnswers = answers,
                isAnswered = isAnsweredState,
                onSubmitAnswer = { selected ->
                    // Simuler viewmodel
                    if (selected == testItem.answer) {
                        scoreState++
                    }
                    isAnsweredState = true
                },
                score = scoreState,
                notEnoughQuizItems = false,
                isQuizDone = false,
                maximumScorePossible = 10,
                onRestart = {},
                onNextQuestion = {
                    // Simuler at vi går til neste (men vi beholder samme item for testen)
                    isAnsweredState = false
                }
            )
        }

        // Sjekk at start-score er 0
        composeTestRule.onNodeWithTag("ScoreText").assertTextContains("0", substring = true)


        // Klikk på riktig svar
        composeTestRule.onNodeWithTag("CorrectAnswer").performClick()
        // Sjekk at score ble 1
        composeTestRule.onNodeWithTag("ScoreText").assertTextContains("1", substring = true)


        // Gå til "neste" spørsmål
        composeTestRule.onNodeWithTag("NextQuestionButton").performClick()
        // Klikk på feil svar
        composeTestRule.onAllNodesWithTag("WrongAnswer")[0].performClick()
        // Sjekk at score er 1
        composeTestRule.onNodeWithTag("ScoreText").assertTextContains("1", substring = true)


        // Gå til "neste" spørsmål
        composeTestRule.onNodeWithTag("NextQuestionButton").performClick()
        // Klikk på riktig svar
        composeTestRule.onNodeWithTag("CorrectAnswer").performClick()
        // Sjekk at score er 2
        composeTestRule.onNodeWithTag("ScoreText").assertTextContains("2", substring = true)

    }
}

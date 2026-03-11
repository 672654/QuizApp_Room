package com.example.quizapp_room.integrationTests

import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.example.quizapp_room.MainActivity
import org.junit.Rule
import org.junit.Test

class QuizScoreTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testCorrectScore(){

        composeTestRule.onNodeWithTag("startQuizButton").performClick()
        composeTestRule.onNodeWithTag("CorrectAnswer").performClick()
        composeTestRule.onNodeWithTag("ScoreText").assertTextContains("1", substring = true)

        composeTestRule.onNodeWithTag("NextQuestionButton").performClick()
        composeTestRule.onAllNodesWithTag("WrongAnswer")[0].performClick()
        composeTestRule.onNodeWithTag("ScoreText").assertTextContains("1", substring = true)

        composeTestRule.onNodeWithTag("NextQuestionButton").performClick()
        composeTestRule.onNodeWithTag("CorrectAnswer").performClick()
        composeTestRule.onNodeWithTag("ScoreText").assertTextContains("2", substring = true)

    }
}
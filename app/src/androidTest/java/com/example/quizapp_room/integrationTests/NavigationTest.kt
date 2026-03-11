package com.example.quizapp_room.integrationTests

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.example.quizapp_room.MainActivity
import org.junit.Rule
import org.junit.Test

class NavigationTest {

    //Can test everything inside setContent in MainActivity.
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testNavigateToQuiz(){
        composeTestRule.onNodeWithTag("startQuizButton").performClick()
        composeTestRule.onNodeWithTag("QuizContent").assertIsDisplayed()
    }

    @Test
    fun testNavigateToGallery(){
        // Click the button with testTag toGalleryButton in MainActivity
        composeTestRule.onNodeWithTag("toGalleryButton").performClick()
        // Look for a node with testTag GalleryContent opn the display.
        composeTestRule.onNodeWithTag("GalleryContent").assertIsDisplayed()

    }



}
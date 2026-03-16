package com.example.quizapp_room.componentTests

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasAnySibling
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.rememberNavController
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import com.example.quizapp_room.data.QuizItem
import com.example.quizapp_room.ui.views.gallery.GalleryScreen
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AddDeleteTest {


    @get:Rule
    val composeTestRule = createComposeRule()

    val quizItems = mutableStateListOf<QuizItem>()


    @Before
    fun setUp(){
        Intents.init()

        quizItems.add(QuizItem(id = 1, answer = "TestItem", imageRes = 0))

        composeTestRule.setContent {
            val navController = rememberNavController()
            GalleryScreen(
                navController = navController,
                quizItems = quizItems,
                onAddButtonClick = { quizItems.add(it) },
                onSortButtonClick = {},
                onDeleteButtonClick = { quizItems.remove(it) }
            )
        }

    }

    @After
    fun done(){
        Intents.release()
        quizItems.clear()
    }

    @Test
    fun testAdd(){

        //Find number of initial count. should be 1.
        val initCount = quizItems.size
        println("initCount: $initCount")

        // Intent stubbing = Return "fake" resource when the intent is started.
        Intents.intending(IntentMatchers.anyIntent()).respondWith(getIntentStub())

        //start intent with pickimageButton.
        composeTestRule.onNodeWithTag("PickImageButton").performClick()

        //skriv navnet på den nye quiz item. Trykk på add knappen.
        composeTestRule.onNodeWithTag("QuizItemNameText").performTextInput("NewTestItem")
        composeTestRule.onNodeWithTag("AddQuizItemButton").performClick()

        val finalCount = quizItems.size
        println("finalCount: $finalCount")


        //Sjekk at listen nå har ett element mer.
        Assert.assertEquals(
            "number should be 1 more than before",
            initCount + 1,
            finalCount
        )

        //sjekk at NewTestItem vises i listen.
        composeTestRule.onNodeWithText("NewTestItem").assertIsDisplayed()


    }

    @Test
    fun testDelete(){
        val initCount = quizItems.size

        //check if item exists.
        composeTestRule.onNodeWithText("TestItem").assertIsDisplayed()

        // click deletebutton on that node
        composeTestRule.onNode(
            hasTestTag("DeleteQuizItemButton") and
                    hasAnySibling(hasText("TestItem"))
        ).performClick()

        val finalCount = quizItems.size

        Assert.assertEquals(
            "Number should be 1 less than before.",
            initCount - 1,
            finalCount
        )

        composeTestRule.onNodeWithText("TestItem").assertDoesNotExist()

    }



    fun getIntentStub(): Instrumentation.ActivityResult {
        val resultData = Intent().apply {
            data = Uri.parse("android.resource://com.example.quizapp_room/drawable/panda")
        }
        return Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)
    }
}
package com.example.quizapp_room.integrationTests

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasAnySibling
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextInput
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.platform.app.InstrumentationRegistry
import com.example.quizapp_room.MainActivity
import com.example.quizapp_room.QuizApplication
import com.example.quizapp_room.data.QuizRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AddDeleteGalleryTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private val repository: QuizRepository by lazy {
        (InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as QuizApplication).repository
    }

    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun done() {
        Intents.release()
    }

    @Test
    fun testAddGalleryItem() {
        // Naviger til gallery
        composeTestRule.onNodeWithTag("toGalleryButton").performClick()

        // Finn startantall direkte fra databasen (problemer med lazycolumn, så får ikke antallet derfra.)
        val initCount = countQuizItems()

        // Intent stubbing: Bruker anyIntent() for å være sikker på å fange bildevelgeren. sender kun en intent uansatt, så ok.
        Intents.intending(IntentMatchers.anyIntent()).respondWith(getIntentStub())

        // Klikk på Legg til-knappen. Legg til knappen starter en intent, og dermed plukkes stub-intentet opp, i stedet for det ekte intentet.
        composeTestRule.onNodeWithTag("PickImageButton").performClick()

        // Skriv navn på den nye quiz item. Trykk på add knappen.
        composeTestRule.onNodeWithTag("QuizItemNameText").performTextInput("TestPanda")
        composeTestRule.onNodeWithTag("AddQuizItemButton").performClick()

        // Vent til databasen er ferdig med å skrive. waitUntil() returnerer true/false. venter på true.
        composeTestRule.waitUntil(5000) {
            countQuizItems() == initCount + 1
        }

        // Sjekk at databasen nå har ett element mer
        val finalCount = countQuizItems()
        Assert.assertEquals(
            "Antall elementer i databasen skal ha økt med 1",
            initCount + 1,
            finalCount
        )

        // Scroll til det nye elementet og sjekk at det vises
        composeTestRule.onNodeWithTag("GalleryContent").performScrollToNode(hasText("TestPanda"))
        composeTestRule.onNodeWithText("TestPanda").assertIsDisplayed()
    }

    @Test
    fun testDeleteGalleryItem() {
        // 1. Naviger til gallery
        composeTestRule.onNodeWithTag("toGalleryButton").performClick()

        // 2. Hent elementer fra databasen for å vite hva vi skal slette
        val items = runBlocking { repository.getAllQuizItems().first() }

        // Hvis listen er tom, legger vi til et element først slik at testen har noe å slette
        if (items.isEmpty()) {
            Intents.intending(IntentMatchers.anyIntent()).respondWith(getIntentStub())
            composeTestRule.onNodeWithTag("PickImageButton").performClick()
            composeTestRule.onNodeWithTag("QuizItemNameText").performTextInput("SlettMegPanda")
            composeTestRule.onNodeWithTag("AddQuizItemButton").performClick()

            // Vent til databasen er ferdig med å skrive. waitUntil() returnerer true/false. venter på true.
            composeTestRule.waitUntil(5000) {
                runBlocking { repository.getAllQuizItems().first().isNotEmpty() }
            }
        }

        // Hent oppdatert liste og antall
        val currentItems = runBlocking { repository.getAllQuizItems().first() }
        val itemToDelete = currentItems.first()
        val initCount = currentItems.size

        // 3. Scroll til elementet i UI-et (LazyColumn tegner kun synlige noder)
        composeTestRule.onNodeWithTag("GalleryContent")
            .performScrollToNode(hasText(itemToDelete.answer))

        // Klikk på sletteknappen tilhørende elementet som skal slettes.
        composeTestRule.onNode(
            hasTestTag("DeleteQuizItemButton") and hasAnySibling(hasText(itemToDelete.answer))
        ).performClick()

        // 5. Verifiser at antallet i databasen har sunket med waitUntil()
        composeTestRule.waitUntil(5000) {
            countQuizItems() == initCount - 1
        }

        val finalCount = countQuizItems()
        Assert.assertEquals(
            "Antall elementer i databasen skal ha sunket med 1",
            initCount - 1,
            finalCount
        )

        // 6. Bekreft at elementet er borte fra UI
        composeTestRule.onNodeWithText(itemToDelete.answer).assertDoesNotExist()
    }

    fun countQuizItems(): Int {
        return runBlocking { repository.getAllQuizItems().first().size }
    }

    fun getIntentStub(): Instrumentation.ActivityResult {
        val resultData = Intent().apply {
            data = Uri.parse("android.resource://com.example.quizapp_room/drawable/panda")
        }
        return Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)
    }
}
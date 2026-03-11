package com.example.quizapp_room.componentTests

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.composable
import androidx.navigation.createGraph
import androidx.navigation.testing.TestNavHostController
import com.example.quizapp_room.ui.views.home.HomeScreen
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testNavigateToQuiz() {
        var navController: TestNavHostController ?= null
        
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current).apply {
                navigatorProvider.addNavigator(ComposeNavigator())
                // Definerer en enkel graf for testen
                graph = createGraph(startDestination = "home") {
                    composable("home") { }
                    composable("quiz") { }
                }
            }
            HomeScreen(navController = navController)
        }

        // Klikk på start quiz knappen
        composeTestRule.onNodeWithTag("startQuizButton").performClick()

        // Sjekk om destinasjonen er riktig
        val route = navController?.currentBackStackEntry?.destination?.route
        assertEquals("quiz", route)
    }
}

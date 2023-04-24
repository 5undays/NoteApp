package com.example.noteapp.feature_note.presentation

import androidx.activity.compose.setContent
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.noteapp.core.util.TestTags
import com.example.noteapp.di.AppModule
import com.example.noteapp.feature_note.presentation.add_edit_note.AddEditNoteScreen
import com.example.noteapp.feature_note.presentation.notes.NoteScreen
import com.example.noteapp.feature_note.presentation.util.Screen
import com.example.noteapp.ui.theme.NoteAppTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class NotesEndToEndTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
        composeRule.activity.setContent {
            NoteAppTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Screen.NoteScreen.route
                ) {
                    composable(route = Screen.NoteScreen.route) {
                        NoteScreen(navController = navController)
                    }
                    composable(route = Screen.AddEditNoteScreen.route + "?noteId={noteId}&noteColor={noteColor}",
                        arguments = listOf(
                            navArgument(name = "noteId") {
                                type = NavType.IntType
                                defaultValue = -1
                            }, navArgument(name = "noteColor") {
                                type = NavType.IntType
                                defaultValue = -1
                            }
                        )) {
                        val color = it.arguments?.getInt("noteColor") ?: -1
                        //AddEditNoteScreen(navController = navController, noteColor = color)
                    }
                }
            }
        }
    }

    @Test
    fun saveNewNote_editAfterwords() {
        // click on fab to get to add note screen
        composeRule.onNodeWithContentDescription("Add").performClick()

        // enter texts in title and content text field
        composeRule.onNodeWithTag(TestTags.TITLE_TEXT_FIELD).performTextInput("test-title")
        composeRule.onNodeWithTag(TestTags.CONTENT_TEXT_FIELD).performTextInput("test-content")

        //save the new
        composeRule.onNodeWithContentDescription("Save").performClick()

        // make sure there is a note in the list with out title and content
        composeRule.onNodeWithText("test-title").assertIsDisplayed()
        // click on note to edit it
        composeRule.onNodeWithText("test-title").performClick()

        // make sure title and content text field contain note title and content
        composeRule.onNodeWithTag(TestTags.TITLE_TEXT_FIELD).assertTextEquals("test-title")
        composeRule.onNodeWithTag(TestTags.CONTENT_TEXT_FIELD).assertTextEquals("test-content")

        // add the text 2 to the title text field
        composeRule.onNodeWithTag(TestTags.TITLE_TEXT_FIELD).performTextInput("2")
        composeRule.onNodeWithContentDescription("Save").performClick()

        // make sure the update was applied to the list
        composeRule.onNodeWithText("test-title2").assertIsDisplayed()

    }

    @Test
    fun saveNewNotes_orderByTitleDescending() {
        for (i in 1..3) {
            composeRule.onNodeWithContentDescription("Add").performClick()

            // enter texts in title and content text field
            composeRule.onNodeWithTag(TestTags.TITLE_TEXT_FIELD).performTextInput("$i")
            composeRule.onNodeWithTag(TestTags.CONTENT_TEXT_FIELD)
                .performTextInput("$i")

            composeRule.onNodeWithContentDescription("Save").performClick()
        }
        composeRule.onNodeWithText("1").assertIsDisplayed()
        composeRule.onNodeWithText("2").assertIsDisplayed()
        composeRule.onNodeWithText("3").assertIsDisplayed()

        composeRule.onNodeWithContentDescription("Sort").performClick()
        composeRule.onNodeWithContentDescription("Title").performClick()
        composeRule.onNodeWithContentDescription("Descending").performClick()

        composeRule.onAllNodesWithTag(TestTags.NOTE_ITEM)[0].assertTextContains("3")
        composeRule.onAllNodesWithTag(TestTags.NOTE_ITEM)[1].assertTextContains("2")
        composeRule.onAllNodesWithTag(TestTags.NOTE_ITEM)[2].assertTextContains("1")
    }
}
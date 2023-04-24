package com.example.noteapp.feature_note.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.noteapp.feature_note.presentation.add_edit_note.AddEditNoteScreen
import com.example.noteapp.feature_note.presentation.add_edit_note.AddEditNoteViewModel
import com.example.noteapp.feature_note.presentation.notes.NoteScreen
import com.example.noteapp.feature_note.presentation.util.Screen
import com.example.noteapp.ui.theme.NoteAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NoteAppTheme {
                Surface(color = MaterialTheme.colors.background) {
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
                            val viewModel = hiltViewModel<AddEditNoteViewModel>()
                            val noteTitle = viewModel.noteTitle.value
                            val noteContent = viewModel.noteContent.value
                            val stateColor = viewModel.noteColor.value
                            val scaffoldState = rememberScaffoldState()

                            LaunchedEffect(key1 = true) {
                                viewModel.eventFlow.collectLatest { event ->
                                    when (event) {
                                        is AddEditNoteViewModel.UiEvent.ShowSnackbar -> {
                                            scaffoldState.snackbarHostState.showSnackbar(message = event.message)
                                        }
                                        is AddEditNoteViewModel.UiEvent.save -> {
                                            navController.navigateUp()
                                        }
                                    }
                                }
                            }
                            AddEditNoteScreen(
                                navController = navController,
                                noteColor = stateColor,
                                noteTitle = noteTitle,
                                noteContent = noteContent,
                                onEvent = viewModel::onEvent,
                                scaffoldState = scaffoldState
                            )
                        }
                    }
                }
            }
        }
    }
}

package com.example.noteapp.feature_note.presentation.add_edit_note

import android.annotation.SuppressLint
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.noteapp.core.util.TestTags
import com.example.noteapp.feature_note.domain.model.Note
import com.example.noteapp.feature_note.presentation.add_edit_note.components.CustomMultiHintTextField
import com.example.noteapp.feature_note.presentation.add_edit_note.components.TransparentHitTextField
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AddEditNoteScreen(
    navController: NavController
    , noteColor: Int
    , noteTitle: NoteTextFieldState
    , noteContent: NoteTextFieldState
    , onEvent: (AddEditNoteEvent) -> Unit
    , scaffoldState: ScaffoldState
) {
    val noteBackgroundAnimatable = remember {
        Animatable(initialValue = Color(noteColor))
    }
    val scope = rememberCoroutineScope()

    Scaffold(floatingActionButton = {
        FloatingActionButton(
            onClick = { onEvent(AddEditNoteEvent.SaveNote) },
            backgroundColor = MaterialTheme.colors.primary
        ) {
            Icon(imageVector = Icons.Default.Save, contentDescription = "Save")
        }
    }, scaffoldState = scaffoldState) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(noteBackgroundAnimatable.value)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Note.notesColors.forEach { color ->
                    val colorInt = color.toArgb()
                    Box(modifier = Modifier
                        .size(50.dp)
                        .shadow(15.dp, CircleShape)
                        .clip(CircleShape)
                        .background(color)
                        .border(
                            width = 3.dp, color = if (noteColor == colorInt) {
                                Color.Black
                            } else Color.Transparent, shape = CircleShape
                        )
                        .clickable {
                            scope.launch {
                                noteBackgroundAnimatable.animateTo(
                                    targetValue = Color(colorInt),
                                    animationSpec = tween(durationMillis = 500)
                                )
                            }
                            onEvent(AddEditNoteEvent.ChangeColor(colorInt))
                        })
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            TransparentHitTextField(
                text = noteTitle.text,
                hint = noteTitle.hint,
                onValueChange = {
                    onEvent(AddEditNoteEvent.EnteredTitle(it))
                },
                onFocusChange = {
                    onEvent(AddEditNoteEvent.ChangeTitleFocus(it))
                },
                isHintVisible = noteTitle.isHintVisible,
                singleLine = true,
                textStyle = MaterialTheme.typography.h5,
                testTag = TestTags.TITLE_TEXT_FIELD
            )
            Spacer(modifier = Modifier.height(16.dp))
            CustomMultiHintTextField(
                value = noteContent.text,
                onValueChanged = {
                    onEvent(AddEditNoteEvent.EnteredContent(it))
                },
                hintText = noteContent.hint,
                modifier = Modifier.fillMaxSize(),
                textStyle = MaterialTheme.typography.h5,
                testTag = TestTags.CONTENT_TEXT_FIELD
            )
        }
    }
}
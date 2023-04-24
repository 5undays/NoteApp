package com.example.noteapp.feature_note.presentation.add_edit_note.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

@Composable
fun CustomMultiHintTextField(
    value: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    hintText: String = "",
    maxLine: Int = 70,
    textStyle: TextStyle = MaterialTheme.typography.body1,
    testTag: String = "",
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChanged,
        textStyle = textStyle,
        modifier = modifier,
        maxLines = maxLine,
        decorationBox = { innerTextField ->
            Box(modifier = modifier) {
                if (value.isEmpty()) {
                    Text(
                        text = hintText,
                        color = Color.DarkGray,
                        style = textStyle
                    )
                }
                innerTextField()
            }
        },
    )

}
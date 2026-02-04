package dev.androhit.crosschat.designsystem.texts

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle

@Composable
fun ErrorText(
    error: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodySmall,
) {
    Text(
        text = error,
        modifier = modifier,
        style = style,
        color = MaterialTheme.colorScheme.error
    )
}
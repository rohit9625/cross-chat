package dev.androhit.crosschat.designsystem.buttons

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import dev.androhit.crosschat.designsystem.ui.theme.CrossChatTheme

@Composable
fun SimpleTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = LocalTextStyle.current
) {
    Text(
        text = text,
        modifier = modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onClick
        ),
        color = MaterialTheme.colorScheme.primary,
        style = textStyle
    )
}

@Preview(showBackground = true)
@Composable
private fun SimpleTextButtonPreview() {
    CrossChatTheme {
        SimpleTextButton(text = "Text Button", onClick = {})
    }
}
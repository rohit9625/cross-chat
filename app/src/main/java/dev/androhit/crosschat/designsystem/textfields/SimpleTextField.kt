package dev.androhit.crosschat.designsystem.textfields

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.androhit.crosschat.designsystem.texts.ErrorText
import dev.androhit.crosschat.designsystem.ui.theme.CrossChatTheme

@Composable
fun SimpleTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    supportingText: String? = null,
    shape: Shape = RoundedCornerShape(12.dp),
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.End) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = {
                label?.let { Text(text = label) }
            },
            singleLine = true,
            shape = shape,
        )
        ErrorText(
            error = supportingText ?: "",
            modifier = Modifier
                .padding(top = 2.dp, end = 12.dp)
        )
    }
}

@Preview
@Composable
private fun SimpleTextFieldPreview() {
    CrossChatTheme {
        Surface {
            SimpleTextField(
                value = "",
                onValueChange = {},
                label = "Email",
                supportingText = "Invalid email address",
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
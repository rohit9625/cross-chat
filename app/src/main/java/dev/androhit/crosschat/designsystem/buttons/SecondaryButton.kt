package dev.androhit.crosschat.designsystem.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.androhit.crosschat.designsystem.ui.theme.CrossChatTheme

@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(12.dp)
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        shape = shape,
        contentPadding = PaddingValues(vertical = 12.dp, horizontal = 24.dp),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.primary)
    ) {
        Text(text = text, style = MaterialTheme.typography.titleMedium)
    }
}

@Preview(showBackground = true)
@Composable
private fun SecondaryButtonPreview() {
    CrossChatTheme {
        SecondaryButton(
            text = "Secondary Button",
            onClick = { },
            modifier = Modifier.padding(16.dp)
        )
    }
}
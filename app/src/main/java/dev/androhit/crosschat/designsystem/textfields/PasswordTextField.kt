package dev.androhit.crosschat.designsystem.textfields

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.androhit.crosschat.R
import dev.androhit.crosschat.designsystem.texts.ErrorText
import dev.androhit.crosschat.designsystem.ui.theme.CrossChatTheme

@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    supportingText: String? = null,
    shape: Shape = RoundedCornerShape(12.dp),
) {
    var isPasswordVisible by rememberSaveable {
        mutableStateOf(false)
    }
    val toggleIcon by remember {
        derivedStateOf {
            if(isPasswordVisible) R.drawable.ic_visibility_on
            else R.drawable.ic_visibility_off
        }
    }

    Column(modifier = modifier, horizontalAlignment = Alignment.End) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = {
                label?.let { Text(text = label) }
            },
            trailingIcon = {
                IconButton({ isPasswordVisible = !isPasswordVisible }) {
                    Icon(
                        painter = painterResource(toggleIcon),
                        contentDescription = "Toggle password visibility"
                    )
                }
            },
            singleLine = true,
            shape = shape,
            visualTransformation = if(isPasswordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            }
        )
        ErrorText(
            error = supportingText ?: "",
            modifier = Modifier
                .padding(top = 2.dp, end = 12.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PasswordTextFieldPreview() {
    CrossChatTheme {
        PasswordTextField(
            value = "myPassword",
            onValueChange = {},
            modifier = Modifier.padding(16.dp),
            label = "Password",
            supportingText = "Password is incorrect"
        )
    }
}
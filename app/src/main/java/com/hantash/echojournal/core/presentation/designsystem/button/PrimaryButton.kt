package com.hantash.echojournal.core.presentation.designsystem.button

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hantash.echojournal.core.presentation.designsystem.theme.EchoJournalTheme
import com.hantash.echojournal.core.presentation.designsystem.theme.buttonGradient

@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    name: String,
    enabled: Boolean = true,
    leadingIcon: (@Composable () -> Unit)? = null,
    onClick: () -> Unit,
) {
    val activeColor = if (enabled) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.outline
    }

    Button(
        modifier = modifier
            .background(
                brush = if (enabled) {
                    MaterialTheme.colorScheme.buttonGradient
                } else {
                    SolidColor(MaterialTheme.colorScheme.surfaceVariant)
                },
                shape = CircleShape
            ),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = activeColor
        ),
        onClick = onClick,
    ) {
        leadingIcon?.invoke()

        if (leadingIcon != null) {
            Spacer(modifier = Modifier.width(6.dp))
        }

        Text(
            text = name,
            style = MaterialTheme.typography.bodyLarge,
            color = activeColor
        )
    }
}

@Preview
@Composable
fun PreviewButtonPrimary() {
    EchoJournalTheme {
        PrimaryButton(
            name = "Primary Button",
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null
                )
            },
            onClick = {}
        )
    }
}







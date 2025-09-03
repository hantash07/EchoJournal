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
fun SecondaryButton(
    modifier: Modifier = Modifier,
    name: String,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        onClick = onClick,
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Preview
@Composable
fun PreviewSecondaryButton() {
    EchoJournalTheme {
        SecondaryButton(
            name = "Cancel",
            onClick = {}
        )
    }
}







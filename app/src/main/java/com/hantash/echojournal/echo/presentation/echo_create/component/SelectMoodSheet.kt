@file:OptIn(ExperimentalMaterial3Api::class)

package com.hantash.echojournal.echo.presentation.echo_create.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.hantash.echojournal.R
import com.hantash.echojournal.core.presentation.designsystem.button.PrimaryButton
import com.hantash.echojournal.core.presentation.designsystem.button.SecondaryButton
import com.hantash.echojournal.echo.presentation.component.MoodSelectorRow
import com.hantash.echojournal.echo.presentation.model.MoodUi

@Composable
fun SelectMoodSheet(
    modifier: Modifier = Modifier,
    selectedMood: MoodUi,
    onMoodClick: (MoodUi) -> Unit,
    onConfirmClick: () -> Unit,
    onDismiss: () -> Unit
) {
    val allMoods = MoodUi.entries.toList()
    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.how_are_you_doing),
                style = MaterialTheme.typography.titleMedium
            )

            MoodSelectorRow(
                selectedMood = selectedMood,
                onMoodClick = onMoodClick
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SecondaryButton(
                    name = stringResource(R.string.cancel),
                    onClick = onDismiss
                )
                PrimaryButton(
                    name = stringResource(R.string.confirm),
                    onClick = onConfirmClick,
                    modifier = Modifier.weight(1f),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = stringResource(R.string.confirm),
                        )
                    }
                )
            }
        }
    }
}
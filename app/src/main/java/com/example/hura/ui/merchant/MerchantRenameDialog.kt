package com.example.hura.ui.merchant

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hura.ui.theme.Spacing
import com.example.hura.ui.theme.Shapes
import com.example.hura.ui.theme.Elevation
import androidx.compose.material3.TextFieldDefaults
@Composable
fun MerchantRenameDialog(
    currentName: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var name by remember { mutableStateOf(currentName) }

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.fillMaxWidth(),
        shape = Shapes.large,
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = Elevation.level3,
        icon = null,
        title = {
            Text(
                text = "Rename Merchant",
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Merchant Name") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        cursorColor = MaterialTheme.colorScheme.primary
                    )
                )
                Text(
                    text = "This will update the name for all past and future transactions from this merchant.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(name) },
                shape = Shapes.large,
                contentPadding = PaddingValues(horizontal = Spacing.lg, vertical = Spacing.sm)
            ) {
                Text(text = "Rename")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                shape = Shapes.large,
                contentPadding = PaddingValues(horizontal = Spacing.lg, vertical = Spacing.sm)
            ) {
                Text(text = "Cancel", color = MaterialTheme.colorScheme.primary)
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewMerchantRenameDialog() {
    MaterialTheme {
        MerchantRenameDialog(
            currentName = "Amazon.com",
            onDismiss = {},
            onConfirm = {}
        )
    }
}
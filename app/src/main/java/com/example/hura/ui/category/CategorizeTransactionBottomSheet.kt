package com.example.hura.ui.category

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.hura.data.local.entity.CategoryEntity
import com.example.hura.ui.theme.Spacing
import com.example.hura.ui.theme.Shapes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorizeMerchantBottomSheet(
    merchantName: String,
    existingCategories: List<CategoryEntity>,
    onCategorySelected: (CategoryEntity) -> Unit,
    onNewCategoryCreated: (name: String, iconRes: Int) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    var selectedCategoryId by remember { mutableStateOf<Long?>(null) }
    var newCategoryName by remember { mutableStateOf("") }
    var selectedNewIcon by remember { mutableStateOf<Int?>(null) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = {
            // Drag handle properly centered using Box with contentAlignment
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Spacing.md),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 40.dp, height = 4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f))
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        Column(modifier = Modifier.padding(bottom = Spacing.xl)) {

            // --- Header ---
            Column(modifier = Modifier.padding(horizontal = Spacing.lg, vertical = Spacing.md)) {
                Text(
                    text = "Categorize Merchant",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(Spacing.xs))
                Text(
                    text = "Select or create a category for '$merchantName'",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // --- Select Category Grid ---
            Text(
                text = "SELECT CATEGORY",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = Spacing.lg, bottom = Spacing.sm)
            )
            CategoryGrid(
                categories = existingCategories,
                selectedCategoryId = selectedCategoryId,
                onCategoryClick = { category ->
                    selectedCategoryId = category.id
                    onCategorySelected(category)
                },
                modifier = Modifier.fillMaxWidth()
            )

            HorizontalDivider(
                modifier = Modifier
                    .padding(vertical = Spacing.lg)
                    .fillMaxWidth(),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )

            // --- Create New Category Section ---
            Column(modifier = Modifier.padding(horizontal = Spacing.lg)) {
                Text(
                    text = "CREATE NEW CATEGORY",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = Spacing.md)
                )

                // Text Input
                OutlinedTextField(
                    value = newCategoryName,
                    onValueChange = { newCategoryName = it },
                    shape = Shapes.medium,
                    label = { Text("Category Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(Spacing.md))

                // Icon Picker Horizontal Scroll
                IconPickerGrid(
                    icons = CategoryIcons.all.values.toList(),
                    selectedIcon = selectedNewIcon,
                    onIconClick = { selectedNewIcon = it }
                )

                Spacer(modifier = Modifier.height(Spacing.lg))

                // Action Buttons
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Spacing.md),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Cancel
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel", style = MaterialTheme.typography.labelLarge)
                    }

                    // Create & Assign
                    Button(
                        onClick = {
                            if (newCategoryName.isNotBlank() && selectedNewIcon != null) {
                                onNewCategoryCreated(newCategoryName, selectedNewIcon!!)
                                onDismiss()
                            }
                        },
                        modifier = Modifier.weight(2f)
                    ) {
                        Text("Create & Assign", style = MaterialTheme.typography.labelLarge)
                    }
                }
            }
        }
    }
}
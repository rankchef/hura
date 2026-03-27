package com.example.hura.ui.category

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.*
import com.example.hura.ui.theme.Spacing

@Composable
fun IconPickerGrid(
    icons: List<Int>,
    selectedIcon: Int?,
    onIconClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.padding(horizontal = Spacing.md),
        horizontalArrangement = Arrangement.spacedBy(Spacing.md)
    ) {
        items(icons) { iconRes ->
            val isSelected = iconRes == selectedIcon

            CategoryItem(
                iconRes = iconRes,
                label = null, // no label for picker
                isSelected = isSelected,
                shape = CircleShape,
                backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                selectedBorderColor = MaterialTheme.colorScheme.primary,
                iconTint = if (isSelected)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onSurfaceVariant,
                textColor = MaterialTheme.colorScheme.onSurfaceVariant, // unused but kept consistent
                onClick = { onIconClick(iconRes) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun IconPickerPreview() {
    var selectedIcon by remember { mutableStateOf<Int?>(null) }

    IconPickerGrid(
        icons = CategoryIcons.all.values.toList(),
        selectedIcon = selectedIcon,
        onIconClick = { selectedIcon = it }
    )
}
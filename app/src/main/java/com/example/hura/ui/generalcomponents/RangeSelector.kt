package com.example.hura.ui.generalcomponents

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.hura.domain.model.HistoryRange
import com.example.hura.ui.theme.AppTypography
import com.example.hura.ui.theme.Spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RangeSelector(
    selectedRange: HistoryRange,
    onRangeSelected: (HistoryRange) -> Unit,
    modifier: Modifier = Modifier
) {
    val options = HistoryRange.entries

    SingleChoiceSegmentedButtonRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing.md, vertical = Spacing.sm)
    ) {
        options.forEachIndexed { index, range ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = options.size
                ),
                onClick = { onRangeSelected(range) },
                selected = range == selectedRange,
                // 1. THE SECRET SAUCE: Remove the clunky checkmark animation
                icon = {},
                // 2. PREMIUM COLORS: Use Container colors for a softer, modern feel
                colors = SegmentedButtonDefaults.colors(
                    activeContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    activeContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    inactiveContainerColor = Color.Transparent,
                    inactiveContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    // Keep borders subtle so it looks like one cohesive pill
                    activeBorderColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                    inactiveBorderColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                ),
                label = {
                    Text(
                        // 3. SNAPPY TEXT: Shorten the labels for a cleaner grid
                        text = when (range) {
                            HistoryRange.LAST_7_DAYS -> "7D"
                            HistoryRange.LAST_30_DAYS -> "30D"
                            HistoryRange.THIS_MONTH -> "Mth"
                            HistoryRange.THIS_YEAR -> "Yr"
                            HistoryRange.ALL_TIME -> "All"
                        },
                        // 4. DYNAMIC TYPOGRAPHY: Bold the active item to make it pop
                        fontWeight = if (range == selectedRange) FontWeight.Bold else FontWeight.Medium,
                        style = AppTypography.labelSmall,
                        maxLines = 1
                    )
                }
            )
        }
    }
}
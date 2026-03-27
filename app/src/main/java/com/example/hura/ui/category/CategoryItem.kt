package com.example.hura.ui.category

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hura.ui.theme.IconSizes
import com.example.hura.ui.theme.Shapes
import com.example.hura.ui.theme.Spacing
import com.example.hura.R

@Composable
fun CategoryItem(
    iconRes: Int? = null, // 1. Make nullable
    label: String? = null,
    isSelected: Boolean = false,
    shape: Shape = Shapes.large,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    selectedBorderColor: Color = MaterialTheme.colorScheme.primary,
    iconTint: Color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
    textColor: Color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(if (isSelected) 0.95f else 1f, label = "scaleAnimation")

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Spacing.xs),
        modifier = Modifier
            .clickable { onClick() }
            .scale(scale)
            .animateContentSize()
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(color = backgroundColor, shape = shape)
                .border(
                    // Using a very small width or transparent color for 0.dp
                    // is safer than 0.dp border in some Compose versions
                    width = if (isSelected) 2.dp else 0.dp,
                    color = if (isSelected) selectedBorderColor else Color.Transparent,
                    shape = shape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                // 2. Use Elvis operator for the default value
                painter = painterResource(id = iconRes ?: R.drawable.ic_support),
                contentDescription = label ?: "Category icon",
                tint = iconTint,
                modifier = Modifier.size(IconSizes.large)
            )
        }

        if (label != null) {
            Text(
                text = label,
                fontSize = MaterialTheme.typography.labelSmall.fontSize,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = textColor
            )
        }
    }
}

@Preview
@Composable
fun CategoryItemPreview(){
    CategoryItem(
        label = "McDonalds",
        isSelected = true
    ) { }
}
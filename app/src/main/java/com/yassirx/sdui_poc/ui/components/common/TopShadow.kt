package com.arbin.arbincommon.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TopShadow(
    modifier: Modifier = Modifier,
    height: Int = 8
) {
    Box(
        modifier = modifier.fillMaxWidth()
            .height(height = height.dp)
            .background(brush = Brush.verticalGradient(
                colors = listOf(
                    Color.Black.copy(alpha = 0f),
                    Color.Black.copy(alpha = 0.00f),
                    Color.Black.copy(alpha = 0.01f),
                    Color.Black.copy(alpha = 0.02f),
                    Color.Black.copy(alpha = 0.03f),
                    Color.Black.copy(alpha = 0.05f),
                    Color.Black.copy(alpha = 0.07f),
                    Color.Black.copy(alpha = 0.08f),
                    Color.Black.copy(alpha = 0.09f),
                    Color.Black.copy(alpha = 0.1f),
                )
            ))
    )
}
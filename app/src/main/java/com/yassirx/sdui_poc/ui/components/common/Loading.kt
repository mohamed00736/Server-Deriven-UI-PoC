package com.arbin.arbincommon.ui.components

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun LoadingView(
    modifier: Modifier = Modifier
){
    Box(modifier = modifier.fillMaxSize()){
        CircularProgress(
            color = Color.White,
            strokeWidth = 3.dp,
            modifier = Modifier.size(50.dp).align(Alignment.Center)
        )
    }
}

@Composable
fun CircularProgress(
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 2.dp,
    color: Color = Color.White,
    roundDuration : Int = 600,
) {
    val infiniteTransition = rememberInfiniteTransition()
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = roundDuration
            }
        )
    )
    CircularProgressIndicator(
        progress = 1f,
        modifier = modifier
            .rotate(angle)
            .border(
                strokeWidth,
                brush = Brush.sweepGradient(
                    listOf(
                        Color.Transparent,
                        color.copy(0.1f),
                        color.copy(0.5f),
                        color
                    )
                ),
                shape = CircleShape
            ),
        strokeWidth = strokeWidth,
        color = Color.Transparent
    )
}

@Composable
fun LinearProgress(
    modifier: Modifier = Modifier,
    color: Color = Color.White,
) {
    LinearProgressIndicator(
        modifier = modifier
            .wrapContentWidth()
            .clip(CircleShape),
        color = color,
        backgroundColor = Color.Transparent
    )
}
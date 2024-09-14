package com.yassirx.sdui_poc.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yassirx.sdui_poc.R

@Composable
fun LinearProgressCustom(
    modifier: Modifier = Modifier,
    progress: Float,
    backgroundColor: Color = colorResource(id = R.color.homeMenuColor),
    progressColor: Color = if (progress == 100f) colorResource(id = R.color.greenPrimary)
    else colorResource(id = R.color.redFavorite),
    height: Dp = 8.dp,
) {
    BoxWithConstraints(
        modifier = modifier
            .height(height)
            .padding(horizontal = 0.dp)
            .clip(CircleShape)
            .border(
                BorderStroke(1.dp, colorResource(id = R.color.homeMenuColor)),
                shape = CircleShape
            )
            .fillMaxWidth()
            .background(color = backgroundColor)
    ) {
        val progressWidth = with(LocalDensity.current) { progress * maxWidth.value / 100 }
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(if (progress == 0f) 8.dp else progressWidth.dp)
                .border(
                    BorderStroke(1.dp, colorResource(id = R.color.redFavorite)),
                    shape = CircleShape
                )
                .clip(CircleShape)
                .background(color = progressColor)
        )
    }
}
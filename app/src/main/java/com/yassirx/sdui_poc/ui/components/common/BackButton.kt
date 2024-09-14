package com.arbin.arbincommon.ui.components.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.yassirx.sdui_poc.R


@Composable
fun BackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    withShadow: Boolean = true
) {
    Box(
        modifier = modifier
            .wrapContentSize(Alignment.Center)
            .background(Color.White, CircleShape)
            .then(
                if (withShadow) Modifier.shadow(elevation = 15.dp, shape = CircleShape)
                else Modifier
            )
            .clip(CircleShape)
            .clickable(onClick = onClick)
    ) {
        Surface(
            modifier = Modifier
                .size(50.dp)
                .background(Color.White, CircleShape)
                .padding(13.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back_arrow),
                contentDescription = "backButton",
            )
        }
    }
}
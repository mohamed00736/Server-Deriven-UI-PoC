package com.arbin.arbincommon.ui.components.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
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
fun CloseButton(
    modifier: Modifier = Modifier,
    onClose: () -> Unit
){
    Box(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .statusBarsPadding()
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .shadow(elevation = 10.dp, shape = CircleShape)
                .clip(CircleShape)
                .clickable(onClick = onClose)
                .background(Color.White, CircleShape)
                .align(Alignment.Center),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_close_circle),
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(28.dp)
            )
        }
    }
}
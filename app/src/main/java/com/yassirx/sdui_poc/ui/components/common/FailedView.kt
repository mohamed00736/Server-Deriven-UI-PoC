package com.arbin.arbincommon.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yassirx.sdui_poc.R


@Composable
fun FailedView(
    onRetry: (() -> Unit)? = null,
    retryable: Boolean = false,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                modifier = Modifier.size(90.dp),
                painter = painterResource(id = R.drawable.ic_close_circle),
                contentDescription = null,
                tint = colorResource(id = R.color.white)
            )
            Row(
                modifier = Modifier.padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "${stringResource(R.string.something_went_wrong)}, ",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W400,
                )
                if (retryable) {
                    Text(
                        modifier = Modifier.clickable(onClick = { onRetry?.invoke() }),
                        text = stringResource(R.string.retry).lowercase(),
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.W600,
                        style = TextStyle(textDecoration = TextDecoration.Underline)
                    )
                }

            }
        }
    }

}
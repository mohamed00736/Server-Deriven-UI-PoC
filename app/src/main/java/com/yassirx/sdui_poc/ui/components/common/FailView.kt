package com.arbin.arbincommon.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.yassirx.sdui_poc.R

@Composable
fun FailView(
    modifier: Modifier = Modifier,
    textColor: Color = Color.White,
    message: String = "Something went wrong!",
    retryOption: Boolean = true,
    onRetry: (() -> Unit)? = null
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = message,
            color = textColor,
        )
        if (retryOption) {
            Button(
                onClick = { onRetry?.invoke() },
            ) {
                Text(
                    text = stringResource(id = R.string.retry),
                    color = Color.White,
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}
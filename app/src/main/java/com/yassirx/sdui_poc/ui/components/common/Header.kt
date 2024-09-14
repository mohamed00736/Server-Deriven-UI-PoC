package com.harbin.vtcdrivertransport.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arbin.arbincommon.ui.components.buttons.BackButton
import com.yassirx.sdui_poc.R
import com.yassirx.sdui_poc.toNiceFormat
import com.yassirx.sdui_poc.ui.components.LinearProgressCustom


@Composable
fun TopViewHeader(
    modifier: Modifier = Modifier,
    title: String,
    trailing: (@Composable () -> Unit)? = null,
    onBack: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        colorResource(id = R.color.homeMenuColor).copy(alpha = 1f),
                        colorResource(id = R.color.homeMenuColor).copy(alpha = 0.9f),
                        colorResource(id = R.color.homeMenuColor).copy(alpha = 0.9f),
                        colorResource(id = R.color.homeMenuColor).copy(alpha = 0.8f),
                        colorResource(id = R.color.homeMenuColor).copy(alpha = 0.8f),
                        colorResource(id = R.color.homeMenuColor).copy(alpha = 0.6f),
                        colorResource(id = R.color.homeMenuColor).copy(alpha = 0f)
                    )
                )
            )
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 10.dp, bottom = 20.dp)
                .statusBarsPadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                BackButton(onClick = onBack)
                Spacer(modifier = Modifier.width(20.dp))
                Text(
                    text = title,
                    fontSize = 22.sp,
                    color = Color.White,
                    fontWeight = FontWeight.W600,
                    modifier = Modifier.weight(1f)
                )
                trailing?.invoke()
            }
        }
    }
}

@Composable
fun OnBoardingViewHeader(
    modifier: Modifier = Modifier,
    title: String,
    progress: Double,
    trailing: (@Composable () -> Unit)? = null,
    onBack: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(16.dp)
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(
                    color = Color.White,
                    shape = CircleShape
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            BackButton(onClick = onBack, withShadow = false)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = title,
                        fontSize = 18.sp,
                        maxLines = 1,
                        color = colorResource(id = R.color.redFavorite),
                        fontWeight = FontWeight.W600,
                        modifier = Modifier.weight(1f),
                    )
                    Text(
                        text = "${progress.toNiceFormat(aroundUp = 0)}%",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.W200,
                        color = colorResource(id = R.color.black).copy(0.5f),
                        textAlign = TextAlign.End
                    )
                }
                LinearProgressCustom(
                    progress = progress.toFloat(),
                    progressColor = colorResource(id = R.color.redFavorite)
                )
            }
            Spacer(modifier = Modifier.size(16.dp))
            trailing?.invoke()
        }
    }
}
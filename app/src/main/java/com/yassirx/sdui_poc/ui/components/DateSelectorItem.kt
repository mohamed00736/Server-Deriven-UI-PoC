package com.yassirx.sdui_poc.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yassirx.sdui_poc.ui.helper.datePicker
import com.yassirx.sdui_poc.R
import com.yassirx.sdui_poc.localize
import com.yassirx.sdui_poc.model.*

@Composable
fun DateSelectorItem(
    data: OnboardingComponent,
    onDateSelected: (String) -> Unit,
){
    val context = LocalContext.current
    val dateValue = remember { mutableStateOf(data.displayedValue ?: data.value) }
    val datePicker = datePicker(context = context) {
        onDateSelected(it)
        dateValue.value = it
    }
    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .background(Color.White, RoundedCornerShape(12.dp))
                .clickable(onClick = datePicker::show, enabled = data.enabled)
                .alpha(if (data.enabled) 1f else 0.5f)
                .border(
                    width = if (data.isRejected) 1.dp else 0.dp,
                    color = if (data.isRejected) colorResource(id = R.color.redPrimary) else Color.Transparent,
                    shape = RoundedCornerShape(12.dp),
                )
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = dateValue.value ?: data.title?.localize(context) ?: "",
                fontSize = 16.sp,
                fontWeight = FontWeight.W400,
                color = data.displayedValue?.let { Color.Black } ?: Color.Black.copy(alpha = 0.5f)
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_calendar),
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.size(25.dp)
            )
        }
        if (data.isRejected || data.isPending || data.isAccepted) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Icon(
                    modifier = Modifier.size(16.dp),
                    painter = if (data.isRejected) painterResource(id = R.drawable.ic_close_circle)
                    else if (data.isAccepted) painterResource(id = R.drawable.ic_tick_circle)
                    else painterResource(id = R.drawable.ic_info_circle),
                    contentDescription = null,
                    tint = if (data.isRejected) colorResource(id = R.color.redPrimary)
                    else if (data.isAccepted) colorResource(id = R.color.greenPrimary)
                    else colorResource(id = R.color.yellowPrimary),
                )
                Text(
                    text = (if (data.isRejected) {
                        if (!data.message.isNullOrBlank()) data.message else stringResource(id = R.string.rejected)
                    } else if (data.isAccepted) stringResource(id = R.string.accepted)
                    else stringResource(id = R.string.pending)) ?: "",
                    color = if (data.isRejected) colorResource(id = R.color.redPrimary)
                    else if (data.isAccepted) colorResource(id = R.color.greenPrimary)
                    else colorResource(id = R.color.yellowPrimary),
                    fontWeight = FontWeight.W600,
                    fontSize = 14.sp,
                    maxLines = 2,
                )
            }
        }
    }
}
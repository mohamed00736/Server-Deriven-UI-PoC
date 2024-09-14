package com.harbin.vtcdrivertransport.ui.onBoarding.flow.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yassirx.sdui_poc.R
import com.yassirx.sdui_poc.localize
import com.yassirx.sdui_poc.model.*


enum class Gender(val value: String) {
    MALE("male"),
    FEMALE("female"),
}

@Composable
fun GenderItem(
    data: OnboardingComponent,
    onSelect: (String) -> Unit,
) {
    val context = LocalContext.current
    val radioOptions = listOf(Gender.MALE.value, Gender.FEMALE.value)
    val (selectedOption, onOptionSelected) = remember {
        mutableStateOf<String?>(null)
    }
    LaunchedEffect(Unit) {
        data.value?.let {
            onOptionSelected(it)
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(12.dp))
                .alpha(if (data.enabled) 1f else 0.5f)
                .border(
                    width = if (data.isRejected) 1.dp else 0.dp,
                    color = if (data.isRejected) colorResource(id = R.color.redPrimary) else Color.Transparent,
                    shape = RoundedCornerShape(12.dp),
                )
        ) {
            radioOptions.forEach { text ->
                Row(
                    Modifier
                        .height(55.dp)
                        .selectable(
                            selected = (text == selectedOption),
                            onClick = {
                                onOptionSelected(text)
                                onSelect(text)
                            },
                            role = Role.RadioButton,
                            enabled = data.enabled,
                        )
                        .padding(horizontal = 16.dp)
                        .weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (text == selectedOption),
                        onClick = null
                    )
                    Text(
                        text = text.localize(context),
                        style = MaterialTheme.typography.body1.merge(),
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
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
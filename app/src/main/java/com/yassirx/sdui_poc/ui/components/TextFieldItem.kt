package com.yassirx.sdui_poc.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
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
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import com.yassirx.sdui_poc.R
import com.yassirx.sdui_poc.localize
import com.yassirx.sdui_poc.model.*

@Composable
fun TextFieldItem(
    modifier: Modifier = Modifier,
    data: OnboardingComponent,
    imeAction: ImeAction = ImeAction.Done,
    hasVisualTransformation: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    onTextChange: (String) -> Unit,
) {
    val context = LocalContext.current
    val textState: TextFieldState = remember { TextFieldState() }
    LaunchedEffect(Unit) {
        textState.text =
            (if (hasVisualTransformation) {
                data.value?.let {
                    if (it.contains("-")) data.value?.replace("-", "")
                    else data.value?.replace("・", "")
                } ?: ""
            } else data.value) ?: ""
    }

    // This mask is used to format the licence plate number
    val mask = remember {
        mutableStateOf("00000-000-00")
    }

    //We change the mask based on the initial value of the licence plate number
    if (hasVisualTransformation) {
        mask.value = if (textState.text.length > 10) "000000-000-00" else "00000-000-00"
    }

    // This transformation is used to transform visually the licence plate number
    val transformation =
        TextFieldVisualTransformation(
            mask.value,
            '0'
        )
    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        OutlinedTextField(
            modifier = modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(12.dp))
                .alpha(if (data.enabled) 1f else 0.5f),
            value = textState.text,
            onValueChange = {
                if (hasVisualTransformation) {
                    if (it.length <= 11 && it.isDigitsOnly()) {
                        mask.value = if (it.length > 10) "000000-000-00" else "00000-000-00"
                        onTextChange(
                            getTransformedText(it, mask.value)
                        )
                        textState.text = it
                    }
                } else {
                    onTextChange(it)
                    textState.text = it
                }
            },
            placeholder = {
                Text(text = data.title?.localize(context) ?: "")
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = imeAction, keyboardType = keyboardType),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = Color.Black,
                disabledTextColor = Color.Black,
                backgroundColor = Color.White,
                unfocusedBorderColor = if (data.isRejected) colorResource(id = R.color.redPrimary) else Color.Transparent,
            ),
            shape = RoundedCornerShape(12.dp),
            enabled = data.enabled,
            visualTransformation = if (hasVisualTransformation) transformation else VisualTransformation.None,
        )
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

// This function is used to get the transformed text of the licence plate
private fun getTransformedText(text: String, mask: String): String {
    val dashIndexes = if (mask == "00000-000-00") listOf(4, 7) else listOf(5, 8)
    val annotatedString = buildAnnotatedString {
        for (i in text.indices) {
            val char = text[i]
            append(char.toString())
            if (dashIndexes.contains(i)) {
                append("-")
            }
        }
    }
    return annotatedString.text
}
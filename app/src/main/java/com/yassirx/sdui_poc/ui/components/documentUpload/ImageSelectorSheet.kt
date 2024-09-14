package com.yassirx.sdui_poc.ui.components.documentUpload

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.arbin.arbincommon.ui.components.buttons.CloseButton
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Camera
import compose.icons.fontawesomeicons.solid.Images
import com.yassirx.sdui_poc.R


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ImageSelectorSheet(
    onSelectCamera: () -> Unit,
    onSelectGallery: () -> Unit,
    onClose: () -> Unit,
    sheetState: ModalBottomSheetState,
) {
    BottomSheetWrapper(
        sheetState = sheetState
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .wrapContentHeight()
                    .background(Color.Gray, CircleShape)
                    .clip(CircleShape)
                    .clickable(onClick = onSelectCamera)
                    .padding(vertical = 25.dp)
            ) {
                Icon(
                    imageVector = FontAwesomeIcons.Solid.Camera,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .size(40.dp)
                        .align(Alignment.Center)
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .wrapContentHeight()
                    .background(colorResource(id = R.color.yellowPrimary), CircleShape)
                    .clip(CircleShape)
                    .clickable(onClick = onSelectGallery)
                    .padding(vertical = 25.dp)
            ) {
                Icon(
                    imageVector = FontAwesomeIcons.Solid.Images,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .size(40.dp)
                        .align(Alignment.Center)
                )
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
        CloseButton(onClose = onClose , modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(bottom = 10.dp))
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetWrapper(
    modifier: Modifier = Modifier,
    sheetState: ModalBottomSheetState,
    onExpanded: (() -> Unit)? = null,
    onHidden: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    LaunchedEffect(sheetState.currentValue) {
        when (sheetState.currentValue) {
            ModalBottomSheetValue.Hidden -> onHidden?.invoke()
            ModalBottomSheetValue.Expanded -> onExpanded?.invoke()
            ModalBottomSheetValue.HalfExpanded -> sheetState.show()
            else -> Unit
        }
    }
    ModalBottomSheetLayout(
        modifier = modifier.background(Color.Transparent),
        sheetContent = {
            content()
        },
        sheetState = sheetState,
        sheetBackgroundColor = Color.Transparent,
        sheetElevation = 0.dp,
        scrimColor = Color.Black.copy(alpha = 0.6f),
    ) {}
}
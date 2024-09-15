package com.harbin.vtcdrivertransport.ui.onBoarding.flow.onBoardingPagesFlow

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel


import com.arbin.arbincommon.ui.components.CircularProgress
import com.arbin.arbincommon.ui.components.buttons.HarbinButton

import com.harbin.vtcdrivertransport.ui.component.OnBoardingViewHeader

import com.harbin.vtcdrivertransport.ui.onBoarding.flow.components.GenderItem

import com.harbin.vtcdrivertransport.ui.onBoarding.flow.components.documentUpload.DocumentUploadItem
import com.yassirx.sdui_poc.R
import com.yassirx.sdui_poc.isValidEmail
import com.yassirx.sdui_poc.isValidLicensePlate
import com.yassirx.sdui_poc.localize
import com.yassirx.sdui_poc.ui.UiState
import com.yassirx.sdui_poc.ui.components.ComponentType
import com.yassirx.sdui_poc.ui.components.DateSelectorItem
import com.yassirx.sdui_poc.ui.components.TextFieldItem
import com.yassirx.sdui_poc.ui.components.documentUpload.ImageSelectorSheet
import kotlinx.coroutines.launch


@SuppressLint("UnrememberedMutableInteractionSource")
@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun OnBoardingFlowScreen(
    viewModel: OnBoardingFlowViewModel = hiltViewModel(),
    groupIndex: Int?,
    onViewImage: (String) -> Unit ={},
    onSelectCar: () -> Unit={},
    onCompleteSuccessfully: (String, Boolean, Boolean) -> Unit= { s: String, b: Boolean, b1: Boolean -> },
    onBack: (Boolean) -> Unit={},
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val imageSelectorSheet = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )
    val selectedGroup = viewModel.selectedGroup.collectAsState()
    val selectedPage = viewModel.selectedPage.collectAsState()
    val progress = viewModel.progress.collectAsState()
    val canPassToNextPage = viewModel.canPassToNextPage.collectAsState()
    val submitUiState = viewModel.uiState.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        groupIndex?.let {
            viewModel.setPreselectedGroup(it)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.homeMenuColor))
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null
            ) {
                keyboardController?.hide()
            }
    ) {
        OnBoardingViewHeader(
            title = selectedGroup.value?.title?.localize(context) ?: "",
            progress = progress.value ?: 0.0
        ) {
            if (viewModel.popBack()) onBack(false)
        }
        LazyColumn(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
        ) {
            item {
                selectedPage.value?.title?.let {

                    Text(
                        modifier = Modifier.padding(top = 30.dp, bottom = 20.dp),
                        text = it.localize(context),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                    )
                }
                selectedPage.value?.description?.let {
                    Text(
                        modifier = Modifier.padding(bottom = 20.dp),
                        text = it.localize(context),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                    )
                }
                Spacer(modifier = Modifier.size(15.dp))
            }
            selectedPage.value?.components?.let {
                itemsIndexed(it) { index, item ->
                    when (item.type) {
                        ComponentType.Document.value -> {
                            DocumentUploadItem(
                                key = item.key.toString(),
                                data = item,
                                onViewImage = onViewImage,
                            ) { onSelectGallery, onSelectCamera ->
                                keyboardController?.hide()
                                scope.launch {
                                    viewModel.onSelectGallery = onSelectGallery
                                    viewModel.onSelectCamera = onSelectCamera
                                    imageSelectorSheet.show()
                                }
                            }
                            Spacer(modifier = Modifier.height(15.dp))
                        }

                        ComponentType.Text.value -> {
                            item.key?.let { key ->
                                TextFieldItem(
                                    data = item,
                                    keyboardType = when (key) {
                                        "email" -> KeyboardType.Email
                                        "licence_plate" -> KeyboardType.Number
                                        else -> KeyboardType.Text
                                    },
                                    hasVisualTransformation = key == "licence_plate"
                                ) { value ->
                                    when (key) {
                                        "email" -> if (value.isValidEmail()) viewModel.updateDataSourceValue(
                                            key = key,
                                            value = value
                                        )

                                        "licence_plate" -> if (value.isValidLicensePlate()) viewModel.updateDataSourceValue(
                                            key = key,
                                            value = value
                                        )

                                        else -> viewModel.updateDataSourceValue(
                                            key = key,
                                            value = value
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(15.dp))
                        }

                        ComponentType.Gender.value -> {
                            GenderItem(
                                data = item,
                            ) { value ->
                                keyboardController?.hide()
                                item.key?.let { key ->
                                    viewModel.updateDataSourceValue(
                                        key = key,
                                        value = value,
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(15.dp))
                        }

                        ComponentType.Date.value -> {
                            DateSelectorItem(
                                data = item
                            ) { value ->
                                keyboardController?.hide()
                                item.key?.let { key ->
                                    viewModel.updateDataSourceValue(
                                        key = key,
                                        value = "${value}T00:00",
                                        displayedValue = value
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(15.dp))
                        }

                    }
                }
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        HarbinButton(
            backgroundColor = MaterialTheme.colors.primaryVariant,
            onClick = viewModel::next,
            shape = CircleShape,
            height = 55,
            enabled = true ,
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(vertical = 10.dp)
                .padding(horizontal = 16.dp)
        ) {
            when (submitUiState.value) {
                UiState.Loading -> CircularProgress(
                    modifier = Modifier.size(35.dp),
                    color = Color.Black
                )

                is UiState.Success -> {
                    LaunchedEffect(Unit) {
                        onCompleteSuccessfully("USER.ON_BOARDING", true, true)
                    }
                }

                else -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        Text(
                            text = stringResource(id = R.string.next),
                            modifier = Modifier.align(Alignment.Center),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.W600,
                            color = Color.Black
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.ic_next),
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier
                                .height(30.dp)
                                .wrapContentWidth()
                                .align(Alignment.CenterEnd)
                                .padding(end = 20.dp),
                        )
                    }
                }
            }
        }
        when (submitUiState.value) {
            is UiState.Fail, is UiState.Error -> {
//                ArbinToastUtils.SweetError(
//                    message = stringResource(R.string.something_went_wrong),
//                    duration = Toast.LENGTH_LONG,
//                    padding = PaddingValues(top = 16.dp),
//                    contentAlignment = Alignment.TopCenter
//                )
            }

            else -> Unit
        }
    }

    ImageSelectorSheet(
        onSelectCamera = {
            scope.launch {
                viewModel.onSelectCamera?.invoke()
                imageSelectorSheet.hide()
            }
        },
        onSelectGallery = {
            scope.launch {
                viewModel.onSelectGallery?.invoke()
                imageSelectorSheet.hide()
            }
        },
        onClose = {
            scope.launch {
                imageSelectorSheet.hide()
            }
        },
        sheetState = imageSelectorSheet,
    )
}
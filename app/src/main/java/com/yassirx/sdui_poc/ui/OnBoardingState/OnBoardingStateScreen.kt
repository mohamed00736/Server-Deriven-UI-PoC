package com.yassirx.sdui_poc.ui.OnBoardingState

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

import com.arbin.arbincommon.ui.components.FailedView
import com.arbin.arbincommon.ui.components.LoadingView
import com.arbin.arbincommon.ui.components.buttons.HarbinButton
import com.yassirx.sdui_poc.R
import com.yassirx.sdui_poc.localize
import com.yassirx.sdui_poc.model.*

import com.yassirx.sdui_poc.ui.UiState
import com.harbin.vtcdrivertransport.ui.component.TopViewHeader
import com.yassirx.sdui_poc.ui.components.LinearProgressCustom


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun OnBoardingStateScreen(
    status: String = "onbb",
    viewModel: OnBoardingStateViewModel = hiltViewModel(),
    onGroupSelected: (index: Int) -> Unit={},
    onBack: () -> Unit={},
) {
    val context = LocalContext.current
    val uiState = viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        if (status == "onbb") {
            viewModel.getOnBoardingStatus()
        } else if (status == "ol") {
          //  viewModel.checkRecruitedBy(context)
            viewModel.updateDataStatusForInactiveCase()
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.homeMenuColor))
    ) {
        TopViewHeader(title = "", onBack = onBack)
        when (val data = uiState.value) {
            is UiState.Success -> {
                (data.data as? OnboardingData)?.let { listOfGroups ->
                    listOfGroups.let {
                        if (status == "onb") {
                            OnBoardingBody(groupList = it.groups, onGroupSelected = onGroupSelected)
                        } else {
                            Box(Modifier.fillMaxSize()) {
                                InactiveBody(viewModel = viewModel, groupList = it.groups)
                                HarbinButton(
                                    backgroundColor = MaterialTheme.colors.primaryVariant,
                                    onClick = {
                                        onGroupSelected(0)
                                    },
                                    shape = CircleShape,
                                    height = 55,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp)
                                        .align(Alignment.BottomCenter)
                                        .statusBarsPadding()
                                        .padding(vertical = 10.dp)
                                ) {
                                    Text(
                                        text = if (viewModel.buttonTextCheck.value) stringResource(R.string.button_continue) else stringResource(
                                            R.string.on_boarding_button_start
                                        ),
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.W600,
                                        color = Color.Black
                                    )
                                }
                            }
                        }
                    }
                }
            }

            is UiState.Loading -> {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxSize()
                        .padding(bottom = 60.dp)
                ) {
                    LoadingView(
                        modifier = Modifier
                            .size(40.dp)
                            .align(Alignment.Center)
                    )
                }
            }

            is UiState.Fail, is UiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 80.dp)
                ) {
                    FailedView(
                        modifier = Modifier.fillMaxSize(),
                        onRetry = viewModel::getOnBoardingStatus,
                        retryable = true
                    )
                }
            }

            else -> Unit
        }
    }
}

@Composable
fun InactiveBody(viewModel: OnBoardingStateViewModel, groupList: List<OnboardingGroup>) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .wrapContentHeight()
            .fillMaxWidth()
            .background(color = Color.White, shape = RoundedCornerShape(16.dp))
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(top = 16.dp)
                .wrapContentHeight()
                .padding(horizontal = 16.dp)
                .background(color = Color.White, RoundedCornerShape(16.dp))
        ) {
            item {
                Text(
                    modifier = Modifier
                        .padding(start = 0.dp, top = 10.dp),
                    text = stringResource(R.string.get_started_with_on_boarding),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.black)
                )
                Text(
                    text = stringResource(R.string.before_getting_start_onboarding_description),
                    modifier = Modifier.padding(vertical = 12.dp, horizontal = 0.dp),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = colorResource(id = R.color.black)
                )
            }
            items(groupList) { group ->
                GroupProgressItem(context, group = group, progress = group.progress * 100)
                if (group.progress > 0.1 && !viewModel.buttonTextCheck.value) {
                    viewModel.buttonTextCheck.value = true
                }
            }
        }

        Spacer(
            modifier = Modifier
                .padding(vertical = 12.dp)
                .height(1.dp)
                .fillMaxWidth()
                .background(color = colorResource(id = R.color.black).copy(0.1f))
        )
        Text(
            text = stringResource(R.string.after_omplete_onboarding_description),
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp),
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = colorResource(id = R.color.black).copy(0.5f)
        )
    }
}

@Composable
fun OnBoardingBody(
    groupList: List<OnboardingGroup>,
    onGroupSelected: (index: Int) -> Unit
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
    ) {
        LazyColumn(
            modifier = Modifier
                .wrapContentHeight()
                .padding(horizontal = 16.dp)
        ) {
            item {
                Text(
                    modifier = Modifier
                        .padding(start = 0.dp, top = 10.dp),
                    text = stringResource(R.string.awesome),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = stringResource(R.string.onboarding_finalized_successfully),
                    modifier = Modifier.padding(vertical = 12.dp, horizontal = 0.dp),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White
                )
            }
            itemsIndexed(groupList) { index, group ->
                OnBoardingGroupItem(context, progress = group.progress * 100, group = group) {
                    onGroupSelected(index)
                }
            }
        }

        Text(
            text = stringResource(id = R.string.after_omplete_onboarding_description),
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp),
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = colorResource(id = R.color.white).copy(0.5f)
        )
    }
}

@Composable
private fun OnBoardingGroupItem(
    context: Context,
    progress: Float = 0f,
    group: OnboardingGroup,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .background(color = Color.White, shape = RoundedCornerShape(12.dp))
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 0.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_tick_circle),
                    contentDescription = null,
                    tint = if (group.isApproved) colorResource(id = R.color.greenPrimary)
                    else Color.Gray.copy(0.7f),
                    modifier = Modifier
                        .size(24.dp)
                        .padding(end = 4.dp)
                        .align(Alignment.CenterVertically)
                )
                Text(
                    text = group.title.localize(context),
                    fontSize = 16.sp,
                    maxLines = 1,
                    color = Color.Black.copy(0.6f),
                    fontWeight = FontWeight.W600,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "${progress.toInt()}%",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W400,
                    color = colorResource(id = R.color.black).copy(0.4f),
                    textAlign = TextAlign.End,
                )
            }
            LinearProgressCustom(
                progress = progress,
                progressColor = if (group.hasRejectedItems) colorResource(id = R.color.redPrimary)
                else if (group.isApproved) colorResource(id = R.color.greenPrimary)
                else colorResource(id = R.color.yellowPrimary),
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Spacer(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .background(color = colorResource(id = R.color.black).copy(0.1f))
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (group.hasRejectedItems) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_info_circle),
                    contentDescription = null,
                    tint = colorResource(id = R.color.redFavorite),
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.CenterVertically)
                )
            }
            Text(
                text = if (!group.hasRejectedItems) stringResource(R.string.view_documents) else stringResource(
                    R.string.missing_documents
                ),
                fontSize = 16.sp,
                maxLines = 1,
                color = if (!group.hasRejectedItems) Color.Black
                else colorResource(id = R.color.redFavorite),
                textDecoration = if (group.hasRejectedItems) TextDecoration.Underline else TextDecoration.None,
                fontWeight = FontWeight.W500,
                modifier = Modifier.weight(1f)
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_forward),
                contentDescription = null,
                modifier = Modifier.width(20.dp)
            )
        }
    }
    Spacer(modifier = Modifier.padding(8.dp))
}

@Composable
private fun GroupProgressItem(
    context: Context,
    progress: Float = 0f,
    group: OnboardingGroup,
) {
    Column(
        modifier = Modifier
            .background(Color.White)
            .padding(vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 0.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_tick_circle),
                contentDescription = null,
                tint = if (progress == 100f) colorResource(id = R.color.greenPrimary)
                else Color.Gray.copy(0.7f),
                modifier = Modifier
                    .size(24.dp)
                    .padding(end = 4.dp)
                    .align(Alignment.CenterVertically)
            )
            Text(
                text = group.title.localize(context),
                fontSize = 16.sp,
                maxLines = 1,
                color = if (progress == 100f) Color.Black.copy(0.6f)
                else colorResource(id = R.color.redFavorite),
                fontWeight = FontWeight.W600,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "${progress.toInt()}%",
                fontSize = 12.sp,
                fontWeight = FontWeight.W400,
                color = colorResource(id = R.color.black).copy(0.4f),
                textAlign = TextAlign.End,
            )
        }
        LinearProgressCustom(progress = progress)
    }
}
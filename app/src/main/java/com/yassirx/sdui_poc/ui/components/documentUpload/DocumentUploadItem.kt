package com.harbin.vtcdrivertransport.ui.onBoarding.flow.components.documentUpload

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arbin.arbincommon.ui.components.CircularProgress
import com.yassirx.sdui_poc.createImageUri
import com.yassirx.sdui_poc.R
import com.skydoves.landscapist.glide.GlideImage
import com.yassirx.sdui_poc.dashedBorder
import com.yassirx.sdui_poc.localize
import com.yassirx.sdui_poc.model.OnboardingComponent
import com.yassirx.sdui_poc.ui.UiState


@Composable
fun DocumentUploadItem(
    key: Any,
    data: OnboardingComponent,
    onViewImage: (String) -> Unit,
    onClick: (() -> Unit, () -> Unit) -> Unit,
) {
    val context = LocalContext.current
    val docState = rememberDocumentUploadState(key)
    val state = docState.uiState.collectAsState()

    LaunchedEffect(Unit) {
        docState.updateData(data)
    }

    val cameraImageUri = remember { mutableStateOf<Uri?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            docState.uploadDocument(uri = it, data = data)
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
    ) {
        if (it) {
            cameraImageUri.value?.let { path ->
                docState.uploadDocument(uri = path, data = data)
            }
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .alpha(if (data.enabled) 1f else 0.4f)
                .background(Color.Gray)
                .height(150.dp)
                .fillMaxWidth()
                .dashedBorder(
                    if (data.isRejected) 2.dp else 1.5.dp,
                    if (data.isRejected) colorResource(id = R.color.redPrimary) else Color.White,
                    12.dp
                )
                .clickable(enabled = data.enabled) {
                    onClick({ galleryLauncher.launch("image/*") }) {
                        cameraImageUri.value = createImageUri(context = context)
                        cameraImageUri.value?.let { cameraLauncher.launch(it) }
                    }
                },
            contentAlignment = Alignment.Center,
        ) {
            when(val value = state.value){
                is UiState.Success -> {
                    GlideImage(
                        modifier = Modifier
                            .padding(5.dp)
                            .size(140.dp)
                            .clip(CircleShape)
                            .clickable {
                                value.data?.let { onViewImage(it.toString()) }
                            },
                        imageModel = value.data ?: "",
                        contentScale = ContentScale.Crop,
                        loading = {
                            Box(modifier = Modifier.fillMaxSize()) {
                                CircularProgress(modifier = Modifier
                                    .size(30.dp)
                                    .align(Alignment.Center))
                            }
                        },
                        failure = {
                            Box(modifier = Modifier.fillMaxSize()) {
                                Icon(
                                    modifier = Modifier.size(40.dp).align(Alignment.Center),
                                    painter = painterResource(id = R.drawable.ic_info_circle),
                                    contentDescription = null,
                                    tint = Color.White,
                                )
                            }
                        }
                    )
                }
                UiState.Loading -> CircularProgress(modifier = Modifier.size(30.dp))
                UiState.Idle -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            modifier = Modifier.size(30.dp),
                            painter = painterResource(id = R.drawable.ic_camera),
                            contentDescription = null,
                            tint = Color.White,
                        )
                        Text(
                            text = data.title?.localize(context) ?: "",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White.copy(0.5f)
                        )
                    }
                }
                is UiState.Fail -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            modifier = Modifier.size(30.dp),
                            painter = painterResource(id = R.drawable.ic_close_circle),
                            contentDescription = null,
                            tint = Color.White,
                        )
                        Text(
                            text = stringResource(id = R.string.something_went_wrong),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White.copy(0.5f)
                        )
                    }
                }
                else -> Unit
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
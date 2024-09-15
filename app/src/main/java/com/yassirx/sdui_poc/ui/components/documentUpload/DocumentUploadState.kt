package com.harbin.vtcdrivertransport.ui.onBoarding.flow.components.documentUpload

import android.content.Context
import android.net.Uri
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext

import com.yassirx.sdui_poc.getImageRequestBody

import com.harbin.vtcdrivertransport.data.pref.DataStoreHelper

import com.yassirx.sdui_poc.R
import com.yassirx.sdui_poc.data.network.Resource
import com.yassirx.sdui_poc.data.repo.UserRepository
import com.yassirx.sdui_poc.fromJsonFile
import com.yassirx.sdui_poc.model.OnboardingComponent
import com.yassirx.sdui_poc.model.OnboardingData
import com.yassirx.sdui_poc.ui.UiState
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch


@Composable
fun rememberDocumentUploadState(
    key: Any,
    context: Context = LocalContext.current,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
) = remember(key, context, coroutineScope) {
    DocumentUploadState(context, coroutineScope)
}

@Stable
class DocumentUploadState(
    val context: Context,
    private val coroutineScope: CoroutineScope,
) {
    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Idle)
    val uiState: StateFlow<UiState> get() = _uiState

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    internal interface MyEntryPoint {
        fun repo(): UserRepository
        fun dataStore(): DataStoreHelper
    }

    private val repo: UserRepository
        get() = EntryPointAccessors.fromApplication(
            context.applicationContext,
            MyEntryPoint::class.java
        ).repo()

    private val dataStore: DataStoreHelper
        get() = EntryPointAccessors.fromApplication(
            context.applicationContext,
            MyEntryPoint::class.java
        ).dataStore()

    fun uploadDocument(
        uri: Uri,
        data: OnboardingComponent,
    ) {
        if (_uiState.value is UiState.Loading) return
        coroutineScope.launch {
            _uiState.value = UiState.Loading
            getImageRequestBody(uri, context).let { body ->
                when (
                    val result = repo.uploadFile(
                        path = data.uploadPath ?: "",
                        data = body,
                    )
                ) {
                    is Resource.Failure -> _uiState.value = UiState.Fail(result.message)
                    is Resource.Success -> {
                        _uiState.value = UiState.Success(result.value.data.url)
                        updateDataSource(
                            key = data.key!!,
                            image = result.value.data.url,
                            value = result.value.data.id.toString(),
                        )
                    }
                }
            }
        }
    }

   private fun fetchImageById(
     data: OnboardingComponent
    ) {
        if (_uiState.value is UiState.Loading) return
        coroutineScope.launch {
               _uiState.value = UiState.Loading
                when (val result = data.value?.let { repo.fetchImageById(it) }) {
                    is Resource.Failure -> _uiState.value = UiState.Fail(result.message)
                    is Resource.Success -> {

                        _uiState.value = UiState.Success(result.value)
                        data.key?.let {
                            updateDataSource(
                                key = it,
                                image = result.value,
                            )
                        }
                    }
                    else -> Unit
                }
        }
    }

    fun updateData(data: OnboardingComponent) {
        if (data.displayedValue.isNullOrBlank() && !data.value.isNullOrBlank()) {
            fetchImageById(data)
        }else {
            data.displayedValue?.let {  _uiState.value = UiState.Success(it) }
        }
    }

    private fun updateDataSource(
        key: String,
        value: String?,
        image: String?,
    ) {
        var dataSource: OnboardingData? = null
        coroutineScope.launch {
            dataStore.readOnBoardingStateData.firstOrNull()?.let { data ->
                dataSource = data
            } ?: run {
                dataSource = context.fromJsonFile(R.raw.onboarding)
            }
            dataSource?.groups?.forEach outer@ { group ->
                group.pages.forEach { page ->
                    page.components.forEach { item ->
                        if (item.key == key) {
                            item.value = value
                            item.displayedValue = image
                            return@outer
                        }
                    }
                }
            }
            dataSource?.let { data ->
                dataStore.saveOnBoardingStateData(data)
            }
        }
    }
    private fun updateDataSource(
        key: String,
        image: String?,
    ) {
        var dataSource: OnboardingData? = null
        coroutineScope.launch {
            dataStore.readOnBoardingStateData.firstOrNull()?.let { data ->
                dataSource = data
            } ?: run {
                dataSource = context.fromJsonFile(R.raw.onboarding)
            }
            dataSource?.groups?.forEach outer@ { group ->
                group.pages.forEach { page ->
                    page.components.forEach { item ->
                        if (item.key == key) {
                            item.displayedValue = image
                            return@outer
                        }
                    }
                }
            }
            dataSource?.let { data ->
                dataStore.saveOnBoardingStateData(data)
            }
        }
    }
}
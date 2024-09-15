package com.yassirx.sdui_poc.ui.OnBoardingState

import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.arbin.arbincommon.base.BaseViewModel

import com.harbin.vtcdrivertransport.data.pref.DataStoreHelper

import com.yassirx.sdui_poc.ui.UiState
import com.yassirx.sdui_poc.R
import com.yassirx.sdui_poc.data.network.Resource
import com.yassirx.sdui_poc.data.repo.UserRepository
import com.yassirx.sdui_poc.fromJsonFile
import com.yassirx.sdui_poc.model.OnboardingData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingStateViewModel @Inject constructor(
    private val application: Application,
    private val repository: UserRepository,
    private val dataStoreHelper: DataStoreHelper,

    ) : BaseViewModel<UiState>() {

    private val _listOfGroups: MutableStateFlow<OnboardingData?> = MutableStateFlow(null)
    private var alreadyUpdateDataStatusForInactiveCase = false

    val buttonTextCheck: MutableState<Boolean> = mutableStateOf(false)

    init {
        startReadDataSourceFlow()
    }

    private fun startReadDataSourceFlow() {
        viewModelScope.launch {
            dataStoreHelper.readOnBoardingStateData.collect { data ->
                data?.let {
                    _listOfGroups.emit(data)
                } ?: run {
                    _listOfGroups.value ?: run {
                        _listOfGroups.emit(application.applicationContext.fromJsonFile(R.raw.onboarding))
                    }
                }
                if (_uiState.value !is UiState.Loading) {
                    _uiState.value = UiState.Success(_listOfGroups.value)
                }
            }
        }
    }

    private fun saveOnBoardingData(groupList: OnboardingData?) {
        viewModelScope.launch {
            if (groupList != null) {
                dataStoreHelper.saveOnBoardingStateData(groupList)
            }
        }
    }

    fun updateDataStatusForInactiveCase() {
        if (!alreadyUpdateDataStatusForInactiveCase) {

            _listOfGroups.value?.groups?.forEach { group ->
                group.pages.forEach { page ->
                    page.components.forEach { component ->
                        component.status = null
                    }
                }
            }
            saveOnBoardingData(_listOfGroups.value)
            alreadyUpdateDataStatusForInactiveCase = true
        }
    }

    fun getOnBoardingStatus() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
           // delay(2000)
            _listOfGroups.value ?:
//            run {
//                dataStoreHelper.readOnBoardingStateData.firstOrNull()?.let {
//                    _listOfGroups.value = it
//                } ?:
                run {
                    _listOfGroups.value ?: run {
                        _listOfGroups.value =
                            application.applicationContext.fromJsonFile(R.raw.onboarding)
                    }
                    _uiState.value = UiState.Success(_listOfGroups.value)
                    saveOnBoardingData(_listOfGroups.value)
                }
//            }
//            when (val result = repository.getOnBoardingStatus()) {
//                is Resource.Failure -> _uiState.value = UiState.Fail()
//                is Resource.Success<*> -> {
//                    val response = result.value
//                    _listOfGroups.value?.groups?.forEach { group ->
//                        group.pages.forEach { page ->
//                            page.components.forEach { component ->
//
//                            }
//                        }
//                    }
//                    _uiState.value = UiState.Success(_listOfGroups.value)
//                    saveOnBoardingData(_listOfGroups.value)
//                }
//            }
        }
    }


}
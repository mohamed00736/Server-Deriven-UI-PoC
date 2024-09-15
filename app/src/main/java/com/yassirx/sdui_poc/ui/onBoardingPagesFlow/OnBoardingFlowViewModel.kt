package com.harbin.vtcdrivertransport.ui.onBoarding.flow.onBoardingPagesFlow

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.arbin.arbincommon.base.BaseViewModel

import com.harbin.vtcdrivertransport.data.pref.DataStoreHelper

import com.yassirx.sdui_poc.R
import com.yassirx.sdui_poc.data.network.Resource
import com.yassirx.sdui_poc.data.repo.UserRepository
import com.yassirx.sdui_poc.fromJsonFile
import com.yassirx.sdui_poc.model.OnboardingData
import com.yassirx.sdui_poc.model.OnboardingGroup
import com.yassirx.sdui_poc.model.OnboardingPage
import com.yassirx.sdui_poc.ui.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingFlowViewModel @Inject constructor(
    private val repository: UserRepository,
    private val dataStoreHelper: DataStoreHelper,
    private val application: Application,
) : BaseViewModel<UiState>() {

    private var dataSource: OnboardingData? = null

    private var alreadySetPreselectedGroup = false

    private val _selectedGroup: MutableStateFlow<OnboardingGroup?> = MutableStateFlow(null)
    val selectedGroup: StateFlow<OnboardingGroup?> get() = _selectedGroup

    private val _selectedPage: MutableStateFlow<OnboardingPage?> = MutableStateFlow(null)
    val selectedPage: StateFlow<OnboardingPage?> get() = _selectedPage

    private val _progress: MutableStateFlow<Double?> = MutableStateFlow(null)
    val progress: StateFlow<Double?> get() = _progress

    private val _canPassToNextPage: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val canPassToNextPage: StateFlow<Boolean> get() = _canPassToNextPage

    var onSelectCamera: (() -> Unit)? = null
    var onSelectGallery: (() -> Unit)? = null

    //This is used to keep track of the current page, to prevent sending multiple events on recomposition
    var pageChanged: Boolean = true

    init {
        startReadDataSourceFlow()
    }

    private fun startReadDataSourceFlow() {
        viewModelScope.launch {
            dataStoreHelper.readOnBoardingStateData.collectLatest { data ->
                data?.let {
                    dataSource = data
                } ?: run {
                    dataSource ?: run {
                        dataSource = application.applicationContext.fromJsonFile(R.raw.onboarding)
                    }
                }
                updateSelectedItems()
            }
        }
    }

    private fun updateSelectedItems() {
        _selectedGroup.value?.let {
            dataSource?.groups?.let { groups ->
                groups.indexOfFirst { e -> e.id == _selectedGroup.value?.id }.let { index ->
                    _selectedGroup.value = groups[index]
                }
            }
        } ?: run { _selectedGroup.value = dataSource?.groups?.get(0) }
        _selectedPage.value?.let {
            _selectedGroup.value?.let { group ->
                group.pages.indexOfFirst { e -> e.title == _selectedPage.value?.title }
                    .let { index ->
                        _selectedPage.value = group.pages[index]
                    }
            }
        } ?: run { _selectedPage.value = _selectedGroup.value?.pages?.get(0) }
        updateProgress()
    }

    fun next() {
        _selectedGroup.value?.let { group ->
            group.pages.let { pages ->
                pages.indexOfFirst { e -> e.title == _selectedPage.value?.title }.let { pIndex ->
                    if (pIndex < pages.size.minus(1)) {
                        _selectedPage.value = pages[pIndex.plus(1)]
                        pageChanged = true
                    } else {
                        dataSource?.groups?.let { groupList ->
                            groupList.indexOfFirst { e -> e.id == group.id }.let { gIndex ->
                                if (gIndex < groupList.size.minus(1)) {
                                    _selectedGroup.value = groupList[gIndex.plus(1)]
                                    _selectedPage.value = _selectedGroup.value?.pages?.get(0)
                                    pageChanged = true
                                } else {
                                    dataSource?.let {
//                                        if (!it.isAlreadyApplied) createApplication()
//                                        else if (it.canPatchData) patchApplication()
                                         _uiState.value = UiState.Success()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        updateProgress()
    }

    fun popBack(): Boolean {
        _selectedGroup.value?.let { group ->
            group.pages.let { pages ->
                pages.indexOfFirst { e -> e.title == _selectedPage.value?.title }.let { pIndex ->
                    if (pIndex > 0) {
                        _selectedPage.value = pages[pIndex.minus(1)]
                        updateProgress()
                        return false
                    } else {
                        dataSource?.groups?.let { groupList ->
                            groupList.indexOfFirst { e -> e.id == group.id }.let { gIndex ->
                                if (gIndex > 0) {
                                    _selectedGroup.value = groupList[gIndex.minus(1)]
                                    _selectedPage.value = _selectedGroup.value?.pages?.lastOrNull()
                                    updateProgress()
                                    return false
                                }
                            }
                        }
                    }
                }
            }
        }
        return true
    }

    fun setPreselectedGroup(index: Int) {
        if (!alreadySetPreselectedGroup) {
            _selectedGroup.value = dataSource?.groups?.get(index)
            _selectedPage.value = _selectedGroup.value?.pages?.firstOrNull()
        }
        alreadySetPreselectedGroup = true
    }

    private fun updateProgress() {
        _selectedGroup.value?.pages?.let { pages ->
            _selectedPage.value?.let { page ->
                val index = pages.indexOfFirst { e -> e.title == page.title }.plus(1).toDouble()
                val totalPages = pages.size.toDouble()
                _progress.value = index.div(totalPages) * 100
                _canPassToNextPage.value = page.canPassToNextPage
            }
        }
    }

    fun updateDataSourceValue(
        key: String,
        value: String?,
        displayedValue: String? = null,
    ) {
        dataSource?.groups?.forEach outer@{ group ->
            group.pages.forEach { page ->
                page.components.forEach { item ->
                    if (item.key == key) {
                        item.value = value
                        displayedValue?.let {
                            item.displayedValue = displayedValue
                        }
                        return@outer
                    }
                }
            }
        }
        viewModelScope.launch {
            dataSource?.let { data ->
                dataStoreHelper.saveOnBoardingStateData(data)
            }
        }
    }





    fun registerOnBoardingFlowPageEvent(pageTitle: String) {
        if (pageChanged) {

            pageChanged = false
        }
    }
}
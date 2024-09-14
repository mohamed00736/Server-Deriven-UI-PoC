package com.harbin.vtcdrivertransport.data.pref

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.yassirx.sdui_poc.fromJson

import com.yassirx.sdui_poc.model.OnboardingData
import com.yassirx.sdui_poc.toJson

import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore("OnBoardingStateDataStore")

class DataStoreHelper @Inject constructor(@ApplicationContext private val context: Context) {

    private object PreferenceKeys {
        val onBoardingStateDataKey = stringPreferencesKey("OnBoardingStateData")
    }

    private val dataStore: DataStore<Preferences> = context.dataStore

    suspend fun saveOnBoardingStateData(
        onBoardingStateData: OnboardingData
    ) {
        onBoardingStateData.toJson()?.let {
            dataStore.edit { preferences ->
                preferences[PreferenceKeys.onBoardingStateDataKey] = it
            }
        }
    }

    val readOnBoardingStateData: Flow<OnboardingData?> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val onBoardingStateData = preferences[PreferenceKeys.onBoardingStateDataKey]
            onBoardingStateData?.fromJson()
        }
}


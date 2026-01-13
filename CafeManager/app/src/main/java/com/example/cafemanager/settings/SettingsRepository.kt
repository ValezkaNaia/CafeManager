package com.example.cafemanager.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repositório de definições locais (DataStore Preferences)
 * - Guarda e lê o volume do som (0.0f..1.0f).
 */
@Singleton
class SettingsRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val KEY_SOUND_VOLUME = floatPreferencesKey("sound_volume")
        private const val DEFAULT_VOLUME = 1.0f
    }

    /** Fluxo com o volume atual (default 1.0f) */
    val soundVolumeFlow: Flow<Float> = dataStore.data.map { prefs ->
        prefs[KEY_SOUND_VOLUME] ?: DEFAULT_VOLUME
    }

    /** Atualiza e persiste o volume */
    suspend fun setSoundVolume(volume: Float) {
        val clamped = volume.coerceIn(0f, 1f)
        dataStore.edit { prefs ->
            prefs[KEY_SOUND_VOLUME] = clamped
        }
    }
}

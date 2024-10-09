package com.xtguiyi.play.store
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.xtguiyi.play.MainApplication
import com.xtguiyi.play.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.flow.stateIn
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "persistent_data")

/**
 * 全局可访问单例类（持久数据）
 * */
 object PersistentData {

    private val USER_KEY = stringPreferencesKey("user_key")
    private val AUTHORITY_KEY = stringPreferencesKey("authority_key")

    suspend fun saveUser(user: User) {
        MainApplication.context.dataStore.edit { preferences ->
            preferences[USER_KEY] = Json.encodeToString(user)
        }
    }

    val user: Flow<User?> = MainApplication.context.dataStore.data
        .map { preferences ->
            preferences[USER_KEY]?.let { Json.decodeFromString<User>(it) }
        }

    suspend fun saveToken(token: String) {
        MainApplication.context.dataStore.edit { preferences ->
            preferences[AUTHORITY_KEY] = token
        }
    }

    val token: Flow<String?> = MainApplication.context.dataStore.data
        .map { preferences ->
            preferences[AUTHORITY_KEY]
        }
}

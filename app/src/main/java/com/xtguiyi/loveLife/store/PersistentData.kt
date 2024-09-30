package com.xtguiyi.loveLife.store
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.xtguiyi.loveLife.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "persistent_data")

/**
 * 全局持久变量
 * */
internal class PersistentData(context: Context) {

    private val dataStore = context.dataStore

    companion object {
        private val USER_KEY = stringPreferencesKey("user_key")
        private val AUTHORITY_KEY = stringPreferencesKey("authority_KEY")
    }

    suspend fun saveUser(user: User) {
        dataStore.edit { preferences ->
            preferences[USER_KEY] = Json.encodeToString(user)
        }
    }

    val user: Flow<User?> = dataStore.data
        .map { preferences ->
            preferences[USER_KEY]?.let { Json.decodeFromString<User>(it) }
        }

    suspend fun saveToken(token: String) {
        dataStore.edit { preferences ->
            preferences[AUTHORITY_KEY] = token
        }
    }

    val token: Flow<String?> = dataStore.data
        .map { preferences ->
            preferences[AUTHORITY_KEY]?.let { it }
        }
}

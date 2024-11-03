package com.xtguiyi.play.store
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.xtguiyi.play.MainApplication
import com.xtguiyi.play.model.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "persistent_data")

/**
 * 全局可访问单例类（持久数据）
 * */
 object DataStoreManager {

    private val USER_INFO = stringPreferencesKey("userInfo") // 用户信息
    private val AUTHORIZATION = stringPreferencesKey("authorization") // token

    suspend fun setUserInfo(user: UserModel) {
        MainApplication.context.dataStore.edit { preferences ->
            preferences[USER_INFO] = Json.encodeToString(user)
        }
    }

    val getUserInfo: Flow<UserModel?> = MainApplication.context.dataStore.data
        .map { preferences ->
            preferences[USER_INFO]?.let { Json.decodeFromString<UserModel>(it) }
        }

    suspend fun clearUserInfo() {
        MainApplication.context.dataStore.edit { preferences ->
            preferences.remove(USER_INFO)
        }
    }

    suspend fun setToken(token: String) {
        MainApplication.context.dataStore.edit { preferences ->
            preferences[AUTHORIZATION] = token
        }
    }

    suspend fun clearToken() {
        MainApplication.context.dataStore.edit { preferences ->
            preferences.remove(AUTHORIZATION)
        }
    }

    val getToken: Flow<String?> = MainApplication.context.dataStore.data
        .map { preferences ->
            preferences[AUTHORIZATION]
        }
}

package com.example.lovelife.store
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.lovelife.entity.User
import kotlinx.coroutines.launch

class GlobalViewModel(context: Context) : ViewModel() {
    private  val preferencesRepository:PreferencesRepository by lazy {
        PreferencesRepository(context)
    }
    val user = preferencesRepository.user

    fun saveUser(user: User) {
        viewModelScope.launch {
            preferencesRepository.saveUser(user)
        }
    }

    companion object {

        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                // Get the Application object from extras
                val application = checkNotNull(extras[APPLICATION_KEY])

                return GlobalViewModel(
                    application
                ) as T
            }
        }
    }
}

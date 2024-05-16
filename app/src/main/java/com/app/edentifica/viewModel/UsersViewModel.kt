package com.app.edentifica.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.edentifica.data.model.User
import com.app.edentifica.data.retrofit.RetrofitApi.userService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class UsersViewModel : ViewModel() {
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    fun getUserByEmail(email: String) {
        viewModelScope.launch {
            try {
                val response = userService.getByEmail(email)
                if (response.isSuccessful) {
                    _user.value = response.body()
                } else {
                    // Manejar el error de la llamada Retrofit
                }
            } catch (e: Exception) {
                // Manejar errores de red u otros errores
            }
        }
    }
}
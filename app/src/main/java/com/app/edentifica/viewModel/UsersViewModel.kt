package com.app.edentifica.viewModel


import android.util.Log
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

    private val _validationOne = MutableStateFlow<Boolean>(false)
    val validationOne: StateFlow<Boolean> = _validationOne

    fun getUserByEmail(email: String) {
        viewModelScope.launch {
            try {
                val response = userService.getByEmail(email)
                if (response.isSuccessful) {
                    _user.value = response.body()
                } else {
                    Log.e("error en userViewModel", "error")
                }
            } catch (e: Exception) {
                // Manejar errores de red u otros errores
                e.message?.let { Log.e("error en userViewModel try", it) }
            }
        }
    }

    fun toDoCallByUser(user:User) {
        viewModelScope.launch {
            try {
                val response = userService.toDoCall(user)
                if (response.isSuccessful) {
                    _validationOne.value = response.body()!!
                    Log.e("validacion", "bien")
                } else {
                    Log.e("error en userViewModel", "error")
                }
            } catch (e: Exception) {
                // Manejar errores de red u otros errores
                e.message?.let { Log.e("error en userViewModel try", it) }
            }
        }
    }
}
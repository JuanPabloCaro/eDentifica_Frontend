package com.app.edentifica.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.edentifica.data.model.User
import com.app.edentifica.data.retrofit.RetrofitApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class UIStateUser(
    val listUsers: List<User> = emptyList(),
    val userSelected: User? = null
)


class UsersViewModel : ViewModel() {
    private val _uis: MutableStateFlow<UIStateUser> =  MutableStateFlow(UIStateUser())
    val uis = _uis.asStateFlow()

    init {
        getListUsers()
    }
    fun setUser(men: User){
        _uis.update {
            it.copy(userSelected = men )
        }
    }

    fun getListUsers(){
        viewModelScope.launch(Dispatchers.IO){
            val response = RetrofitApi.userService.getAllUser()
            if (response.isSuccessful){
                _uis.update {
                    it.copy(listUsers = response?.body() ?: emptyList())
                }
            }
        }
    }
}
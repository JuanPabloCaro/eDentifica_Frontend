package com.app.edentifica.ui.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.edentifica.data.model.User
import com.app.edentifica.data.retrofit.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UsersViewModel: ViewModel() {
    private val _usersStateFlow = MutableStateFlow<List<User>>(emptyList())
    val usersStateFlow: StateFlow<List<User>> = _usersStateFlow

    fun getAllUsers(){
        viewModelScope.launch(Dispatchers.IO) {
            val response = RetrofitClient.webService.getAllUsers()
            withContext(Dispatchers.Main){
                if(response.body()!!.status=="200"){
                    val _listUsers = response.body()!!.data
                    _usersStateFlow.value = _listUsers
                }
            }
        }
    }

}
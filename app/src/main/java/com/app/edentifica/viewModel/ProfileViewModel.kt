package com.app.edentifica.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.edentifica.data.model.Email
import com.app.edentifica.data.retrofit.RetrofitApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel: ViewModel() {
    private val _emailInserted = MutableStateFlow<Boolean?>(null)
    val emailInserted: StateFlow<Boolean?> = _emailInserted


    /**
     * Esta funcion recibe un Email y lo inserta en la base de datos
     */
    fun insertEmailVm(email: Email, idProfile:String) {
        viewModelScope.launch {
            try {
                val response = RetrofitApi.profileService.insertEmail(idProfile, email)
                if (response.isSuccessful) {
                    _emailInserted.value = true
                } else {
                    Log.e("error en profileViewModel", "insertEmail")
                }
            } catch (e: Exception) {
                // Manejar errores de red u otros errores
                e.message?.let { Log.e("error catch profileViewModel insert email", it) }
            }
        }
    }

}
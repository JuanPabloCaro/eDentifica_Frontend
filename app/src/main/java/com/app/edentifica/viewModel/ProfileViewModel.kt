package com.app.edentifica.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.edentifica.data.model.Email
import com.app.edentifica.data.model.Phone
import com.app.edentifica.data.model.SocialNetwork
import com.app.edentifica.data.retrofit.RetrofitApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel: ViewModel() {
    private val _emailInserted = MutableStateFlow<Boolean?>(null)
    val emailInserted: StateFlow<Boolean?> = _emailInserted

    private val _phoneInserted = MutableStateFlow<Boolean?>(null)
    val phoneInserted: StateFlow<Boolean?> = _phoneInserted

    private val _socialInserted = MutableStateFlow<Boolean?>(null)
    val socialInserted: StateFlow<Boolean?> = _socialInserted



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

    /**
     * Esta funcion recibe un Email y lo inserta en la base de datos
     */
    fun insertPhoneVm(phone: Phone, idProfile:String) {
        viewModelScope.launch {
            try {
                val response = RetrofitApi.profileService.insertPhone(idProfile, phone)
                if (response.isSuccessful) {
                    _phoneInserted.value = true
                } else {
                    Log.e("error en profileViewModel", "insertPhone")
                }
            } catch (e: Exception) {
                // Manejar errores de red u otros errores
                e.message?.let { Log.e("error catch profileViewModel insert phone", it) }
            }
        }
    }

    /**
     * Esta funcion recibe una red social y lo inserta en la base de datos
     */
    fun insertSocialNetworkVm(socialNetwork: SocialNetwork, idProfile:String) {
        viewModelScope.launch {
            try {
                val response = RetrofitApi.profileService.insertSocialNetwork(idProfile, socialNetwork)
                if (response.isSuccessful) {
                    _socialInserted.value = true
                } else {
                    Log.e("error en profileViewModel", response.isSuccessful.toString())
                }
            } catch (e: Exception) {
                // Manejar errores de red u otros errores
                e.message?.let { Log.e("error catch profileViewModel insert social", it) }
            }
        }
    }

}
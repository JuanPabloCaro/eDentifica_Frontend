package com.app.edentifica.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.edentifica.data.model.Phone
import com.app.edentifica.data.model.User
import com.app.edentifica.data.retrofit.RetrofitApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PhonesViewModel: ViewModel() {

    private val _phoneUpdated = MutableStateFlow<Boolean?>(false)
    val phoneUpdated: StateFlow<Boolean?> = _phoneUpdated

    private val _listPhones = MutableStateFlow<Set<Phone>?>(null)
    val listPhone: StateFlow<Set<Phone>?> = _listPhones

    /**
     * Esta funcion recibe un Phone y lo actualiza
     */
    fun updatePhoneVM(phone: Phone) {
        viewModelScope.launch {
            try {
                val response = RetrofitApi.phoneService.updatePhone(phone)
                if (response.isSuccessful) {
                    _phoneUpdated.value = response.body()
                } else {
                    Log.e("error en phoneViewModel", "update Phone")
                }
            } catch (e: Exception) {
                // Manejar errores de red u otros errores
                e.message?.let { Log.e("error catch phoneViewModel update", it) }
            }
        }
    }




    /**
     * Esta funcion recibe un idProfile y devuelve la lista de phones
     */
    fun getListPhones(idProfile: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitApi.phoneService.listPhonesUser(idProfile)
                if (response.isSuccessful) {
                    _listPhones.value = response.body()
                } else {
                    Log.e("error en phoneViewModel", "list Phone")
                }
            } catch (e: Exception) {
                // Manejar errores de red u otros errores
                e.message?.let { Log.e("error catch phoneViewModel list", it) }
            }
        }
    }


}
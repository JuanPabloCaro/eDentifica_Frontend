package com.app.edentifica.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.edentifica.data.model.Email
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
    val listPhones: StateFlow<Set<Phone>?> = _listPhones

    private val _phoneEdit = MutableStateFlow<Phone?>(null)
    val phoneEdit: StateFlow<Phone?> = _phoneEdit

    private val _phoneDeleted = MutableStateFlow<Boolean?>(null)
    val phoneDeleted: StateFlow<Boolean?> = _phoneDeleted

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



    /**
     * Esta funcion recibe un Phone y lo guarda
     */
    fun savePhoneEdit(phone: Phone) {
        viewModelScope.launch {
            try {
                val response = phone.id?.let { RetrofitApi.phoneService.getPhone(it) }
                if (response != null) {
                    if (response.isSuccessful) {
                        _phoneEdit.value = response.body()
                    } else {
                        Log.e("error en phoneViewModel", "edit Phone")
                    }
                }
            } catch (e: Exception) {
                // Manejar errores de red u otros errores
                e.message?.let { Log.e("error catch phoneViewModel edit", it) }
            }
        }
    }


    /**
     * Esta funcion recibe un id de Phone y lo elimina
     */
    fun deletePhoneVM(id:String) {
        viewModelScope.launch {
            try {
                val response = RetrofitApi.phoneService.deletePhone(id)
                if (response.isSuccessful) {
                    _phoneDeleted.value = response.body()
                } else {
                    Log.e("error en phoneViewModel", "delete Phone")
                }
            } catch (e: Exception) {
                // Manejar errores de red u otros errores
                e.message?.let { Log.e("error catch PhoneViewModel delete", it) }
            }
        }
    }

    /**
     * Esta funcion pone a nulo el phoneEdit
     */
    fun toNullPhoneEdit() {
        viewModelScope.launch {
            try {
                _phoneEdit.value = null
            } catch (e: Exception) {
                // Manejar errores de red u otros errores
                e.message?.let { Log.e("error catch phoneViewModel edit null", it) }
            }
        }
    }


}
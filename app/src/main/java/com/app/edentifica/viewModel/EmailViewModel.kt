package com.app.edentifica.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.edentifica.data.model.Email
import com.app.edentifica.data.model.Phone
import com.app.edentifica.data.retrofit.RetrofitApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EmailViewModel: ViewModel()  {
    private val _emailUpdated = MutableStateFlow<Boolean?>(false)
    val emailUpdated: StateFlow<Boolean?> = _emailUpdated

    private val _listEmails = MutableStateFlow<Set<Email>?>(null)
    val listEmail: StateFlow<Set<Email>?> = _listEmails


    /**
     * Esta funcion recibe un Email y lo actualiza
     */
    fun updateEmailVM(email: Email) {
        viewModelScope.launch {
            try {
                val response = RetrofitApi.emailService.updateEmail(email)
                if (response.isSuccessful) {
                    _emailUpdated.value = response.body()
                } else {
                    Log.e("error en emailViewModel", "update Email")
                }
            } catch (e: Exception) {
                // Manejar errores de red u otros errores
                e.message?.let { Log.e("error catch emailViewModel update", it) }
            }
        }
    }




    /**
     * Esta funcion recibe un idProfile y devuelve la lista de emails
     */
    fun listEmails(idProfile: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitApi.emailService.listEmailsUser(idProfile)
                if (response.isSuccessful) {
                    _listEmails.value = response.body()
                } else {
                    Log.e("error en emailViewModel", "list Email")
                }
            } catch (e: Exception) {
                // Manejar errores de red u otros errores
                e.message?.let { Log.e("error catch emailViewModel list", it) }
            }
        }
    }



}
package com.app.edentifica.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.edentifica.data.model.Phone
import com.app.edentifica.data.model.SocialNetwork
import com.app.edentifica.data.retrofit.RetrofitApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SocialViewModel: ViewModel() {
    private val _SocialNetworkUpdated = MutableStateFlow<Boolean?>(false)
    val socialNetworkUpdated: StateFlow<Boolean?> = _SocialNetworkUpdated

    private val _listSocialNetworks = MutableStateFlow<Set<SocialNetwork>?>(null)
    val listSocialNetworks: StateFlow<Set<SocialNetwork>?> = _listSocialNetworks


    /**
     * Esta funcion recibe una Red Social y la actualiza
     */
    fun updateSocialVM(socialNetwork: SocialNetwork) {
        viewModelScope.launch {
            try {
                val response = RetrofitApi.socialNetworkService.updateSocialNetwork(socialNetwork)
                if (response.isSuccessful) {
                    _SocialNetworkUpdated.value = response.body()
                } else {
                    Log.e("error en socialViewModel", "update Social")
                }
            } catch (e: Exception) {
                // Manejar errores de red u otros errores
                e.message?.let { Log.e("error catch SocialViewModel update", it) }
            }
        }
    }




    /**
     * Esta funcion recibe un idProfile y devuelve la lista de redes Sociales
     */
    fun getListSocialNetworks(idProfile: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitApi.socialNetworkService.listSocialNetworksUser(idProfile)
                if (response.isSuccessful) {
                    _listSocialNetworks.value = response.body()
                } else {
                    Log.e("error en socialViewModel", "list Social")
                }
            } catch (e: Exception) {
                // Manejar errores de red u otros errores
                e.message?.let { Log.e("error catch socialViewModel list", it) }
            }
        }
    }



}
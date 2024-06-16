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
    private val _SocialNetworkUpdated = MutableStateFlow<Boolean?>(null)
    val socialNetworkUpdated: StateFlow<Boolean?> = _SocialNetworkUpdated

    private val _listSocialNetworks = MutableStateFlow<Set<SocialNetwork>?>(null)
    val listSocialNetworks: StateFlow<Set<SocialNetwork>?> = _listSocialNetworks

    private val _socialEdit = MutableStateFlow<SocialNetwork?>(null)
    val socialEdit: StateFlow<SocialNetwork?> = _socialEdit

    private val _socialdeleted = MutableStateFlow<Boolean?>(null)
    val socialdeleted: StateFlow<Boolean?> = _socialdeleted



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
                    _SocialNetworkUpdated.value = false
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


    /**
     * Esta funcion recibe una red social y la guarda
     */
    fun saveSocialEdit(socialNetwork: SocialNetwork) {
        viewModelScope.launch {
            try {
                val response = socialNetwork.id?.let { RetrofitApi.socialNetworkService.getSocialNetwork(it) }
                if (response != null) {
                    if (response.isSuccessful) {
                        _socialEdit.value = response.body()
                    } else {
                        Log.e("error en SocialViewModel", "edit SocialNetwork")
                    }
                }
            } catch (e: Exception) {
                // Manejar errores de red u otros errores
                e.message?.let { Log.e("error catch socialViewModel edit", it) }
            }
        }
    }

    /**
     * Esta funcion recibe un id de red social y lo elimina
     */
    fun deleteSocialNetworkVM(id:String) {
        viewModelScope.launch {
            try {
                val response = RetrofitApi.socialNetworkService.deleteSocialNetwork(id)
                if (response.isSuccessful) {
                    _socialdeleted.value = response.body()
                } else {
                    Log.e("error en socialViewModel", "delete Social")
                }
            } catch (e: Exception) {
                // Manejar errores de red u otros errores
                e.message?.let { Log.e("error catch socialViewModel delete", it) }
            }
        }
    }


    /**
     * Esta funcion pone a nulo la socialEdit
     */
    fun toNullSocialNetworkEdit() {
        viewModelScope.launch {
            try {
                _socialEdit.value = null
            } catch (e: Exception) {
                // Manejar errores de red u otros errores
                e.message?.let { Log.e("error catch socialViewModel edit null", it) }
            }
        }
    }

    /**
     * Esta funcion pone a nulo la socialUpdated
     */
    fun toNullSocialNetworkUpdated() {
        viewModelScope.launch {
            try {
                _SocialNetworkUpdated.value = null
            } catch (e: Exception) {
                // Manejar errores de red u otros errores
                e.message?.let { Log.e("error catch socialViewModel socialUpdated to null", it) }
            }
        }
    }

    /**
     * Esta funcion pone a nulo la socialDeleted
     */
    fun toNullSocialNetworkDeleted() {
        viewModelScope.launch {
            try {
                _socialdeleted.value = null
            } catch (e: Exception) {
                // Manejar errores de red u otros errores
                e.message?.let { Log.e("error catch socialViewModel socialdeleted to null", it) }
            }
        }
    }


}
package com.app.edentifica.viewModel


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.edentifica.data.model.Profile
import com.app.edentifica.data.model.User
import com.app.edentifica.data.retrofit.RetrofitApi
import com.app.edentifica.data.retrofit.RetrofitApi.userService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class UsersViewModel : ViewModel() {
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val _userEmailSearch = MutableStateFlow<User?>(null)
    val userEmailSearch: StateFlow<User?> = _userEmailSearch

    private val _userPhoneSearch = MutableStateFlow<User?>(null)
    val userPhoneSearch: StateFlow<User?> = _userPhoneSearch

    private val _userSocialSearch = MutableStateFlow<User?>(null)
    val userSocialSearch: StateFlow<User?> = _userSocialSearch

    private val _userInserted = MutableStateFlow<Boolean?>(false)
    val userInserted: StateFlow<Boolean?> = _userInserted

    private val _validationOne = MutableStateFlow<Boolean>(false)
    val validationOne: StateFlow<Boolean> = _validationOne

    private val _answerValidation = MutableStateFlow<Boolean?>(null)
    val answerValidation: StateFlow<Boolean?> = _answerValidation

    private val _userUpdate = MutableStateFlow<Boolean?>(false)
    val userUpdate: StateFlow<Boolean?> = _userUpdate


    //CRUD USER
    /**
     * Esta funcion recibe un User y lo inserta en la base de datos
     */

    suspend fun insertUserVm(user:User): Boolean {
        return try {
            val response = userService.insertUser(user)
            if (response.isSuccessful) {
                _userInserted.value = true
                true
            } else {
                Log.e("error en userViewModel", "insertUser")
                false
            }
        } catch (e: Exception) {
            // Manejar errores de red u otros errores
            e.message?.let { Log.e("error catch userViewModel insert User", it) }
            false
        }
    }

    /**
     * Esta funcion recibe un email y nos devuelve al usuario encontrado
     */
    fun getUserByEmail(email: String) {
        viewModelScope.launch {
            try {
                val response = userService.getByEmail(email)
                if (response.isSuccessful) {
                    _user.value = response.body()
                } else {
                    Log.e("error en userViewModel", "getUserByEmail")
                }
            } catch (e: Exception) {
                // Manejar errores de red u otros errores
                e.message?.let { Log.e("error catch userViewModel getUserByEmail", it) }
            }
        }
    }

    //BUSQUEDAS
    /**
     * Esta funcion recibe un email y nos devuelve al usuario encontrado, esta la utilizamos para las busquedas de correo
     */
    fun getUserByEmailSearch(email: String) {
        viewModelScope.launch {
            try {
                val response = userService.getByEmail(email)
                if (response.isSuccessful) {
                    //Aqui solo devuelvo los resultados de busqueda de los usuarios que esten validados.
                    //Pendiente agregar la validacion 2
                    if(response.body()?.validations?.get(0)?.isValidated == true){
                        _userEmailSearch.value = response.body()
                    }else{
                        _userEmailSearch.value = null
                    }
                } else {
                    Log.e("error en userViewModel", "getUserByEmailSearch")
                }
            } catch (e: Exception) {
                // Manejar errores de red u otros errores
                e.message?.let { Log.e("error catch userViewModel getUserByEmailSearch", it) }
            }
        }
    }


    /**
     * Esta funcion pone en nulo la respuesta de buscar por email
     */
    fun putEmailResultNull() {
        viewModelScope.launch {
            try {
                _userEmailSearch.value=null
            } catch (e: Exception) {
                // Manejar errores de red u otros errores
                e.message?.let { Log.e("error catch userViewModel EmailResultNull", it) }
            }
        }
    }



    /**
     * Esta funcion recibe un phone y nos devuelve al usuario encontrado, esta la utilizamos para las busquedas de telefonos
     */
    fun getUserByPhoneSearch(phone: String) {
        viewModelScope.launch {
            try {
                val response = userService.getByPhone(phone)
                if (response.isSuccessful) {
                    //Aqui solo devuelvo los resultados de busqueda de los usuarios que esten validados.
                    //Pendiente agregar la validacion 2
                    if(response.body()?.validations?.get(0)?.isValidated == true){
                        _userPhoneSearch.value = response.body()
                    }else{
                        _userPhoneSearch.value = null
                    }
                } else {
                    Log.e("error en userViewModel", "getUserByPhoneSearch")
                }
            } catch (e: Exception) {
                // Manejar errores de red u otros errores
                e.message?.let { Log.e("error catch userViewModel getUserByPhoneSearch", it) }
            }
        }
    }

    /**
     * Esta funcion pone en nulo la respuesta de buscar por telefono
     */
    fun putPhoneResultNull() {
        viewModelScope.launch {
            try {
                _userPhoneSearch.value=null
            } catch (e: Exception) {
                // Manejar errores de red u otros errores
                e.message?.let { Log.e("error catch userViewModel PhoneResultNull", it) }
            }
        }
    }



    /**
     * Esta funcion recibe un phone y nos devuelve al usuario encontrado, esta la utilizamos para las busquedas de telefonos
     */
    fun getUserBySocialSearch(type: String, socialname: String) {
        viewModelScope.launch {
            try {
                val response = userService.getBySocialNetwork(type,socialname)
                if (response.isSuccessful) {
                    //Aqui solo devuelvo los resultados de busqueda de los usuarios que esten validados.
                    //Pendiente agregar la validacion 2
                    if(response.body()?.validations?.get(0)?.isValidated == true){
                        _userSocialSearch.value = response.body()
                    }else{
                        _userPhoneSearch.value = null
                    }
                } else {
                    Log.e("error en userViewModel", "getUserBySocialSearch")
                }
            } catch (e: Exception) {
                // Manejar errores de red u otros errores
                e.message?.let { Log.e("error catch userViewModel getUserBySocialSearch", it) }
            }
        }
    }

    /**
     * Esta funcion pone en nulo la respuesta de buscar por telefono
     */
    fun putSocialNetworkResultNull() {
        viewModelScope.launch {
            try {
                _userSocialSearch.value=null
            } catch (e: Exception) {
                // Manejar errores de red u otros errores
                e.message?.let { Log.e("error catch userViewModel SocialNetworkResultNull", it) }
            }
        }
    }



    //FUNCIONES VALIDACIONES

    /**
     * Esta funcion recibe un usuario para realizar la llamada de la validacion one
     */
    fun toDoCallByUser(user: User) {
        viewModelScope.launch {
            try {
                val response = userService.toDoCall(user)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _validationOne.value = responseBody
                        Log.e("validacion", "bien")
                    } else {
                        Log.e("error en userViewModel", "toDoCallByUser if")
                    }
                } else {
                    Log.e("error en userViewModel", "toDoCallByUser else")
                }
            } catch (e: Exception) {
                // Manejar errores de red u otros errores
                e.message?.let { Log.e("error catch userViewModel toDoCallByUser try", it) }
            }
        }
    }


    /**
     * Esta funcion recibe la respuesta matematica y actualiza su variable si es correcta
     */
    fun answerMathByUser(answer: Int,user: User) {
        viewModelScope.launch {
            try {
                val response = userService.answerMathChallenge(answer,user)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody==true) {
                        _answerValidation.value = responseBody
                        //si la respuesta es true, actualizao al usuario desde la base de datos
                        getUserByEmail(user.email.email)
                        Log.e("respuesta validacion", "bien")
                    } else {
                        _answerValidation.value = responseBody
                        //si la respuesta es true, actualizao al usuario desde la base de datos
                        getUserByEmail(user.email.email)
                        Log.e("error en userViewModel", "la respuesta no es correcta")
                    }
                } else {
                    Log.e("error en userViewModel", "answerMathByUser")
                }
            } catch (e: Exception) {
                // Manejar errores de red u otros errores
                e.message?.let { Log.e("error catch userViewModel answerMathByUser try", it) }
            }
        }
    }


    /**
     * Esta funcion niega la validacion 1
     */
    fun validationOneNegative() {
        viewModelScope.launch {
            try {
                _validationOne.value=false
            } catch (e: Exception) {
                // Manejar errores de red u otros errores
                e.message?.let { Log.e("error catch userViewModel validationOneNegative", it) }
            }
        }
    }

    /**
     * Esta funcion pone en nulo la respuesta del reto matematico del usuario
     */
    fun validationOneCheckNegative() {
        viewModelScope.launch {
            try {
                _answerValidation.value=null
            } catch (e: Exception) {
                // Manejar errores de red u otros errores
                e.message?.let { Log.e("error catch userViewModel validationOneCheckNegative", it) }
            }
        }
    }


    /**
     * Esta funcion recibe un User y lo actualiza
     */
    fun updateUserVM(user:User) {
        viewModelScope.launch {
            try {
                val response = userService.updateUser(user)
                if (response.isSuccessful) {
                    _userUpdate.value = response.body()
                } else {
                    Log.e("error en userViewModel", "update user")
                }
            } catch (e: Exception) {
                // Manejar errores de red u otros errores
                e.message?.let { Log.e("error catch userViewModel update", it) }
            }
        }
    }

}
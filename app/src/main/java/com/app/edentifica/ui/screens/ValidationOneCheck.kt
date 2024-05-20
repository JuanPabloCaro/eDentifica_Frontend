package com.app.edentifica.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.app.edentifica.R
import com.app.edentifica.data.model.User
import com.app.edentifica.navigation.AppScreen
import com.app.edentifica.utils.AuthManager
import com.app.edentifica.viewModel.UsersViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ValidationOneCheckScreen(
    navController: NavController,
    auth: AuthManager,
    vmUsers: UsersViewModel,
) {
    //VARIABLES Y CONSTANTES
    val context = LocalContext.current

    val user = auth.getCurrentUser()
    // Llama a getUserByEmail cuando se inicia ValidationOneScreen
    LaunchedEffect(Unit) {
        auth.getCurrentUser()?.email?.let { vmUsers.getUserByEmail(it) }
    }
    // Observa el flujo de usuario en el ViewModel
    val userState by vmUsers.user.collectAsState()

    Log.e("userBBDD", userState.toString())



    //Estructura de la pantalla
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if(user?.photoUrl != null) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(user?.photoUrl)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "Imagen",
                                placeholder = painterResource(id = R.drawable.profile),
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(40.dp))
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.profile),
                                contentDescription = "image profile default",
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .size(40.dp)
                                    .clip(CircleShape)
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if(!user?.displayName.isNullOrEmpty()) "Hola ${user?.displayName}" else "Bienvenid@",//welcomeMessage,
                                fontSize = 20.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = if(!user?.email.isNullOrEmpty()) "${user?.email}" else "Anónimo",
                                fontSize = 12.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis)
                        }
                    }
                },
                backgroundColor= Color.Gray
            )
        },
        bottomBar = {
            BottomAppBar (backgroundColor = Color.DarkGray){
                Text(
                    text = "Version 1.0 @Copyrigth 2024 Todos los derechos reservados",
                    fontSize = 12.sp,
                    fontStyle = FontStyle.Italic,
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center)
                )
            }
        }
    ) {
        //funcion composable que pinta el contenido de home
        BodyContentValidationOneCheck(navController, vmUsers, userState, context)
    }


}

@Composable
fun BodyContentValidationOneCheck(
    navController: NavController,
    vmUsers: UsersViewModel,
    userState: User?,
    context: Context
) {
    // Observa el estado de validationOneCheck
    val validationOneCheckState = vmUsers.answerValidation.collectAsState()

    // Estado para almacenar la respuesta del usuario
    var userResponse by remember { mutableStateOf("") }

    // Estado para verificar si se ha presionado el botón
    var buttonPressed by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Por favor introduce la respuesta del reto matemático",
            style = MaterialTheme.typography.h5
        )

        // Campo de entrada para la respuesta del usuario
        OutlinedTextField(
            value = userResponse,
            onValueChange = { userResponse = it },
            label = { Text("Respuesta") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
            Button(
                onClick = {
                    // Llamar a la función del ViewModel
                    if (userState != null) {
                        vmUsers.answerMathByUser(userResponse.toInt(), userState)
                        buttonPressed = true // Marcar el botón como presionado
                    }
                },
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Validar respuesta")
            }
        }

        // Observar cambios en validationOneCheckState
        LaunchedEffect(validationOneCheckState.value) {
            // Si validationOneCheckState es false y el botón ha sido presionado
            if (validationOneCheckState.value == false && buttonPressed) {
                Toast.makeText(
                    context,
                    "Respuesta inválida",
                    Toast.LENGTH_LONG
                ).show()

                // Reiniciar variables y navegar a la pantalla anterior
                buttonPressed = false
                vmUsers.validationOneNegative()
                vmUsers.validationOneCheckNegative()
                navController.popBackStack()
            }
        }

        // Si validationOneCheckState es true, navegar a la pantalla HomeScreen
        if (validationOneCheckState.value == true) {
            LaunchedEffect(Unit) {
                Toast.makeText(
                    context,
                    "La validación 1 ha sido exitosa",
                    Toast.LENGTH_LONG
                ).show()
                navController.navigate(AppScreen.HomeScreen.route)
            }
        }
    }
}
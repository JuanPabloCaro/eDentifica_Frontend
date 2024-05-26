package com.app.edentifica.ui.screens.Validations

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import com.app.edentifica.ui.theme.AppColors
import com.app.edentifica.ui.theme.TextSizes
import com.app.edentifica.utils.AuthManager
import com.app.edentifica.viewModel.UsersViewModel

@OptIn(ExperimentalMaterial3Api::class)
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



    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AppColors.mainEdentifica),
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
                                fontSize = TextSizes.H3,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = AppColors.whitePerlaEdentifica
                            )
                            Text(
                                text = if(!user?.email.isNullOrEmpty()) "${user?.email}" else "Anónimo",
                                fontSize = TextSizes.Footer,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = AppColors.whitePerlaEdentifica
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = AppColors.mainEdentifica,
                modifier = Modifier.height(44.dp)
            ) {
                Text(
                    text = stringResource(R.string.copyrigth),
                    fontSize = TextSizes.Footer,
                    fontStyle = FontStyle.Italic,
                    color = AppColors.whitePerlaEdentifica,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center)
                )
            }
        }
    ) {
        //funcion composable que pinta el contenido
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColors.whitePerlaEdentifica) //Color de fondo de la aplicacion
                .padding(24.dp)
        ){
            BodyContentValidationOneCheck(navController, vmUsers, userState, context)
        }
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
        //Title
        Text(
            text = "Validacion Llamada",
            fontSize = TextSizes.H1,
            color = AppColors.mainEdentifica
        )

        //Image
        Image(
            painter = painterResource(id = R.drawable.check_call),
            contentDescription = "check Call",
            modifier = Modifier
                .fillMaxWidth().scale(0.7f).padding(0.dp), // ajusta la altura según sea necesario
            contentScale = ContentScale.Crop // Escala de la imagen
        )

        Text(
            text = "Por favor introduce la respuesta del reto matemático:",
            fontSize = TextSizes.H3
        )

        // Campo de entrada para la respuesta del usuario
        Spacer(modifier = Modifier.height(20.dp))
        TextField(
            label = {
                Text(
                    text = "Respuesta",
                    fontSize = TextSizes.Paragraph
                )
            },
            value = userResponse,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            onValueChange = { userResponse = it })


        //Button
        Spacer(modifier = Modifier.height(20.dp))
        Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
            Button(
                onClick = {
                    // Llamar a la función del ViewModel
                    if (userState != null) {
                        vmUsers.answerMathByUser(userResponse.toInt(), userState)
                        buttonPressed = true // Marcar el botón como presionado
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = AppColors.FocusEdentifica),
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = "Validar respuesta",
                    fontSize = TextSizes.H3,
                    color = AppColors.whitePerlaEdentifica)
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
                navController.navigate(AppScreen.ValidationOneScreen.route){
                    popUpTo(AppScreen.ValidationOneCheckScreen.route){
                        inclusive= true
                    }
                }
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
                navController.navigate(AppScreen.HomeScreen.route){
                    popUpTo(AppScreen.ValidationOneCheckScreen.route){
                        inclusive= true
                    }
                }
            }
        }
    }
}
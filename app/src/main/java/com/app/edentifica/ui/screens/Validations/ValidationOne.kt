package com.app.edentifica.ui.screens.Validations

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.app.edentifica.R
import com.app.edentifica.data.model.User
import com.app.edentifica.navigation.AppScreen
import com.app.edentifica.ui.screens.BodyContentHome
import com.app.edentifica.ui.theme.AppColors
import com.app.edentifica.ui.theme.TextSizes
import com.app.edentifica.utils.AuthManager
import com.app.edentifica.viewModel.UsersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ValidationOneScreen(
    navController: NavController,
    auth: AuthManager,
    vmUsers: UsersViewModel,
    onSignOutGoogle: () -> Unit,
) {
    //VARIABLES Y CONSTANTES

    //para mostrar el dialogo de cerrar Sesion
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val currentUser = auth.getCurrentUser()

    // Llama a getUserByEmail cuando se inicia ValidationOneScreen
    LaunchedEffect(Unit) {
        auth.getCurrentUser()?.email?.let { vmUsers.getUserByEmail(it) }
    }

    // Observa el flujo de usuario en el ViewModel
    val userState by vmUsers.user.collectAsState()

    // Verifica si el teléfono del usuario es nulo y navega a la pantalla de registro de teléfono si es necesario
    LaunchedEffect(userState) {
        if (userState?.phone?.phoneNumber == null || userState?.phone?.phoneNumber.equals("") || userState?.phone?.phoneNumber.equals("null")) {
            navController.navigate(AppScreen.RegisterPhoneScreen.route)
        }
    }



    val onLogoutConfirmed:()->Unit = {
        auth.signOut()
        onSignOutGoogle()

        navController.navigate(AppScreen.LoginScreen.route){
            popUpTo(AppScreen.HomeScreen.route){
                inclusive= true
            }
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AppColors.mainEdentifica),
                title = {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Spacer(modifier = Modifier.width(8.dp))
                            (if(!currentUser?.displayName.isNullOrEmpty() || userState!=null) userState?.name?.let {
                                stringResource(
                                    R.string.hola, it
                                )
                            } else stringResource(R.string.bienvenid))?.let {
                                Text(
                                    text = it,//welcomeMessage,
                                    fontSize = TextSizes.H3,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    color = AppColors.whitePerlaEdentifica
                                )
                            }
                            (if(!currentUser?.email.isNullOrEmpty()|| userState!=null) userState?.email?.email else stringResource(
                                R.string.usuario_anonimo
                            ))?.let {
                                Text(
                                    text = it,
                                    fontSize = TextSizes.Footer,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    color = AppColors.whitePerlaEdentifica
                                )
                            }
                        }
                    }
                },
                actions = {
                    //boton de accion para salir cerrar sesion
                    IconButton(
                        onClick = {
                            showDialog = true
                        }
                    ) {
                        Icon(
                            Icons.Outlined.ExitToApp,
                            contentDescription = "Cerrar sesión",
                            tint = AppColors.whitePerlaEdentifica
                        )
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
        //funcion para mostrar un pop up preguntando si quiere cerrar la sesion
        contentPadding ->
        Box(modifier = Modifier.padding(contentPadding)) {
            if (showDialog) {
                LogoutDialogValidation(
                    onConfirmLogout = {
                        onLogoutConfirmed()
                        showDialog = false
                    },
                    onDismiss = { showDialog = false })
            }

        }
        //funcion composable que pinta el contenido
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColors.whitePerlaEdentifica) //Color de fondo de la aplicacion
                .padding(24.dp)
        ){
            BodyContentValidationOne(navController, vmUsers, userState, context)
        }

    }

}

@Composable
fun BodyContentValidationOne(
    navController: NavController,
    vmUsers: UsersViewModel,
    userState: User?,
    context: Context
) {

    // Observa el estado de validationOne
    val validationOneState = vmUsers.validationOne.collectAsState()
    val isLoading by vmUsers.isLoadingCall.collectAsState()

    // Usa un when para manejar diferentes casos
    when {
        validationOneState.value == true -> {
            // Si validationOne es true, redirigir a otra pantalla
            LaunchedEffect(Unit) {
                navController.navigate(AppScreen.ValidationOneCheckScreen.route){
                    popUpTo(AppScreen.ValidationOneScreen.route){
                        inclusive= true
                    }
                }
                Toast.makeText(context,
                    context.getString(R.string.te_llamaremos_en_breve), Toast.LENGTH_LONG).show()
            }
        }
        validationOneState.value == false -> {
            if (isLoading==true) {
                // Mostrar CircularProgressIndicator mientras carga
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AppColors.mainEdentifica.copy(alpha = 0.2f))
                        .clickable { /* No hace nada al hacer clic para evitar que se propague */ },
                    contentAlignment = Alignment.Center

                ) {
                    Box(
                        modifier = Modifier
                            .wrapContentSize(Alignment.Center)
                            .background(Color.White, shape = RoundedCornerShape(8.dp))
                    ) {
                        CircularProgressIndicator(
                            color = AppColors.FocusEdentifica,
                            strokeWidth = 4.dp,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }
            }
            // Si validationOne es false, mostrar el botón para comenzar la validación
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier
                        .wrapContentSize(Alignment.Center)
                        .padding(horizontal = 32.dp),
                    text = stringResource(R.string.validacion_1),
                    fontSize = TextSizes.H1,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.mainEdentifica,
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(16.dp))
                //Image
                Image(
                    painter = painterResource(id = R.drawable.call),
                    contentDescription = "Mobile",
                    modifier = Modifier
                        .fillMaxWidth()
                        .scale(0.7f)
                        .padding(0.dp), // ajusta la altura según sea necesario
                    contentScale = ContentScale.Crop // Escala de la imagen
                )

                Text(
                    modifier = Modifier
                        .wrapContentSize(Alignment.Center)
                        .padding(horizontal = 32.dp),
                    text = stringResource(R.string.para_este_proceso_te_vamos_a_llamar_y_escuchar_s_una_operaci_n_matem_tica_despu_s_de_esto_debes_colgar_la_llamada_y_contestar_el_resultado_en_nuestra_aplicaci_n_si_est_s_listo_empecemos),
                    fontSize = TextSizes.H3,
                    color = AppColors.mainEdentifica,
                    textAlign = TextAlign.Center,
                )

                //Button enviar llamada
                Spacer(modifier = Modifier.height(34.dp))
                Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                    Button(
                        onClick = {
                            // Llamar a la función del ViewModel
                            userState?.let { user ->
                                vmUsers.toDoCallByUser(user)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AppColors.FocusEdentifica),
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text(text = stringResource(R.string.empezar_validacion),
                            fontSize = TextSizes.H3,
                            color = AppColors.whitePerlaEdentifica)
                    }
                }
            }
        }
        else -> {
            // Manejar otros casos si es necesario
        }
    }

}


/**
 * Funcion composable que se encarga de mostrar un alert para preguntar al
 * usuario si quiere continuar o cerrar sesion
 */
@Composable
fun LogoutDialogValidation(
    onConfirmLogout: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        containerColor = AppColors.whitePerlaEdentifica,
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.cerrar_sesi_n), color = AppColors.mainEdentifica) },
        text = { Text(stringResource(R.string.est_s_seguro_que_deseas_cerrar_sesi_n),color = AppColors.mainEdentifica) },
        confirmButton = {
            Button(
                onClick = onConfirmLogout,
                colors = ButtonDefaults.buttonColors(containerColor = AppColors.FocusEdentifica)
            ) {
                Text(stringResource(R.string.aceptar), color = AppColors.whitePerlaEdentifica)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                border = BorderStroke(1.dp, AppColors.FocusEdentifica),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = AppColors.FocusEdentifica)
            ) {
                Text(stringResource(R.string.cancelar))
            }
        }
    )

}


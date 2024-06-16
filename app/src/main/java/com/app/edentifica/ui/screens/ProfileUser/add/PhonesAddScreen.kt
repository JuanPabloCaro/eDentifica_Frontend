package com.app.edentifica.ui.screens.ProfileUser.add

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.app.edentifica.R
import com.app.edentifica.data.model.Email
import com.app.edentifica.data.model.Phone
import com.app.edentifica.data.model.User
import com.app.edentifica.navigation.AppScreen
import com.app.edentifica.ui.screens.LoginAndRegister.isValidPhoneNumber
import com.app.edentifica.ui.theme.AppColors
import com.app.edentifica.ui.theme.TextSizes
import com.app.edentifica.utils.AuthManager
import com.app.edentifica.viewModel.EmailViewModel
import com.app.edentifica.viewModel.PhonesViewModel
import com.app.edentifica.viewModel.ProfileViewModel
import com.app.edentifica.viewModel.UsersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PhonesAddScreen(
    navController: NavController,
    auth: AuthManager,
    onSignOutGoogle: () -> Unit,
    vmUsers: UsersViewModel,
    vmPhones: PhonesViewModel,
    vmProfiles: ProfileViewModel
) {
    //VARIABLES Y CONSTANTES
    //para mostrar el dialogo de cerrar Sesion
    var showDialog by remember { mutableStateOf(false) }

    // Llama a getUserByEmail cuando se inicia HomeScreen
    LaunchedEffect(Unit) {
        auth.getCurrentUser()?.email?.let { vmUsers.getUserByEmail(it) }
    }
    // Observa el flujo de usuario en el ViewModel
    val userState by vmUsers.user.collectAsState()


    val onLogoutConfirmedPhonesAddScreen:()->Unit = {
        auth.signOut()
        onSignOutGoogle()

        navController.navigate(AppScreen.LoginScreen.route){
            popUpTo(AppScreen.HomeScreen.route){
                inclusive= true
            }
        }
    }

    //Estructura de la pantalla
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AppColors.mainEdentifica),
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate(AppScreen.PhonesScreen.route)
                    }) {
                        Icon(
                            imageVector= Icons.Default.ArrowBack,
                            contentDescription="ArrowBack",
                            tint = AppColors.whitePerlaEdentifica
                        )
                    }
                },
                title = {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = stringResource(R.string.agregar_telefono),
                            fontSize = TextSizes.H2,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = AppColors.whitePerlaEdentifica
                        )
                    }
                },
                actions = {
                    //Botton Home
                    IconButton(
                        onClick = {
                            navController.navigate(AppScreen.HomeScreen.route)
                        }
                    ) {
                        Icon(
                            Icons.Outlined.Home,
                            contentDescription = "Home",
                            tint = AppColors.whitePerlaEdentifica
                        )
                    }
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
                LogoutDialogPhonesAdd(
                    onConfirmLogout = {
                        onLogoutConfirmedPhonesAddScreen()
                        showDialog = false
                    },
                    onDismiss = { showDialog = false })
            }

        }

        //funcion composable que pinta el contenido de home
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColors.whitePerlaEdentifica) //Color de fondo de la aplicacion
                .padding(24.dp)
        ){
            BodyContentPhonesAddScreen(navController,vmPhones,vmProfiles,userState)
        }

    }


}




@Composable
fun BodyContentPhonesAddScreen(
    navController: NavController,
    vmPhones: PhonesViewModel,
    vmProfiles: ProfileViewModel,
    userState: User?,
) {
    //VARIABLES
    var phone by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var context= LocalContext.current

    var showDuplicatePhoneError by remember { mutableStateOf(false) }

    // Observa el flujo de phone en el ViewModel
    val phoneInsertState by vmProfiles.phoneInserted.collectAsState()

    // Observa el flujo de actualización del teléfono y muestra un Toast cuando se completa la actualización
    LaunchedEffect(phoneInsertState) {
        if (phoneInsertState == true) {
            Toast.makeText(
                context,
                context.getString(R.string.el_tel_fono_se_inserto_correctamente),
                Toast.LENGTH_SHORT
            ).show()
            vmProfiles.toNullPhoneInserted()
            navController.navigate(AppScreen.PhonesScreen.route)
        } else if (phoneInsertState == false) {
            showDuplicatePhoneError = true
        }
    }


    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Spacer(modifier = Modifier.height(68.dp))
        //Image
        Image(
            painter = painterResource(id = R.drawable.addphone),
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
            text = stringResource(R.string.con_ctate_con_el_mundo_inserta_un_tel_fono_incluyendo_el_prefijo_del_pa_s),
            color = AppColors.mainEdentifica,
            fontSize = TextSizes.H2
        )

        // Campo de entrada para el correo electrónico
        Spacer(modifier = Modifier.height(34.dp))
        TextField(
            label = { Text(text = stringResource(R.string.telefono), fontSize = TextSizes.Paragraph) },
            value = phone,
            onValueChange = { phone = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            placeholder = { Text(text = "34xxxxxxxxx") }
        )

        if (showError) {
            Text(
                text = stringResource(R.string.el_n_mero_de_tel_fono_no_es_v_lido),
                color = AppColors.FocusEdentifica,
                fontSize = TextSizes.Paragraph
            )
        }

        if (showDuplicatePhoneError) {
            Text(
                text = stringResource(R.string.el_n_mero_de_tel_fono_ya_existe),
                color = AppColors.FocusEdentifica,
                fontSize = TextSizes.Paragraph
            )
        }



        Spacer(modifier = Modifier.height(34.dp))

        // Botón para insertar el telefono
        Box(modifier = Modifier.padding(60.dp, 0.dp, 60.dp, 0.dp)) {
            Button(
                onClick = {

                    if(isValidPhoneNumberAdd(phone)){

                        // Insertar el telefono llamando a la función del ViewModel
                        userState?.let { user ->
                            val phoneToInsert = Phone(id = null, phoneNumber = phone, isVerified = false, idProfileUser = null)
                            user.profile?.id?.let { profileId ->
                                vmProfiles.insertPhoneVm(phoneToInsert, profileId)
                            }
                        }

                        showError=false

                    }else{
                        showError=true
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = AppColors.FocusEdentifica),
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = stringResource(R.string.insertar_telefono),
                    fontSize = TextSizes.H3,
                    color = AppColors.whitePerlaEdentifica
                )
            }
        }
    }
}



/**
 * Funcion composable que se encarga de mostrar un alert para preguntar al
 * usuario si quiere continuar o cerrar sesion
 */
@Composable
fun LogoutDialogPhonesAdd(
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

fun isValidPhoneNumberAdd(phone: String): Boolean {
    val phoneRegex = "^[0-9]{10,15}$".toRegex()
    return phone.matches(phoneRegex)
}
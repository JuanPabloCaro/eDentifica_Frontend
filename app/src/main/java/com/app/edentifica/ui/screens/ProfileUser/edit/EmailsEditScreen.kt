package com.app.edentifica.ui.screens.ProfileUser.edit

import android.annotation.SuppressLint
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.app.edentifica.R
import com.app.edentifica.data.model.Email
import com.app.edentifica.navigation.AppScreen
import com.app.edentifica.ui.theme.AppColors
import com.app.edentifica.ui.theme.TextSizes
import com.app.edentifica.utils.AuthManager
import com.app.edentifica.viewModel.EmailViewModel
import com.app.edentifica.viewModel.UsersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun EmailsEditScreen(
    navController: NavController,
    auth: AuthManager,
    onSignOutGoogle: () -> Unit,
    vmUsers: UsersViewModel,
    vmEmails: EmailViewModel
) {
    //VARIABLES Y CONSTANTES
    //para mostrar el dialogo de cerrar Sesion
    var showDialog by remember { mutableStateOf(false) }

    // Llama a getUserByEmail pa
    LaunchedEffect(Unit) {
        auth.getCurrentUser()?.email?.let { vmUsers.getUserByEmail(it) }
    }
    // Observa el flujo de email A editar en el ViewModel
    val emailCurrent by vmEmails.emailEdit.collectAsState()


    val onLogoutConfirmedEmailsEditScreen:()->Unit = {
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
                        navController.navigate(AppScreen.EmailsScreen.route)
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
                            text = stringResource(R.string.editar_correo),
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
                LogoutDialogEmailsEdit(
                    onConfirmLogout = {
                        onLogoutConfirmedEmailsEditScreen()
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
            //funcion composable que pinta el contenido de home
            emailCurrent?.let { BodyContentEmailsEditScreen(navController = navController, vmEmails = vmEmails, email = it) }
        }

    }


}





@Composable
fun BodyContentEmailsEditScreen(
    navController: NavController,
    vmEmails: EmailViewModel,
    email: Email
) {
    var currentEmail by remember { mutableStateOf(email.email) }

    Column(
        modifier = Modifier
            .padding(top = 30.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(68.dp))
        //Image
        Image(
            painter = painterResource(id = R.drawable.editemail),
            contentDescription = "edit email",
            modifier = Modifier
                .fillMaxWidth()
                .scale(0.7f)
                .padding(0.dp), // ajusta la altura según sea necesario
            contentScale = ContentScale.Crop // Escala de la imagen
        )

        TextField(
            label = { Text(text = stringResource(R.string.correo) , fontSize = TextSizes.Paragraph) },
            value = currentEmail,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            onValueChange = { currentEmail = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            placeholder = {Text(stringResource(R.string.ejemplo_gmail_com))}
        )

        Box(modifier = Modifier.padding(60.dp, 0.dp, 60.dp, 0.dp)) {
            Button(
                onClick = {
                    //Actualizar el email
                    val updatedEmail = email.copy(email = currentEmail)
                    vmEmails.updateEmailVM(updatedEmail)
                    vmEmails.toNullEmailEdit()
                    navController.popBackStack()
                },
                colors = ButtonDefaults.buttonColors(containerColor = AppColors.FocusEdentifica),
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = stringResource(R.string.editar),
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
fun LogoutDialogEmailsEdit(
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
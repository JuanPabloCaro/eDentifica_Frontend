package com.app.edentifica.ui.screens.Results

import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.outlined.Home
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.app.edentifica.R
import com.app.edentifica.data.model.User
import com.app.edentifica.navigation.AppScreen
import com.app.edentifica.ui.screens.ClickableProfileImage
import com.app.edentifica.ui.theme.AppColors
import com.app.edentifica.ui.theme.TextSizes
import com.app.edentifica.utils.AuthManager
import com.app.edentifica.viewModel.UsersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ResultSearchSocialScreen(
    navController: NavController,
    auth: AuthManager,
    onSignOutGoogle: () -> Unit,
    vmUsers: UsersViewModel,
) {
    //VARIABLES Y CONSTANTES
    //para mostrar el dialogo de cerrar Sesion
    var showDialog by remember { mutableStateOf(false) }
    //recojo al user Actual
    val currentUser = auth.getCurrentUser()
    // Llama a getUserByEmail cuando se inicia HomeScreen
    LaunchedEffect(Unit) {
        auth.getCurrentUser()?.email?.let { vmUsers.getUserByEmail(it) }
    }
    // Observa el flujo de usuario en el ViewModel
    val userState by vmUsers.user.collectAsState()

    Log.e("userValidation", userState?.validations?.get(0)?.isValidated.toString())
    Log.e("userValidation", userState?.toString().toString())


    val onLogoutConfirmedResultSocial:()->Unit = {
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
                title = {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        if(currentUser?.photoUrl != null) {
                            if(auth.getCurrentUser()?.email!=null && userState?.validations?.get(0)?.isValidated ==true ){
                                userState?.profile?.urlImageProfile?.let {
                                    ClickableProfileImage(
                                        navController = navController,
                                        imageUrl = it
                                    ) {
                                        navController.navigate(AppScreen.ProfileUserScreen.route)
                                    }
                                }
                            }else{
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(userState?.profile?.urlImageProfile)
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = "Imagen",
                                    placeholder = painterResource(id = R.drawable.profile),
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .size(40.dp)
                                )
                            }

                        } else {
                            if(auth.getCurrentUser()?.email!=null && userState?.validations?.get(0)?.isValidated ==true ){
                                userState?.profile?.urlImageProfile?.let {
                                    ClickableProfileImage(
                                        navController = navController,
                                        imageUrl = it
                                    ) {
                                        navController.navigate(AppScreen.ProfileUserScreen.route)
                                    }
                                }

                            } else{
                                Image(
                                    painter = painterResource(id = R.drawable.profile),
                                    contentDescription = "image profile default",
                                    modifier = Modifier
                                        .padding(end = 8.dp)
                                        .size(40.dp)
                                        .clip(CircleShape)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if(!currentUser?.displayName.isNullOrEmpty() || userState!=null) "Hola ${userState?.name}" else "Bienvenid@",//welcomeMessage,
                                fontSize = TextSizes.H3,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = AppColors.whitePerlaEdentifica
                            )
                            (if(!currentUser?.email.isNullOrEmpty()|| userState!=null) userState?.email?.email else "Anonimo")?.let {
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
                    //Botton Home
                    IconButton(
                        onClick = {
                            vmUsers.putSocialNetworkResultNull()
                            vmUsers.toNullfindString()
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
                LogoutDialogResultSocial(
                    onConfirmLogout = {
                        onLogoutConfirmedResultSocial()
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
        ) {
            //funcion composable que pinta el contenido de home
            BodyContentResultSocial(navController, vmUsers, userState)
        }

    }

}







@Composable
fun BodyContentResultSocial(navController: NavController, vmUsers: UsersViewModel, userState: User?) {

    // Observamos el estado del resultado de la busqueda
    val searchResultSocial by vmUsers.userSocialSearch.collectAsState()
    // Es el parametro que busco el usuario
    val findString by vmUsers.findString.collectAsState()
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (searchResultSocial != null) {
            //Image
            Image(
                painter = painterResource(id = R.drawable.checkresult),
                contentDescription = "Check Result",
                modifier = Modifier
                    .fillMaxWidth().scale(0.9f).padding(0.dp), // ajusta la altura según sea necesario
                contentScale = ContentScale.Crop // Escala de la imagen
            )
            Text(
                text = "La red social de ${findString} le pertenece al usuario ${searchResultSocial!!.name} con eDentificador ${searchResultSocial!!.edentificador} garantizando la seguridad del perfil",
                modifier = Modifier.padding(16.dp)
            )

        } else {
            //Image
            Image(
                painter = painterResource(id = R.drawable.warning),
                contentDescription = "Warning",
                modifier = Modifier
                    .fillMaxWidth().scale(0.9f).padding(0.dp), // ajusta la altura según sea necesario
                contentScale = ContentScale.Crop // Escala de la imagen
            )
            Text(
                text = "Lo sentimos, usuario no encontrado, puede tratarse de una posible suplantacion",
                modifier = Modifier.padding(16.dp)
            )
        }

        // Botón para volver a hacer otra busqueda
        Spacer(modifier = Modifier.height(34.dp))
        Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
            Button(
                onClick = {
                    vmUsers.putSocialNetworkResultNull()
                    vmUsers.toNullfindString()
                    navController.navigate(AppScreen.FindBySocialNetworkScreen.route)
                },
                colors = ButtonDefaults.buttonColors(containerColor = AppColors.FocusEdentifica),
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Realizar otra Busqueda")
            }
        }
    }

}







/**
 * Funcion composable que se encarga de mostrar un alert para preguntar al
 * usuario si quiere continuar o cerrar sesion
 */
@Composable
fun LogoutDialogResultSocial(
    onConfirmLogout: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        containerColor = AppColors.whitePerlaEdentifica,
        onDismissRequest = onDismiss,
        title = { Text("Cerrar sesión", color = AppColors.mainEdentifica) },
        text = { Text("¿Estás seguro que deseas cerrar sesión?",color = AppColors.mainEdentifica) },
        confirmButton = {
            Button(
                onClick = onConfirmLogout,
                colors = ButtonDefaults.buttonColors(containerColor = AppColors.FocusEdentifica)
            ) {
                Text("Aceptar", color = AppColors.whitePerlaEdentifica)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                border = BorderStroke(1.dp, AppColors.FocusEdentifica),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = AppColors.FocusEdentifica)
            ) {
                Text("Cancelar")
            }
        }
    )
}


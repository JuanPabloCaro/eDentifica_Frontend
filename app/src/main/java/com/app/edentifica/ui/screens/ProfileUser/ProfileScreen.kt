package com.app.edentifica.ui.screens .ProfileUser

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
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.app.edentifica.R
import com.app.edentifica.data.model.User
import com.app.edentifica.navigation.AppScreen
import com.app.edentifica.ui.theme.AppColors
import com.app.edentifica.ui.theme.TextSizes
import com.app.edentifica.utils.AuthManager
import com.app.edentifica.viewModel.ProfileViewModel
import com.app.edentifica.viewModel.UsersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProfileScreen(
    navController: NavController,
    auth: AuthManager,
    onSignOutGoogle: () -> Unit,
    vmUsers: UsersViewModel,
    vmProfiles: ProfileViewModel
) {
    //VARIABLES Y CONSTANTES
    //para mostrar el dialogo de cerrar Sesion
    var showDialog by remember { mutableStateOf(false) }
//    //recojo al user Actual
//    val user = auth.getCurrentUser()

    // Llama a getUserByEmail cuando se inicia Profile
    LaunchedEffect(Unit) {
        auth.getCurrentUser()?.email?.let { vmUsers.getUserByEmail(it) }
    }

    // Observa el flujo de usuario en el ViewModel
    val userState by vmUsers.user.collectAsState()

    // Función para actualizar los datos del usuario
    LaunchedEffect(userState) {
        userState?.let { user ->
            vmUsers.getUserByEmail(user.email.email) // Vuelve a obtener los datos del usuario
        }
    }



    val onLogoutConfirmedProfile:()->Unit = {
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
                        navController.navigate(AppScreen.HomeScreen.route)
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
                            text = "Perfil",
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
                LogoutDialogProfile(
                    onConfirmLogout = {
                        onLogoutConfirmedProfile()
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
            BodyContentProfile(navController, vmUsers, userState,vmProfiles)
        }

    }


}






@Composable
fun BodyContentProfile(
    navController: NavController,
    vmUsers: UsersViewModel,
    userState: User?,
    vmProfiles: ProfileViewModel
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(60.dp))
        //Image
        Box(
            modifier = Modifier
                .size(150.dp) // Ajusta el tamaño deseado
                .clip(CircleShape)
                .background(color = Color.Gray) // Color de fondo opcional
        ) {
            if (userState != null && !userState.profile?.urlImageProfile.equals("")) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(userState.profile?.urlImageProfile)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Imagen",
                    placeholder = painterResource(id = R.drawable.profile),
                    contentScale = ContentScale.Crop, // Ajusta la escala de contenido según tus necesidades
                    modifier = Modifier
                        .size(150.dp) // Ajusta el tamaño deseado
                        .clip(CircleShape)
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.profile),
                    contentDescription = "edit image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .scale(1f)
                        .padding(0.dp)
                        .clip(CircleShape), // ajusta la altura según sea necesario
                    contentScale = ContentScale.Crop // Escala de la imagen
                )
            }
        }
        Spacer(modifier = Modifier.height(22.dp))

        UserInfoItem(label = "Nombre", value = userState?.name ?: "")
        UserInfoItem(label = "Apellido", value = userState?.lastName ?: "")
        UserInfoItem(label = "Fecha de Nacimiento", value = userState?.profile?.dateBirth.toString())
        UserInfoItem(label = "Descripcion del perfil", value = userState?.profile?.description ?: "")

        Spacer(modifier = Modifier.height(32.dp))

        //Button Ver Correos
        Box(modifier = Modifier.padding(20.dp, 0.dp, 20.dp, 0.dp)) {
            OutlinedButton(
                onClick = {
                    navController.navigate(AppScreen.EmailsScreen.route)
                },
                border = BorderStroke(1.dp, AppColors.FocusEdentifica),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = AppColors.FocusEdentifica),
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    "Ver mis correos",
                    fontSize = TextSizes.H3,
                    color = AppColors.FocusEdentifica
                )
            }
        }
        //Button Ver Telefonos
        Spacer(modifier = Modifier.height(16.dp))
        Box(modifier = Modifier.padding(20.dp, 0.dp, 20.dp, 0.dp)) {
            OutlinedButton(
                onClick = {
                    navController.navigate(AppScreen.PhonesScreen.route)
                },
                border = BorderStroke(1.dp, AppColors.FocusEdentifica),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = AppColors.FocusEdentifica),
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                     "Ver mis telefonos",
                    fontSize = TextSizes.H3,
                    color = AppColors.FocusEdentifica
                )
            }
        }
        //Button Ver Redes Sociales
        Spacer(modifier = Modifier.height(16.dp))
        Box(modifier = Modifier.padding(20.dp, 0.dp, 20.dp, 0.dp)) {
            OutlinedButton(
                onClick = {
                    navController.navigate(AppScreen.SocialNetworksScreen.route)
                },
                border = BorderStroke(1.dp, AppColors.FocusEdentifica),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = AppColors.FocusEdentifica),
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    "Ver mis redes sociales",
                    fontSize = TextSizes.H3,
                    color = AppColors.FocusEdentifica
                )
            }
        }

        //Button editar perfil
        Spacer(modifier = Modifier.height(32.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                Button(
                    onClick = {
                        if (userState != null) {
                            userState.profile?.let { vmProfiles.saveProfileEdit(it) }
                            vmUsers.saveUserEdit(userState)
                            navController.navigate(AppScreen.ProfileUserEditScreen.route)

                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AppColors.FocusEdentifica),
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(text = "Editar Perfil")
                }
            }
        }
    }
}

@Composable
fun UserInfoItem(label: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "$label: ", fontWeight = FontWeight.Bold)
        Text(text = value)
    }
}






/**
 * Funcion composable que se encarga de mostrar un alert para preguntar al
 * usuario si quiere continuar o cerrar sesion
 */
@Composable
fun LogoutDialogProfile(
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


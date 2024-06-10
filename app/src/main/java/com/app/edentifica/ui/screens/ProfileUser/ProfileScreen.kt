package com.app.edentifica.ui.screens .ProfileUser

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
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
                            text = stringResource(R.string.perfil),
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
    LazyColumn(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        item {
            Spacer(modifier = Modifier.height(60.dp))
            // Image
            Box(
                modifier = Modifier
                    .size(150.dp) // Ajusta el tamaño deseado
                    .clip(CircleShape)
                    .background(color = Color.Gray) // Color de fondo opcional
            ) {
                if (userState != null) {
                    userState.profile?.urlImageProfile?.let {
                        ClickableProfileImage(
                            navController = navController,
                            imageUrl = it
                        ) {
                            navController.navigate(AppScreen.ProfileUserPhotoEditScreen.route)
                        }
                    }
                }

            }
            Spacer(modifier = Modifier.height(22.dp))
        }


        item{
            Text(text = stringResource(R.string.edentificador), fontWeight = FontWeight.Bold, color = AppColors.mainEdentifica)
            Box(
                modifier = Modifier
                    .padding(top = 4.dp, bottom = 16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(
                        width = 2.dp,
                        color = AppColors.mainEdentifica,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .background(AppColors.mainEdentifica)
            ){
                Text(
                    text = userState?.edentificador.toString(),
                    modifier = Modifier.padding(top = 10.dp, bottom = 10.dp, start= 16.dp, end= 16.dp),
                    color = AppColors.whitePerlaEdentifica
                )
            }
        }

        item { UserInfoItem(label = stringResource(R.string.nombre), value = userState?.name ?: "") }
        item { UserInfoItem(label = stringResource(R.string.apellido), value = userState?.lastName ?: "") }
        item { UserInfoItem(label = stringResource(R.string.fecha_de_nacimiento), value = userState?.profile?.dateBirth.toString()) }
        item { UserInfoItem(label = stringResource(R.string.descripcion_del_perfil), value = userState?.profile?.description ?: "") }

        item { Spacer(modifier = Modifier.height(16.dp)) }

        // Button Ver Correos
        item {
            Box() {
                OutlinedButton(
                    onClick = {
                        navController.navigate(AppScreen.EmailsScreen.route)
                    },
                    border = BorderStroke(2.dp, AppColors.mainEdentifica),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = AppColors.mainEdentifica),
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            Icons.Outlined.List,
                            contentDescription = "Ver",
                            tint = AppColors.mainEdentifica,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp)) // Espacio entre el icono y el texto
                        Text(
                            text= stringResource(R.string.ver_mis_correos),
                            fontSize = TextSizes.H3,
                            color = AppColors.mainEdentifica
                        )
                    }
                }
            }
        }

        // Button Ver Teléfonos
        item { Spacer(modifier = Modifier.height(16.dp)) }
        item {
            Box() {
                OutlinedButton(
                    onClick = {
                        navController.navigate(AppScreen.PhonesScreen.route)
                    },
                    border = BorderStroke(2.dp, AppColors.mainEdentifica),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = AppColors.mainEdentifica),
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            Icons.Outlined.List,
                            contentDescription = "Ver",
                            tint = AppColors.mainEdentifica,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp)) // Espacio entre el icono y el texto
                        Text(
                            text= stringResource(R.string.ver_mis_telefonos),
                            fontSize = TextSizes.H3,
                            color = AppColors.mainEdentifica
                        )
                    }
                }
            }
        }

        // Button Ver Redes Sociales
        item { Spacer(modifier = Modifier.height(16.dp)) }
        item {
            Box() {
                OutlinedButton(
                    onClick = {
                        navController.navigate(AppScreen.SocialNetworksScreen.route)
                    },
                    border = BorderStroke(2.dp, AppColors.mainEdentifica),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = AppColors.mainEdentifica),
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            Icons.Outlined.List,
                            contentDescription = "Ver",
                            tint = AppColors.mainEdentifica,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp)) // Espacio entre el icono y el texto
                        Text(
                            text = stringResource(R.string.ver_mis_redes_sociales),
                            fontSize = TextSizes.H3,
                            color = AppColors.mainEdentifica
                        )
                    }
                }
            }
        }

        // Button Editar Perfil
        item { Spacer(modifier = Modifier.height(32.dp)) }
        item {
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
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Icon(
                                Icons.Outlined.Create,
                                contentDescription = "Ver",
                                tint = AppColors.whitePerlaEdentifica,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp)) // Espacio entre el icono y el texto
                            Text(
                                stringResource(R.string.editar_perfil),
                                fontSize = TextSizes.H3,
                                color = AppColors.whitePerlaEdentifica
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}


@Composable
fun UserInfoItem(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "$label: ", fontWeight = FontWeight.Bold, color = AppColors.mainEdentifica)
        Box(
            modifier = Modifier
                .padding(top = 4.dp, bottom = 16.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .border(
                    width = 2.dp,
                    color = AppColors.mainEdentifica,
                    shape = RoundedCornerShape(16.dp)
                )
                .background(Color.Transparent)
         ){
            Text(
                text = value,
                modifier = Modifier.padding(all = 12.dp)
            )
        }
    }
}


/**
 * Imagen de perfil clikeable
 */
@Composable
fun ClickableProfileImage(
    navController: NavController,  // Assuming you're using NavController for navigation
    imageUrl: String,
    onClick: () -> Unit  // Define your click action
) {
    Box(
        modifier = Modifier
            .clickable { onClick() }
    ) {
        if(!imageUrl.equals("")){
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Imagen",
                placeholder = painterResource(id = R.drawable.profile),
                contentScale = ContentScale.Crop, // Ajusta la escala de contenido según tus necesidades
                modifier = Modifier
                    .size(150.dp) // Ajusta el tamaño deseado
                    .clip(CircleShape)
            )
        }else{
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


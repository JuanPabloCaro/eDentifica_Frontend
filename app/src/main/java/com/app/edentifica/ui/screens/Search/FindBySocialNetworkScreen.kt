package com.app.edentifica.ui.screens.Search

import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
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
import com.app.edentifica.ui.screens.ClickableProfileImage
import com.app.edentifica.ui.theme.AppColors
import com.app.edentifica.ui.theme.TextSizes
import com.app.edentifica.utils.AuthManager
import com.app.edentifica.viewModel.UsersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FindBySocialNetworkScreen(
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


    val onLogoutConfirmedFindBySocial:()->Unit = {
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
                        if(auth.getCurrentUser()?.email!=null && userState?.validations?.get(0)?.isValidated ==true ){
                            userState?.profile?.urlImageProfile?.let {
                                ClickableProfileImage(
                                    navController = navController,
                                    imageUrl = it
                                ) {
                                    navController.navigate(AppScreen.ProfileUserScreen.route)
                                }
                            }
                        }


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
                LogoutDialogFindBySocial(
                    onConfirmLogout = {
                        onLogoutConfirmedFindBySocial()
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
            BodyContentFindBySocial(navController, vmUsers, userState)
        }

    }

}




@Composable
fun BodyContentFindBySocial(navController: NavController, vmUsers: UsersViewModel, userState: User?) {
    // Estado para almacenar el nombre del perfil de la red social ingresado por el usuario
    var socialName by remember { mutableStateOf("") }

    // Estado para controlar si se ha realizado una búsqueda
    var searchPerformedSocial by remember { mutableStateOf(false) }

    // Estado para almacenar el tipo de red social seleccionado
    var selectedSocialType by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        //Title
        Text(
            text = stringResource(R.string.vamos_a_encontrar_a_esa_persona_selecciona_aqu_el_tipo_de_red_social_y_escribe_el_nombre_del_usuario_del_perfil_si_es_facebook_pega_el_enlace_del_perfil),
            fontSize = TextSizes.H2,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        //Image
        Image(
            painter = painterResource(id = R.drawable.redes_sociales),
            contentDescription = "Redes Sociales",
            modifier = Modifier
                .fillMaxWidth()
                .scale(0.9f)
                .padding(0.dp), // ajusta la altura según sea necesario
            contentScale = ContentScale.Crop // Escala de la imagen
        )

        // Componente de selección de tipo de red social
        SelectSocialTypeDropdown(
            socialTypes = listOf("Facebook", "Instagram", "Twitter"),
            onSelectionChanged = { selectedType ->
                // Cuando se selecciona un tipo de red social, almacenar el valor
                selectedSocialType = selectedType
            }
        )

        // Campo de entrada para el nombre del perfil de la red social
        Spacer(modifier = Modifier.height(34.dp))
        TextField(
            label = { Text(text = stringResource(R.string.nombre_de_usuario), fontSize = TextSizes.Paragraph) },
            value = socialName,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            onValueChange = { socialName = it },
            placeholder = {Text(stringResource(R.string.ejemplo_de_prueba999))}
        )

        // Botón para enviar la búsqueda
        Spacer(modifier = Modifier.height(34.dp))
        Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
            Button(
                onClick = {
                    // Llamar a la función del ViewModel para buscar por red social
                    selectedSocialType?.let { type ->
                        vmUsers.getUserBySocialSearch(type, socialName)
                        vmUsers.saveFindString(type)
                        searchPerformedSocial = true
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = AppColors.FocusEdentifica),
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = stringResource(R.string.buscar_red_social))
            }
        }

        // Mostrar el resultado de la búsqueda o navegar a la pantalla de resultados
        if (searchPerformedSocial) {
            // Enviar los parámetros de búsqueda por el viewModel
            navController.navigate(AppScreen.ResultSearchSocialScreen.route)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectSocialTypeDropdown(
    socialTypes: List<String>,
    onSelectionChanged: (String) -> Unit
) {
//    var expanded by remember { mutableStateOf(false) }
    var selectedType by remember { mutableStateOf<String?>(null) }

    val dialog = remember { mutableStateOf(false) }

    Column {
        // Botón que muestra el tipo seleccionado y abre el diálogo al hacer clic
        Spacer(modifier = Modifier.height(34.dp))
        Box(modifier = Modifier.padding(20.dp, 0.dp, 20.dp, 0.dp)) {
            OutlinedButton(
                onClick = {
                    dialog.value = true
                },
                border = BorderStroke(1.dp, AppColors.FocusEdentifica),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = AppColors.FocusEdentifica)
            ) {
                Text(
                    selectedType ?: stringResource(R.string.seleccionar_tipo_de_red_social),
                    fontSize = TextSizes.H3,
                    color = AppColors.FocusEdentifica
                )
            }
        }

        if (dialog.value) {
            AlertDialog(
                contentColor = AppColors.whitePerlaEdentifica,
                onDismissRequest = { dialog.value = false },
                title = {
                    Text(
                        modifier = Modifier.padding(vertical = 16.dp),
                        text = stringResource(R.string.seleccionar_tipo_de_red_social),
                        fontSize = TextSizes.H3,
                        color = AppColors.mainEdentifica
                    ) },
                buttons = {
                    socialTypes.forEach { type ->
                        TextButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AppColors.FocusEdentifica),
                            onClick = {
                                selectedType = type
                                onSelectionChanged(type)
                                dialog.value = false
                            }
                        ) {
                            Text(
                                text = type,
                                fontSize = TextSizes.Paragraph,
                                color = AppColors.whitePerlaEdentifica
                            )
                        }
                    }
                }
            )
        }
    }
}




/**
 * Funcion composable que se encarga de mostrar un alert para preguntar al
 * usuario si quiere continuar o cerrar sesion
 */
@Composable
fun LogoutDialogFindBySocial(
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


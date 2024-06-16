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
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.app.edentifica.R
import com.app.edentifica.data.model.NetworkType
import com.app.edentifica.data.model.Phone
import com.app.edentifica.data.model.SocialNetwork
import com.app.edentifica.data.model.User
import com.app.edentifica.navigation.AppScreen
import com.app.edentifica.ui.screens.Search.SelectSocialTypeDropdown
import com.app.edentifica.ui.theme.AppColors
import com.app.edentifica.ui.theme.TextSizes
import com.app.edentifica.utils.AuthManager
import com.app.edentifica.viewModel.ProfileViewModel
import com.app.edentifica.viewModel.SocialViewModel
import com.app.edentifica.viewModel.UsersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SocialNetworksAddScreen(
    navController: NavController,
    auth: AuthManager,
    onSignOutGoogle: () -> Unit,
    vmUsers: UsersViewModel,
    vmSocial: SocialViewModel,
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
//    val emailCurrent by vmEmails.emailEdit.collectAsState()


    val onLogoutConfirmedSocialsAddScreen:()->Unit = {
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
                        navController.navigate(AppScreen.SocialNetworksScreen.route)
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
                            text = stringResource(R.string.agregar_red_social),
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
                LogoutDialogSocialsAdd(
                    onConfirmLogout = {
                        onLogoutConfirmedSocialsAddScreen()
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
            BodyContentSocialsAddScreen(navController,vmProfiles,userState)
        }

    }


}




@Composable
fun BodyContentSocialsAddScreen(
    navController: NavController,
    vmProfiles: ProfileViewModel,
    userState: User?
) {
    //VARIABLES
    // Estado para almacenar el tipo de red social seleccionado
    var selectedSocialType by remember { mutableStateOf<String?>(null) }
    var nameSocial by remember { mutableStateOf("") }

    var context= LocalContext.current

    var showDuplicateSocialError by remember { mutableStateOf(false) }

    // Observa el flujo de email en el ViewModel
    val socialInsertState by vmProfiles.socialInserted.collectAsState()

    // Observa el flujo de actualización del email y muestra un Toast cuando se completa la actualización
    LaunchedEffect(socialInsertState) {
        if (socialInsertState == true) {
            Toast.makeText(
                context,
                context.getString(R.string.la_red_social_se_insert_correctamente),
                Toast.LENGTH_SHORT
            ).show()
            vmProfiles.toNullSocialInserted()
            navController.navigate(AppScreen.SocialNetworksScreen.route)
        } else if (socialInsertState == false) {
            showDuplicateSocialError = true
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Spacer(modifier = Modifier.height(68.dp))
        //Image
        Image(
            painter = painterResource(id = R.drawable.socialinsert),
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
            text = stringResource(R.string.con_ctate_con_el_mundo_inserta_una_red_social),
            color = AppColors.mainEdentifica,
            fontSize = TextSizes.H2
        )

        // Campo de entrada para el tipo de red social
        Spacer(modifier = Modifier.height(34.dp))
        // Componente de selección de tipo de red social
        SelectSocialTypeDropdownAdd(
            socialTypes = listOf("Facebook", "Instagram", "Twitter"),
            onSelectionChanged = { selectedType ->
                // Cuando se selecciona un tipo de red social, almacenar el valor
                selectedSocialType = selectedType
            }
        )

        // Campo de entrada para el nombre del perfil de la red social
        Spacer(modifier = Modifier.height(34.dp))
        TextField(
            label = { Text(text = stringResource(R.string.nombre_del_perfil), fontSize = TextSizes.Paragraph) },
            value = nameSocial,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            onValueChange = { nameSocial = it },
            placeholder = {Text(stringResource(R.string.ejemplo_de_prueba999 ))}
        )

        if (showDuplicateSocialError) {
            Text(
                text = stringResource(R.string.la_red_social_ya_existe),
                color = AppColors.FocusEdentifica,
                fontSize = TextSizes.Paragraph
            )
        }

        Spacer(modifier = Modifier.height(34.dp))

        // Botón para insertar la red social
        Box(modifier = Modifier.padding(60.dp, 0.dp, 60.dp, 0.dp)) {
            Button(
                onClick = {
                    //si hay tipo seleccionado se inserta
                    selectedSocialType?.let { type ->
                        // Insertar la red social llamando a la función del ViewModel
                        userState?.let { user ->
                            val socialToInsert = SocialNetwork(id = null, networkType = NetworkType.fromString(selectedSocialType.toString())!!, socialName = nameSocial ,isVerified = false, idProfileUser = null)
                            user.profile?.id?.let { profileId ->
                                vmProfiles.insertSocialNetworkVm(socialToInsert, profileId)
                            }
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = AppColors.FocusEdentifica),
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = stringResource(R.string.insertar_red_social),
                    fontSize = TextSizes.H3,
                    color = AppColors.whitePerlaEdentifica
                )
            }
        }
    }
}









@Composable
fun SelectSocialTypeDropdownAdd(
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
            androidx.compose.material.AlertDialog(
                contentColor = AppColors.whitePerlaEdentifica,
                onDismissRequest = { dialog.value = false },
                title = {
                    Text(
                        modifier = Modifier.padding(vertical = 16.dp),
                        text = stringResource(R.string.seleccionar_tipo_de_red_social),
                        fontSize = TextSizes.H3,
                        color = AppColors.mainEdentifica
                    )
                },
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
fun LogoutDialogSocialsAdd(
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
package com.app.edentifica.ui.screens.ProfileUser.edit

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.app.edentifica.R
import com.app.edentifica.data.model.Profile
import com.app.edentifica.data.model.User
import com.app.edentifica.navigation.AppScreen
import com.app.edentifica.ui.theme.AppColors
import com.app.edentifica.ui.theme.TextSizes
import com.app.edentifica.utils.AuthManager
import com.app.edentifica.viewModel.ProfileViewModel
import com.app.edentifica.viewModel.UsersViewModel
import java.util.Calendar
import android.os.Bundle
import android.widget.DatePicker
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProfileUserEditScreen(
    navController: NavController,
    auth: AuthManager,
    onSignOutGoogle: () -> Unit,
    vmUsers: UsersViewModel,
    vmProfile: ProfileViewModel
) {
    //VARIABLES Y CONSTANTES
    //para mostrar el dialogo de cerrar Sesion
    var showDialog by remember { mutableStateOf(false) }

    // Llama a getUserByEmail pa
    LaunchedEffect(Unit) {
        auth.getCurrentUser()?.email?.let { vmUsers.getUserByEmail(it) }
    }
    // Observa el flujo de email A editar en el ViewModel
    val profileCurrent by vmProfile.profileEdit.collectAsState()
    val userCurrent by vmUsers.user.collectAsState()


    val onLogoutConfirmedProfileEditScreen:()->Unit = {
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
                        navController.navigate(AppScreen.ProfileUserScreen.route)
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
                            text = "Editar Perfil",
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
                LogoutDialogProfileEdit(
                    onConfirmLogout = {
                        onLogoutConfirmedProfileEditScreen()
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
            profileCurrent?.let { userCurrent?.let { it1 -> BodyContentProfileEditScreen(navController = navController, vmProfile = vmProfile,vmUsers=vmUsers, profileCurrent = it, user = it1) } }
        }

    }


}





@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BodyContentProfileEditScreen(
    vmProfile: ProfileViewModel,
    navController: NavController,
    profileCurrent: Profile,
    user: User,
    vmUsers: UsersViewModel
) {
    var name by remember { mutableStateOf(user.name) }
    var lastname by remember { mutableStateOf(user.lastName) }
    var description by remember { mutableStateOf(profileCurrent.description) }

    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    var showDatePicker by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(top = 30.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(68.dp))
        //Image
//        Image(
//            painter = painterResource(id = R.drawable.editphone),
//            contentDescription = "edit email",
//            modifier = Modifier
//                .fillMaxWidth()
//                .scale(0.7f)
//                .padding(0.dp), // ajusta la altura según sea necesario
//            contentScale = ContentScale.Crop // Escala de la imagen
//        )


        //NAME
        TextField(
            label = { Text(text = "Nombre", fontSize = TextSizes.Paragraph) },
            value = name,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            onValueChange = { name = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        //LASTNAME
        TextField(
            label = { Text(text = "Apellidos", fontSize = TextSizes.Paragraph) },
            value = lastname,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            onValueChange = { lastname = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        //DATEBIRTH
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Button(onClick = {
                showDatePicker = true
            }) {
                Text("Seleccionar Fecha de Nacimiento")
            }

            Spacer(modifier = Modifier.height(16.dp))

            selectedDate?.let {
                Text("Fecha seleccionada: ${it.format(formatter)}")
            }

            if (showDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    onDateSelected = { date ->
                        selectedDate = date
                        showDatePicker = false
                    }
                )
            }
        }


        //DESCRIPTION
        TextField(
            label = { Text(text = "Descripcion", fontSize = TextSizes.Paragraph) },
            value = description,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            onValueChange = { description = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )



        Box(modifier = Modifier.padding(60.dp, 0.dp, 60.dp, 0.dp)) {
            Button(
                onClick = {
                    //Actualizar el profile
                    val updateProfile = profileCurrent.copy(dateBirth=selectedDate, description = description)
                    //Actualizar el user
                    val updateUser = user.copy(name = name, lastName = lastname)
                    vmUsers.updateUserVM(updateUser)
                    vmUsers.toNullUserEdit()
                    vmProfile.updateProfileVM(updateProfile)
                    vmProfile.toNullProfileEdit()

                    vmUsers.getUserByEmail(user.email.email)

                    navController.navigate(AppScreen.ProfileUserScreen.route)
                },
                colors = ButtonDefaults.buttonColors(containerColor = AppColors.FocusEdentifica),
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = "Editar",
                    fontSize = TextSizes.H3,
                    color = AppColors.whitePerlaEdentifica
                )
            }
        }
    }
}




@Composable
fun DatePickerDialog(
    onDismissRequest: () -> Unit,
    onDateSelected: (LocalDate) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                onDateSelected(LocalDate.of(year, month + 1, dayOfMonth))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
}




/**
 * Funcion composable que se encarga de mostrar un alert para preguntar al
 * usuario si quiere continuar o cerrar sesion
 */
@Composable
fun LogoutDialogProfileEdit(
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
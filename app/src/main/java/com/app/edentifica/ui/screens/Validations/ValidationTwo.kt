package com.app.edentifica.ui.screens.Validations


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.app.edentifica.R
import com.app.edentifica.data.model.User
import com.app.edentifica.data.model.Validation
import com.app.edentifica.navigation.AppScreen
import com.app.edentifica.ui.theme.AppColors
import com.app.edentifica.ui.theme.TextSizes
import com.app.edentifica.utils.AuthManager
import com.app.edentifica.viewModel.UsersViewModel
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ValidationTwoScreen(
    navController: NavController,
    auth: AuthManager,
    vmUsers: UsersViewModel,
    onSignOutGoogle: () -> Unit,
) {
    //VARIABLES Y CONSTANTES

    //para mostrar el dialogo de cerrar Sesion
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val user = auth.getCurrentUser()

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

    Log.e("userBBDD", userState.toString())


    //Si no tiene ninguna validacion lo envio a las validaciones
    if(userState?.validations?.get(1)?.isValidated==true) { // importante modificacion en home

        navController.navigate(AppScreen.HomeScreen.route) {
            popUpTo(AppScreen.ValidationTwoScreen.route) {
                inclusive = true
            }
        }

//        if(userState?.validations?.get(0)?.isValidated==true && userState?.validations?.get(1)?.isValidated==false){// si le falta la validacion dos lo envio a esa pantalla
//            navController.navigate(AppScreen.ValidationTwoScreen.route){
//                popUpTo(AppScreen.HomeScreen.route){
//                    inclusive= true
//                }
//            }
//        }
    }



    val onLogoutConfirmedValidationTwo:()->Unit = {
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
                            Text(
                                text = if(!user?.displayName.isNullOrEmpty()) "Hola ${user?.displayName}" else "Bienvenid@",//welcomeMessage,
                                fontSize = TextSizes.H3,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = AppColors.whitePerlaEdentifica
                            )
                            Text(
                                text = if(!user?.email.isNullOrEmpty()) "${user?.email}" else "Usuario Anonimo",
                                fontSize = TextSizes.Footer,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = AppColors.whitePerlaEdentifica
                            )
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
                LogoutDialogValidationTwo(
                    onConfirmLogout = {
                        onLogoutConfirmedValidationTwo()
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
            BodyContentValidationTwo(navController,userState, vmUsers)
        }

    }

}










@Composable
fun BodyContentValidationTwo(
    navController: NavController,
    userCurrent: User?,
    vmUsers: UsersViewModel
) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var imageSelected = false
    var validations= ArrayList<Validation>()


    Column {
        Spacer(modifier = Modifier.height(80.dp))
        //Title
        Text(
            text = "Validacion 2",
            fontSize = TextSizes.H1,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            modifier = Modifier.wrapContentSize(Alignment.Center).padding(horizontal = 32.dp),
            text = "Para continuar con el proceso de validacion, por favor, toma una foto de tu rostro.",
            fontSize = TextSizes.H3,
            color = AppColors.mainEdentifica,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(10.dp))
        ImagePickerValidation(onImageSelected = { uri ->
            selectedImageUri = uri
            uri?.let {
                scope.launch {
                    try {
                        val imageUrl =uploadImageToFirebaseValidation(it,context)
//                        //Almaceno las validaciones por separado y cambio la validacion 2 a true
//                        var validationsUser1= userCurrent?.validations?.get(0)
//                        var validationsUser2= userCurrent?.validations?.get(1)?.copy(isValidated = true);
//
//                        //Agrego las validaciones a la lista
//                        if (validationsUser1 != null) {
//                            validations.add(0,validationsUser1)
//                        };
//                        if (validationsUser2 != null) {
//                            validations.add(1,validationsUser2)
//                        }
//
//                        //Actualizar la validacion del usuario con la lista creada anteriormente
//                        val updateUser =userCurrent?.copy(validations = validations)
//                        if (updateUser != null) {
//                            vmUsers.updateUserVM(updateUser)
//                        }

                    } catch (e: Exception) {
                        Log.e("Error Validation 2", e.message.toString())
                    }
                }
            }
        })

        selectedImageUri?.let { uri ->
            Image(
                painter = rememberImagePainter(data = uri),
                contentDescription = "image selected",
                modifier = Modifier
                    .fillMaxWidth()
                    .scale(1f)
                    .padding(0.dp), // ajusta la altura según sea necesario
                contentScale = ContentScale.Crop // Escala de la imagen
            )
            imageSelected= true
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (imageSelected){
            Box(modifier = Modifier.padding(60.dp, 0.dp, 60.dp, 0.dp)) {
                Button(
                    onClick = {

                        try {
                            //Almaceno las validaciones por separado y cambio la validacion 2 a true
                            var validationsUser1= userCurrent?.validations?.get(0)
                            var validationsUser2= userCurrent?.validations?.get(1)?.copy(isValidated = true);

                            //Agrego las validaciones a la lista
                            if (validationsUser1 != null) {
                                validations.add(0,validationsUser1)
                            };
                            if (validationsUser2 != null) {
                                validations.add(1,validationsUser2)
                            }

                            //Actualizar la validacion del usuario con la lista creada anteriormente
                            val updateUser =userCurrent?.copy(validations = validations)
                            if (updateUser != null) {
                                vmUsers.updateUserVM(updateUser)
                            }

                            if (userCurrent != null) {
                                vmUsers.getUserByEmail(userCurrent.email.email)
                            }

                            navController.navigate(AppScreen.ValidationTwoSuccessScreen.route)//cambiar a pantalla exitosa validacion 2

                        } catch (e: Exception) {
                            Log.e("Error Validation 2", e.message.toString())
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AppColors.FocusEdentifica),
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(
                        text = "Enviar",
                        fontSize = TextSizes.H3,
                        color = AppColors.whitePerlaEdentifica
                    )
                }
            }
        }
//        else{
//            // Muestra la imagen actual del perfil si existe
//            userCurrent?.profile?.urlImageProfile?.let { imageUrl ->
//                //Image
//                Image(
//                    painter = rememberImagePainter(data = imageUrl),
//                    contentDescription = "image profile",
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .scale(1f)
//                        .padding(0.dp), // ajusta la altura según sea necesario
//                    contentScale = ContentScale.Crop // Escala de la imagen
//                )
//            }
//        }
    }
}

suspend fun uploadImageToFirebaseValidation(uri: Uri, context: Context): String {
    val storage = FirebaseStorage.getInstance()
    val storageRef = storage.reference
    val imagesRef: StorageReference = storageRef.child("images/${uri.lastPathSegment}")

    val uploadTask = imagesRef.putFile(uri)
    val taskSnapshot = uploadTask.await()

    return taskSnapshot.storage.downloadUrl.await().toString()
}


@Composable
fun ImagePickerValidation(
    onImageSelected: (Uri?) -> Unit
) {
    val context = LocalContext.current
//    val launcher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.GetContent(),
//        onResult = { bitmap ->
//            onImageSelected(bitmap)
//        }
//    )

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview(),
        onResult = { bitmap ->
            val uri = bitmap?.let {
                saveBitmapToInternalStorageValidation(context, it)
            }
            onImageSelected(uri)
        }
    )

    Column {
//        Button(
//            onClick = { launcher.launch("image/*") },
//            colors = ButtonDefaults.buttonColors(containerColor = AppColors.secondaryEdentifica),
//            shape = RoundedCornerShape(50.dp),
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(50.dp)
//        ) {
//            Text(
//                text = "Select from Gallery",
//                fontSize = TextSizes.H3,
//                color = AppColors.whitePerlaEdentifica
//            )
//        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { cameraLauncher.launch() },
            colors = ButtonDefaults.buttonColors(containerColor = AppColors.secondaryEdentifica),
            shape = RoundedCornerShape(50.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(
                text = "Tomar Foto",
                fontSize = TextSizes.H3,
                color = AppColors.whitePerlaEdentifica
            )
        }
        Spacer(modifier = Modifier.height(22.dp))
    }
}





fun saveBitmapToInternalStorageValidation(context: Context, bitmap: Bitmap): Uri? {
    val filename = "profile_image_${System.currentTimeMillis()}.jpg"
    var fos: FileOutputStream? = null
    var uri: Uri?
    try {
        fos = context.openFileOutput(filename, Context.MODE_PRIVATE)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        fos.close()
        val file = File(context.filesDir, filename)
        uri = Uri.fromFile(file)
    } catch (e: Exception) {
        e.printStackTrace()
        uri = null
    } finally {
        fos?.close()
    }
    return uri
}
















/**
 * Funcion composable que se encarga de mostrar un alert para preguntar al
 * usuario si quiere continuar o cerrar sesion
 */
@Composable
fun LogoutDialogValidationTwo(
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

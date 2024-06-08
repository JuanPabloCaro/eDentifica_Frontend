package com.app.edentifica.ui.screens.ProfileUser.edit


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.app.edentifica.R
import com.app.edentifica.data.model.User
import com.app.edentifica.navigation.AppScreen
import com.app.edentifica.ui.theme.AppColors
import com.app.edentifica.ui.theme.TextSizes
import com.app.edentifica.utils.AuthManager
import com.app.edentifica.viewModel.UsersViewModel
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.rememberImagePainter
import com.app.edentifica.viewModel.ProfileViewModel
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun EditPhoto(
    navController: NavController,
    auth: AuthManager,
    onSignOutGoogle: () -> Unit,
    vmUsers: UsersViewModel,
    vmProfiles: ProfileViewModel
) {
    //VARIABLES Y CONSTANTES
    //para mostrar el dialogo de cerrar Sesion
    var showDialog by remember { mutableStateOf(false) }

    // Llama a getUserByEmail pa
    LaunchedEffect(Unit) {
        auth.getCurrentUser()?.email?.let { vmUsers.getUserByEmail(it) }
    }
    // Observa el flujo del user
    val userCurrent by vmUsers.user.collectAsState()


    val onLogoutConfirmedPhotoEdit:()->Unit = {
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
                            text = "Editar Foto",
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
                LogoutDialogPhotoEdit(
                    onConfirmLogout = {
                        onLogoutConfirmedPhotoEdit()
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
            BodyContentPhotoEdit(navController = navController, userCurrent, vmProfiles)
        }

    }


}





@Composable
fun BodyContentPhotoEdit(
    navController: NavController,
    userCurrent: User?,
    vmProfiles: ProfileViewModel
) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var imageSelected = false


    Column {
        Spacer(modifier = Modifier.height(64.dp))
        ImagePicker(onImageSelected = { uri ->
            selectedImageUri = uri
            uri?.let {
                scope.launch {
                    try {
                        val imageUrl = uploadImageToFirebase(it, context)
                        //Actualizar el profile
                        val updateProfile =userCurrent?.profile?.copy(urlImageProfile = imageUrl)
                        if (updateProfile != null) {
                            vmProfiles.updateProfileVM(updateProfile)
                        }
                    } catch (e: Exception) {
                        // Maneja el error
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

        Spacer(modifier = Modifier.height(50.dp))

        if (imageSelected){
            Box(modifier = Modifier.padding(60.dp, 0.dp, 60.dp, 0.dp)) {
                Button(
                    onClick = {
                        navController.navigate(AppScreen.ProfileUserScreen.route)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AppColors.FocusEdentifica),
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(
                        text = "Seleccionar",
                        fontSize = TextSizes.H3,
                        color = AppColors.whitePerlaEdentifica
                    )
                }
            }
        }else{
            // Muestra la imagen actual del perfil si existe
            userCurrent?.profile?.urlImageProfile?.let { imageUrl ->
                //Image
                Image(
                    painter = rememberImagePainter(data = imageUrl),
                    contentDescription = "image profile",
                    modifier = Modifier
                        .fillMaxWidth()
                        .scale(1f)
                        .padding(0.dp), // ajusta la altura según sea necesario
                    contentScale = ContentScale.Crop // Escala de la imagen
                )
            }
        }
    }
}

suspend fun uploadImageToFirebase(uri: Uri, context: Context): String {
    val storage = FirebaseStorage.getInstance()
    val storageRef = storage.reference
    val imagesRef: StorageReference = storageRef.child("images/${uri.lastPathSegment}")

    val uploadTask = imagesRef.putFile(uri)
    val taskSnapshot = uploadTask.await()

    return taskSnapshot.storage.downloadUrl.await().toString()
}


@Composable
fun ImagePicker(
    onImageSelected: (Uri?) -> Unit
) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { bitmap ->
            onImageSelected(bitmap)
        }
    )

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview(),
        onResult = { bitmap ->
            val uri = bitmap?.let {
                saveBitmapToInternalStorage(context, it)
            }
            onImageSelected(uri)
        }
    )

    Column {
        Button(
            onClick = { launcher.launch("image/*") },
            colors = ButtonDefaults.buttonColors(containerColor = AppColors.secondaryEdentifica),
            shape = RoundedCornerShape(50.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(
                text = "Select from Gallery",
                fontSize = TextSizes.H3,
                color = AppColors.whitePerlaEdentifica
            )
        }
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
                text = "Take a Photo",
                fontSize = TextSizes.H3,
                color = AppColors.whitePerlaEdentifica
            )
        }
        Spacer(modifier = Modifier.height(22.dp))
    }
}





fun saveBitmapToInternalStorage(context: Context, bitmap: Bitmap): Uri? {
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
fun LogoutDialogPhotoEdit(
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















// funcion para habilitar permisos
//{
//    var showPermissionDialog by remember { mutableStateOf(false) }
//        if (showPermissionDialog) {
//            AlertDialog(
//                onDismissRequest = { showPermissionDialog = false },
//                confirmButton = {
//                    TextButton(
//                        onClick = {
//                            showPermissionDialog = false
//                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//                            val uri = Uri.fromParts("package", context.packageName, null)
//                            intent.data = uri
//                            context.startActivity(intent)
//                        }
//                    ) {
//                        Text("Go to Settings")
//                    }
//                },
//                dismissButton = {
//                    TextButton(onClick = { showPermissionDialog = false }) {
//                        Text("Cancel")
//                    }
//                },
//                title = { Text("Permissions Required") },
//                text = { Text("Camera and Storage permissions are required to change your profile photo. Please enable them in the app settings.") }
//            )
//        }

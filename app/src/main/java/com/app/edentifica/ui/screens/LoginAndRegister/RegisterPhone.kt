package com.app.edentifica.ui.screens.LoginAndRegister

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.app.edentifica.R
import com.app.edentifica.data.model.Phone
import com.app.edentifica.data.model.User
import com.app.edentifica.navigation.AppScreen
import com.app.edentifica.ui.theme.AppColors
import com.app.edentifica.ui.theme.TextSizes
import com.app.edentifica.utils.AuthManager
import com.app.edentifica.viewModel.PhonesViewModel
import com.app.edentifica.viewModel.UsersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnusedMaterialScaffoldPaddingParameter")
@Composable
fun RegisterPhoneScreen(
    navController: NavController,
    auth: AuthManager,
    vmUsers: UsersViewModel,
    vmPhones: PhonesViewModel,
) {
    //VARIABLES Y CONSTANTES
    var context = LocalContext.current

    //recojo al user Actual
    val currentUser = auth.getCurrentUser()

    // Llama a getUserByEmail cuando se inicia HomeScreen
    LaunchedEffect(Unit) {
        auth.getCurrentUser()?.email?.let { vmUsers.getUserByEmail(it) }
    }

    // Observa el flujo de usuario en el ViewModel
    val userState by vmUsers.user.collectAsState()


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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColors.whitePerlaEdentifica) //Color de fondo de la aplicacion
                .padding(horizontal = 8.dp)
        ){
            //funcion composable que pinta el contenido de home
            BodyContentRegisterPhone(navController, userState, context,vmPhones)
        }
    }
}




@Composable
fun BodyContentRegisterPhone(
    navController: NavController,
    userState: User?,
    context: Context,
    vmPhones: PhonesViewModel
) {
    //VARIABLES
    var userPhone by remember { mutableStateOf("") }

    // Observa el flujo de phone en el ViewModel
    val phoneUpsateState by vmPhones.phoneUpdated.collectAsState()

    // Observa el flujo de actualización del teléfono y muestra un Toast cuando se completa la actualización
    LaunchedEffect(phoneUpsateState) {
        if (phoneUpsateState==true) {
            Toast.makeText(
                context,
                context.getString(R.string.el_tel_fono_se_inserto_correctamente),
                Toast.LENGTH_SHORT
            ).show()
            navController.navigate(AppScreen.ValidationOneScreen.route)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Spacer(modifier = Modifier.height(68.dp))
        //Image
        Image(
            painter = painterResource(id = R.drawable.movil),
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
            text = stringResource(R.string.vamos_a_mantenernos_conectados_no_tienes_un_n_mero_de_tel_fono_registrado_por_favor_introduce_tu_n_mero_de_whatsapp_incluyendo_el_prefijo_del_pa_s),
            color = AppColors.mainEdentifica,
            fontSize = TextSizes.H2
        )

        // Campo de entrada para la respuesta del usuario
        Spacer(modifier = Modifier.height(34.dp))
        TextField(
            label = { Text(text = "Phone", fontSize = TextSizes.Paragraph) },
            value = userPhone,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            onValueChange = { userPhone = it },
            placeholder = {Text("34xxxxxxxxx")}

        )
        Spacer(modifier = Modifier.height(34.dp))

        Box(modifier = Modifier.padding(60.dp, 0.dp, 60.dp, 0.dp)) {
            Button(
                onClick = {
                    // Actualizar el teléfono en el objeto User y llamar a la función del ViewModel para actualizarlo en la base de datos
                    userState?.let { user ->
                        val updatedUser = user.copy(phone = Phone(id = user.phone.id, phoneNumber = userPhone, isVerified = user.phone.isVerified, idProfileUser = user.phone.idProfileUser))
                        vmPhones.updatePhoneVM(updatedUser.phone)
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
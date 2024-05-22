package com.app.edentifica.ui.screens.LoginAndRegister

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.app.edentifica.R
import com.app.edentifica.navigation.AppScreen
import com.app.edentifica.utils.AuthManager
import com.app.edentifica.utils.AuthRes
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ForgotPasswordScreen(navController: NavController, auth: AuthManager /*altaUsuarioViewModel: AltaUsuarioViewModel*/){

    Scaffold(topBar = {
        TopAppBar(
            navigationIcon = {
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(imageVector= Icons.Default.ArrowBack, contentDescription="ArrowBack")
                }
            },
            title = {
                Text(text = "Back", fontSize = 26.sp, fontStyle = FontStyle.Italic, color= Color.Gray)
            }
        )
    },
        bottomBar = {
            BottomAppBar (){
                Text(
                    text = "Version 1.0 @Copyrigth 2024 Todos los derechos reservados",
                    fontSize = 12.sp,
                    fontStyle = FontStyle.Italic,
                    color= Color.DarkGray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center)
                )
            }
        }
    ) {
        BodyContentForgotPassword(navController, auth /*altaUsuarioViewModel*/)
    }
}

@Composable
fun BodyContentForgotPassword(navController: NavController, auth: AuthManager) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ){
        FormularioForgotPassword(Modifier.align(Alignment.Center), navController, auth/* loginViewModel*/)
    }
}

@Composable
fun FormularioForgotPassword(align: Modifier, navController: NavController, auth: AuthManager) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    Column(
        modifier = align.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(150.dp))
        //Logo
        Image(
            painter = painterResource(id = R.drawable.nombre_edentifica),
            contentDescription = "Logo eDentifica",
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp), // ajusta la altura según sea necesario
            contentScale = ContentScale.Crop // Escala de la imagen
        )

        //Title
        Text(text = "Forgot your password", color = Color.DarkGray, fontWeight = FontWeight.Bold, fontSize = 24.sp)

        //field email
        Spacer(modifier = Modifier.height(50.dp))
        TextField(
            label = { Text(text = "Email") },
            value = email,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            onValueChange = { email = it })

        //button send email
        Spacer(modifier = Modifier.height(30.dp))
        Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
            Button(
                onClick = {
                    scope.launch {
                        when(val res = auth.resetPassword(email)) {
                            is AuthRes.Succes -> {
                                Toast.makeText(context, "Mail sent", Toast.LENGTH_SHORT).show()
                                navController.navigate(AppScreen.LoginScreen.route)
                            }
                            is AuthRes.Error -> {
                                Toast.makeText(context, "Error sending mail", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                },
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Password recovery")
            }
        }
    }
}

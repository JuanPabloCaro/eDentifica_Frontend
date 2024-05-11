package com.app.edentifica.ui.screens

import android.annotation.SuppressLint
import android.content.Context
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
import androidx.compose.foundation.text.ClickableText
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.app.edentifica.R
import com.app.edentifica.utils.AuthManager
import com.app.edentifica.utils.AuthRes
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RegisterScreen(navController: NavController, auth: AuthManager ){

    //VARIABLES Y CONSTANTES
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()


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
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            //Logo
            Image(
                painter = painterResource(id = R.drawable.nombre_edentifica),
                contentDescription = "Logo eDentifica",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp), // ajusta la altura según sea necesario
                contentScale = ContentScale.Crop // Escala de la imagen
            )

            //title
            Text(
                text = "Create an account",
                color = Color.DarkGray,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )

            //field name
            Spacer(modifier = Modifier.height(40.dp))
            TextField(
                label = { Text(text = "Name") },
                value = name,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                onValueChange = { name = it })

            //field last name
            Spacer(modifier = Modifier.height(20.dp))
            TextField(
                label = { Text(text = "Last Name") },
                value = lastName,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                onValueChange = { lastName = it })

            //field phone
            Spacer(modifier = Modifier.height(20.dp))
            TextField(
                label = { Text(text = "Phone") },
                value = phone,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                onValueChange = { phone = it })

            //field email
            Spacer(modifier = Modifier.height(20.dp))
            TextField(
                label = { Text(text = "Email") },
                value = email,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                onValueChange = { email = it })

            //field password
            Spacer(modifier = Modifier.height(20.dp))
            TextField(
                label = { Text(text = "Password") },
                value = password,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                onValueChange = { password = it })

            //button signUp
            Spacer(modifier = Modifier.height(30.dp))
            Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                Button(
                    onClick = {
                        scope.launch {

                            signUp(name, lastName, phone, email, password, auth, context, navController)
                        }
                    },
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(text = "Sign Up")
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            ClickableText(
                text = AnnotatedString("Already have an account? Sign in"),
                onClick = {
                    navController.popBackStack()
                },
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Default,
                    textDecoration = TextDecoration.Underline,
                    color = Color.Blue
                )
            )
        }
    }

}

private suspend fun signUp(
    name: String,
    lastName: String,
    phone: String,
    email: String,
    password: String,
    auth: AuthManager,
    context: Context,
    navController: NavController
) {
    if(name.isNotEmpty() && lastName.isNotEmpty() && phone.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()){
        when(val result = auth.createUserWithEmailandPassword(email, password)){ // agregar la linea de retrofit para insertar el user en la base de datos
            is AuthRes.Succes ->{
                Toast.makeText(context, "Successful registration", Toast.LENGTH_SHORT).show()
                navController.popBackStack()
            }
            is AuthRes.Error ->{
                Toast.makeText(context, "Error SignUp: ${result.errorMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }else{
        Toast.makeText(context, "There are empty fields", Toast.LENGTH_SHORT).show()
    }
}

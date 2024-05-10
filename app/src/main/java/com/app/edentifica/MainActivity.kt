package com.app.edentifica

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.app.edentifica.navigation.AppNavigation
import com.app.edentifica.navigation.AppScreen
import com.app.edentifica.ui.theme.EDentificaTheme
import com.app.edentifica.utils.googleAuth.GoogleAuthUiClient
import com.app.edentifica.utils.googleAuth.SignInViewModel
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch

/**
 * Proyecto eDentifica:
 * Estandarizar la autenticación de personas a nivel global. eDentifica dará fe de que el
 * usuario de la aplicación es realmente quien dice ser, de tal forma que todo mensaje (correo,
 * perfil RRSS, mensaje por WhatsApp / Telegram, etc.) firmado a través de eDentifica
 * tendrá el aval y la validez que se dará de una forma inequívoca.
 * Gracias a eDentifica se limitará el anonimato virtual, se invitará a que nadie acepte
 * contenido que no esté con perfil de eDentifica.
 *
 * @version 1.0
 * @author Juan Pablo Caro Peñuela
 * @since 2024-03-8
 */
class MainActivity : ComponentActivity() {
    private val googleAuthUiClient by lazy{
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EDentificaTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //Inicializo la el view model y los componentes necesarios para que se inicie sesion con google
                    val navController= rememberNavController()
                    val viewModel= viewModel<SignInViewModel>()
                    val state by viewModel.state.collectAsStateWithLifecycle()
                    val launcher= rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.StartIntentSenderForResult(),
                        onResult = {result ->
                            if(result.resultCode == RESULT_OK){
                                lifecycleScope.launch{
                                    val signInResult = googleAuthUiClient.signInWithIntent(
                                        intent = result.data ?: return@launch
                                    )
                                    viewModel.onSignInResult(signInResult)
                                }
                            }

                        }
                    )


                    LaunchedEffect(key1 = state.isSignInSuccessful) {
                        if(state.isSignInSuccessful){
                            //navController.navigate(AppScreen.HomeScreen.route)
                            Toast.makeText(
                                applicationContext,
                                "Sing in Successful",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }


                    //Este es el componente que se encarga de la navegacion y sabe cual es la primera pantalla
                    //This is the component that is in charge of navigation and knows which is the first screen.
                    AppNavigation(
                        state = state,
                        onSignInClick = {
                            lifecycleScope.launch {
                                val signInIntentSender = googleAuthUiClient.signIn()
                                launcher.launch(
                                    IntentSenderRequest.Builder(
                                        signInIntentSender ?: return@launch
                                    ).build()
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}




@Preview(showBackground = true)
@Composable
fun eDentificaPreview() {
    EDentificaTheme{
        //AppNavigation(this)
    }
}
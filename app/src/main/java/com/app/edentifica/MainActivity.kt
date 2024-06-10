package com.app.edentifica

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.edentifica.navigation.AppNavigation
import com.app.edentifica.ui.theme.EDentificaTheme
import com.app.edentifica.viewModel.UsersViewModel
import com.app.edentifica.utils.googleAuth.GoogleAuthUiClient
import com.app.edentifica.utils.googleAuth.SignInViewModel
import com.app.edentifica.viewModel.EmailViewModel
import com.app.edentifica.viewModel.PhonesViewModel
import com.app.edentifica.viewModel.ProfileViewModel
import com.app.edentifica.viewModel.SocialViewModel
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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

    //ViewModels
    val vmUsers by viewModels<UsersViewModel>()
    val vmPhones by viewModels<PhonesViewModel>()
    val vmEmails by viewModels<EmailViewModel>()
    val vmSocialNetworks by viewModels<SocialViewModel>()
    val vmProfiles by viewModels<ProfileViewModel>()

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
                    val viewModel= viewModel<SignInViewModel>()
                    val state by viewModel.state.collectAsStateWithLifecycle()
                    val context= LocalContext.current

                    //Esta funcion es de google para saber si todo esta ok con el inicio de sesion para enviar el intent
                    val launcher= rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.StartIntentSenderForResult(),
                        onResult = {result ->
                            if(result.resultCode == RESULT_OK){
                                lifecycleScope.launch{
                                    val signInResult = googleAuthUiClient.signInWithIntent(
                                        intent = result.data ?: return@launch
                                    )
                                    viewModel.onSignInResult(signInResult, context)
                                }
                            }

                        }
                    )


                    //Aqui, si el inicio es correcto entonces muestra un mensaje por pantalla y se resetea el viewModel
                    LaunchedEffect(key1 = state.isSignInSuccessful) {
                        if(state.isSignInSuccessful){
                            Toast.makeText(
                                applicationContext,
                                "Sing in Successful",
                                Toast.LENGTH_SHORT
                            ).show()
                            viewModel.resetState()
                        }
                    }



                    //Este es el componente que se encarga de la navegacion y sabe cual es la primera pantalla
                    //This is the component that is in charge of navigation and knows which is the first screen.
                    AppNavigation(
                        state = state,
                        onSignInClickGoogle = {
                            lifecycleScope.launch {
                                val signInIntentSender = googleAuthUiClient.signIn()
                                launcher.launch(
                                    IntentSenderRequest.Builder(
                                        signInIntentSender ?: return@launch
                                    ).build()
                                )
                            }
                        },
                        onSignOutGoogle = {
                            lifecycleScope.launch {
                                googleAuthUiClient.signOut()
                                Toast.makeText(
                                    applicationContext,
                                    "Signed out",
                                    Toast.LENGTH_SHORT
                                ).show()

                                // Reiniciar la actividad principal, asi pueden volver a iniciar sesion con otra cuenta de google
                                val intent = Intent(context, MainActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                                finish()

                            }
                        },
                        vmUsers= vmUsers,
                        vmPhones=vmPhones,
                        vmEmails=vmEmails,
                        vmProfiles=vmProfiles,
                        vmSocialNetworks=vmSocialNetworks
                    )
                }
            }
        }
    }
}
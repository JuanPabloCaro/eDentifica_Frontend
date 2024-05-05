package com.app.edentifica

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.app.edentifica.navigation.AppNavigation
import com.app.edentifica.ui.theme.EDentificaTheme

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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EDentificaTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //Este es el componente que se encarga de la navegacion y sabe cual es la primera pantalla
                    //This is the component that is in charge of navigation and knows which is the first screen.
                    AppNavigation()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun eDentificaPreview() {
    EDentificaTheme{
        AppNavigation()
    }
}
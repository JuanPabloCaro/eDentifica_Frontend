------------------------------------------------------------------------------
# eDentifica Frontend
------------------------------------------------------------------------------
eDentifica es una aplicación diseñada para que las personas puedan registrar sus correos, teléfonos y redes sociales, contribuyendo así a evitar futuros fraudes o estafas. 
La validación de datos se realiza a través de una llamada con un reto matemático o mediante la captura de una foto del rostro, asegurando que no se trata de un robot ni de 
una suplantación de identidad.

## Descripción del Proyecto
El frontend de eDentifica se ha desarrollado en Android Studio utilizando el lenguaje Kotlin. Este cliente móvil interactúa con el servidor backend de eDentifica, gestionando 
las autenticaciones, notificaciones y almacenamiento de imágenes a través de Firebase, y realizando llamadas al servidor mediante Retrofit.

## Instalación y Requisitos Previos

### Requisitos del Sistema
Sistema Operativo: Windows 10 o superior
Procesador: Intel Core i5 o superior
Almacenamiento: 512GB SSD mínimo
Memoria RAM: 8GB mínimo

### Herramientas Necesarias
Android Studio
Kotlin
Retrofit
Firebase

### Clonar el Repositorio
Para clonar el repositorio, sigue estos pasos:

1. Accede al siguiente enlace: https://github.com/JuanPabloCaro/eDentifica_Frontend
2. Clona el repositorio en tu máquina local.
git clone https://github.com/JuanPabloCaro/eDentifica_Frontend.git
cd eDentifica_Frontend

## Configuración del Entorno
### Firebase
Configura Firebase para la autenticación, el almacenamiento de imágenes y las notificaciones push:

1. Ve a la Consola de Firebase.

2. Crea un nuevo proyecto.

3. Configura Firebase Authentication, Firebase Storage y Firebase Cloud Messaging.

4. Descarga el archivo google-services.json y colócalo en la carpeta app de tu proyecto.

### Retrofit
Asegúrate de que la URL de Retrofit coincida con la URL del servidor backend. Actualiza la configuración de Retrofit en tu proyecto según la URL del backend.

## Estructura del Proyecto
El proyecto está organizado en las siguientes carpetas:

* data: Maneja la capa de datos, incluyendo los repositorios y fuentes de datos.
* navigation: Gestiona la navegación entre pantallas.
* ui: Contiene las pantallas y temas de la interfaz de usuario.
* screens: Pantallas de la aplicación.
* LoginAndRegister: Pantallas de inicio de sesión y registro.
* ProfileUser: Pantalla de perfil de usuario.
* Results: Pantalla de resultados.
* Search: Pantalla de búsqueda.
* Validations: Pantalla de validaciones.
* themes: Temas de la aplicación.
* utils: Utilidades y autenticaciones.
* viewmodel: Modelos de vista.
* main activity: Actividad principal de la aplicación.

## Instrucciones de Ejecución

### Ejecución del Servidor Backend
Para que el frontend funcione correctamente, asegúrate de que el servidor backend esté encendido y accesible. Puedes seguir las instrucciones del README 
del backend para ejecutarlo.

### Ejecución del Frontend
1. Abre Android Studio y carga el proyecto clonado.

2. Conecta un dispositivo Android o configura un emulador.

3. Compila y ejecuta la aplicación.

## Características de la Aplicación

### Autenticación
Correo y Contraseña: Permite a los usuarios registrarse e iniciar sesión utilizando su correo electrónico y una contraseña.
Google Sign-In: Permite a los usuarios autenticarse mediante su cuenta de Google.
### Notificaciones
Utiliza Firebase Cloud Messaging para enviar y recibir notificaciones push.

### Almacenamiento de Imágenes
Utiliza Firebase Storage para almacenar imágenes, incluyendo las capturadas durante el proceso de validación.

## Pantallas Principales
* Login and Register: Pantallas para el registro e inicio de sesión de usuarios.
* Profile User: Pantalla donde los usuarios pueden ver y editar su perfil.
* Results: Pantalla que muestra los resultados de las búsquedas y validaciones.
* Search: Pantalla para buscar otros usuarios o información.
* Validations: Pantalla para realizar y verificar validaciones.

## Contribuciones
Por el momento, las contribuciones no están habilitadas ya que es un proyecto privado.

## Licencia
El proyecto no tiene una licencia específica asignada. Para cualquier consulta al respecto, por favor contacta al autor.

## Contacto
Autor: eDentifica

Correo: jcaropenuela@gmail.com / informacion@edentifica.com
Página web: edentifica.com

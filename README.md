# TaskManagerApp - Gestión de Tareas con Clean Architecture y MVVM

![Android Studio](https://img.shields.io/badge/Android%20Studio-3DDC84?style=for-the-badge&logo=android-studio&logoColor=white)
![Kotlin](https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpack-compose&logoColor=white)
![Firebase](https://img.shields.io/badge/firebase-%23039BE5.svg?style=for-the-badge&logo=firebase)

## 1. Información General y Autores
**TaskManagerApp** es una solución móvil integral diseñada para la gestión eficiente de tareas personales. El objetivo principal del proyecto es demostrar la implementación de una arquitectura robusta, escalable y mantenible en Android, garantizando la integridad de los datos mediante sincronización en tiempo real con la nube y persistencia local para el trabajo fuera de línea.

### Autores
*   **[Nombre del Integrante 1]** - Desarrollador Principal
*   **[Nombre del Integrante 2]** - Especialista en QA / Documentación
*   *SENA - Especialización Tecnológica en Desarrollo de Aplicaciones Móviles*

---

## 2. Tecnologías y Arquitectura Implementadas

### Stack Tecnológico
*   **Kotlin:** Lenguaje de programación oficial para el desarrollo moderno en Android.
*   **Jetpack Compose & Material 3:** UI declarativa con componentes de diseño de última generación.
*   **Firebase BoM:** Gestión centralizada de dependencias para **Firebase Authentication** y **Cloud Firestore**.
*   **Room Database:** Persistencia local para la gestión de borradores offline.
*   **Kotlin Coroutines & Flow:** Manejo de asincronía y flujos de datos reactivos en tiempo real.
*   **KAPT:** Procesamiento de anotaciones estable para la generación de código de Room.

### Arquitectura (Clean Architecture + MVVM)
El proyecto se divide en tres capas fundamentales para separar responsabilidades:

1.  **Capa de Dominio (Domain):** Contiene la lógica de negocio pura, modelos de datos (`Task`) y los **Casos de Uso** (Use Cases). Es totalmente independiente de librerías externas.
2.  **Capa de Datos (Data):** Implementa las interfaces de los repositorios. Gestiona la comunicación con **Firebase** (remoto) y **Room** (local), decidiendo la fuente de la verdad.
3.  **Capa de Presentación (UI):** Implementa el patrón **MVVM**. Los **ViewModels** exponen estados reactivos mediante `StateFlow` que la interfaz de usuario en **Compose** observa y "pinta" de manera automática.

---

## 3. Diagrama de Arquitectura y Modelo de Datos

### Estructura de Paquetes
```text
com.example.actividad_finalandroid/
├── data/               # Implementación de datos
│   ├── local/          # Room (Database, DAOs, Entities)
│   ├── remote/         # Configuración de Firebase
│   └── repository/     # Implementaciones de repositorios
├── domain/             # Lógica de negocio (Capa Central)
│   ├── model/          # Entidades de dominio (Task)
│   ├── repository/     # Interfaces de contratos
│   └── usecase/        # Acciones de negocio (Auth, Task, Draft)
├── ui/                 # Interfaz de Usuario
│   ├── screen/         # Pantallas (Login, Register, Task, Drafts)
│   ├── state/          # UI States y ViewModels
│   └── theme/          # Estilos de Material 3
├── navigation/         # Grafo de navegación y rutas seguras
└── MainActivity.kt     # Punto de entrada de la aplicación
```

### Modelo de Datos e Integridad
Para garantizar la privacidad y el aislamiento, tanto el modelo de **Firestore** (`Task`) como el de **Room** (`TaskDraftEntity`) incluyen el campo **`ownerId`**.
*   **Aislamiento:** Un usuario solo puede ver, editar o eliminar las tareas cuyo `ownerId` coincida con su UID de Firebase Authentication.
*   **Publicación Segura:** El `PublishDraftUseCase` asegura que un borrador solo se elimine del teléfono local tras recibir una confirmación de éxito de Firestore en la nube.

---

## 4. Instrucciones de Configuración y Ejecución

### Requisitos Previos
*   Android Studio Ladybug (o superior).
*   Un dispositivo físico o emulador con **Google Play Store** (API 26+).

### Pasos para Ejecución
1.  **Clonar el Repositorio:**
    ```bash
    git clone https://github.com/vergaramarinjosedavid-ui/Actividad_FinalAndroid.git
    ```
2.  **Vincular Firebase:**
    *   Crea un proyecto en [Firebase Console](https://console.firebase.google.com/).
    *   Registra la app con el ID: `com.example.actividad_finalandroid`.
    *   Descarga el archivo `google-services.json` y colócalo en la carpeta `app/` del proyecto.
3.  **Habilitar Servicios:**
    *   **Authentication:** Activa el método "Correo electrónico/Contraseña".
    *   **Firestore Database:** Crea la base de datos en "Modo de Prueba".
4.  **Compilar y Ejecutar:** Presiona el botón **Run** en Android Studio.

---

## 5. Evidencias y Casos de Prueba Funcionales

Se han ejecutado y superado satisfactoriamente los siguientes 10 casos de prueba:

| ID | Caso de Prueba | Descripción | Resultado |
|:---:|:---|:---|:---:|
| 01 | Registro de Usuario | Validación de campos, formato email y contraseña >= 6 caracteres. | ✅ EXITOSO |
| 02 | Inicio de Sesión | Autenticación correcta contra los servidores de Firebase. | ✅ EXITOSO |
| 03 | Redirección Automática | La app detecta la sesión activa y entra directo al Home. | ✅ EXITOSO |
| 04 | CRUD: Crear Tarea | Guardado exitoso de nueva tarea en Cloud Firestore. | ✅ EXITOSO |
| 05 | CRUD: Leer Tiempo Real | Actualización automática de la lista al cambiar datos en la nube. | ✅ EXITOSO |
| 06 | CRUD: Actualizar | Cambio de estado de completado y refresco de `updatedAt`. | ✅ EXITOSO |
| 07 | CRUD: Eliminar | Eliminación del documento en Firestore desde la UI. | ✅ EXITOSO |
| 08 | Aislamiento de Datos | El usuario A no puede ver las tareas del usuario B (Filtro por `ownerId`). | ✅ EXITOSO |
| 09 | Persistencia de Borradores | Guardado de tareas en Room para modo offline. | ✅ EXITOSO |
| 10 | Publicación Segura | El borrador solo se borra de Room tras éxito en la nube. | ✅ EXITOSO |

### Galería de Evidencias
*(Inserta aquí tus capturas de pantalla)*
> [!TIP]
> **Captura 1:** Pantalla de Registro con error de validación.  
> **Captura 2:** Listado de tareas cargadas desde Firestore.  
> **Captura 3:** Transacción exitosa de publicación de borrador.

---
© 2026 TaskManager Project - Desarrollado para el Taller Final de Android Avanzado.

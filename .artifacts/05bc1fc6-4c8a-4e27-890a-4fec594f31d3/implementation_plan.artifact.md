# Implementation Plan - Login MVVM and Navigation Architecture

This plan covers adding the Login architecture components (State, ViewModel, Screen), setting up Compose Navigation dependencies, and creating a secure navigation graph that automatically redirects authenticated users and cleans the navigation stack appropriately.

## User Review Required

> [!IMPORTANT]
> - Adding Navigation Compose requires modifying `libs.versions.toml` and `app/build.gradle.kts` to add `androidx.navigation:navigation-compose:2.10.1`.
> - A placeholder `HomeScreen` will be added under `ui/screen/` to act as the destination upon successful login or automatic redirection, complete with a sign-out button to demonstrate backstack clearing.

## Proposed Changes

### Build Configuration

#### [MODIFY] [libs.versions.toml](file:///C:/Users/USUARIO/AndroidStudioProjects/Actividad_FinalAndroid/gradle/libs.versions.toml)
- Add version reference: `navigationCompose = "2.10.1"`
- Add library reference: `androidx-navigation-compose = { group = "androidx.navigation", name = "navigation-compose", version.ref = "navigationCompose" }`

#### [MODIFY] [build.gradle.kts](file:///C:/Users/USUARIO/AndroidStudioProjects/Actividad_FinalAndroid/app/build.gradle.kts)
- Add `implementation(libs.androidx.navigation.compose)` to the dependencies block.

### Presentation & State Layer

#### [NEW] [LoginState.kt](file:///C:/Users/USUARIO/AndroidStudioProjects/Actividad_FinalAndroid/app/src/main/java/com/example/actividad_finalandroid/ui/state/LoginState.kt)
- Define `LoginUiState` sealed interface (`Idle`, `Loading`, `Success`, `Error`).

#### [NEW] [LoginViewModel.kt](file:///C:/Users/USUARIO/AndroidStudioProjects/Actividad_FinalAndroid/app/src/main/java/com/example/actividad_finalandroid/ui/state/LoginViewModel.kt)
- Manage user email/password login fields.
- Validate empty credentials and invoke `LoginUserUseCase`.
- Provide checking functionality via `GetCurrentUserUseCase` to determine if a user is already signed in.

#### [NEW] [LoginScreen.kt](file:///C:/Users/USUARIO/AndroidStudioProjects/Actividad_FinalAndroid/app/src/main/java/com/example/actividad_finalandroid/ui/screen/LoginScreen.kt)
- Create Login UI layout with Material 3 text fields, loading indicators, and explicit navigation links to navigate to the sign-up screen.

#### [NEW] [HomeScreen.kt](file:///C:/Users/USUARIO/AndroidStudioProjects/Actividad_FinalAndroid/app/src/main/java/com/example/actividad_finalandroid/ui/screen/HomeScreen.kt)
- A base authenticated screen with a logout option invoking `LogoutUserUseCase`.

### Navigation Infrastructure

#### [NEW] [Screen.kt](file:///C:/Users/USUARIO/AndroidStudioProjects/Actividad_FinalAndroid/app/src/main/java/com/example/actividad_finalandroid/navigation/Screen.kt)
- Define navigation routes: `login`, `register`, `home`.

#### [NEW] [NavGraph.kt](file:///C:/Users/USUARIO/AndroidStudioProjects/Actividad_FinalAndroid/app/src/main/java/com/example/actividad_finalandroid/navigation/NavGraph.kt)
- Main `NavHost` element.
- Performs initial session checks to automatically route to `home` if a valid UID exists.
- Cleans up backstacks using `popUpTo` options on auth state changes.

#### [MODIFY] [MainActivity.kt](file:///C:/Users/USUARIO/AndroidStudioProjects/Actividad_FinalAndroid/app/src/main/java/com/example/actividad_finalandroid/MainActivity.kt)
- Instantiate required dependencies manually or provide a clean entrance using the newly configured `NavGraph`.

## Verification Plan

### Automated Verification
- Run `app:assembleDebug` to confirm build completion.

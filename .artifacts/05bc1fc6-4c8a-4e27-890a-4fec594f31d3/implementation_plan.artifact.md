# Implementation Plan - Stabilize Build and Fix App Crash

The application is currently failing to build due to a KSP internal error (`unexpected jvm signature V`) and previously crashed due to Room implementation missing. I will switch to `kapt` (the traditional annotation processor) and disable the "built-in Kotlin" feature that conflicts with it, ensuring a stable and working build.

## User Review Required

> [!IMPORTANT]
> I am switching from `KSP` to `KAPT` for Room database processing. While `KSP` is newer, it is currently having compatibility issues with your environment's Kotlin version. `KAPT` is a mature and stable alternative that will fix the "AppDatabase_Impl does not exist" error.

## Proposed Changes

### Build Configuration

#### [MODIFY] [libs.versions.toml](file:///C:/Users/USUARIO/AndroidStudioProjects/Actividad_FinalAndroid/gradle/libs.versions.toml)
- Set `kotlin = "1.9.24"`.
- Remove `ksp` versions and plugins.
- Ensure `room = "2.6.1"`.

#### [MODIFY] [gradle.properties](file:///C:/Users/USUARIO/AndroidStudioProjects/Actividad_FinalAndroid/gradle.properties)
- Add `android.builtInKotlin=false` to allow using `kapt`.
- Ensure `android.disallowKotlinSourceSets=false`.

#### [MODIFY] [build.gradle.kts (root)](file:///C:/Users/USUARIO/AndroidStudioProjects/Actividad_FinalAndroid/build.gradle.kts)
- Use standard `alias(libs.plugins.kotlin.android) apply false` instead of compose plugin at root if needed, or keep it if versioned correctly.

#### [MODIFY] [app/build.gradle.kts](file:///C:/Users/USUARIO/AndroidStudioProjects/Actividad_FinalAndroid/app/build.gradle.kts)
- Apply `id("org.jetbrains.kotlin.kapt")`.
- Update `compileSdk` to `35` and `targetSdk` to `34` for better compatibility with current stable libraries.
- Replace `ksp(libs.androidx.room.compiler)` with `kapt(libs.androidx.room.compiler)`.

## Verification Plan

### Automated Verification
- Run `gradlew clean app:assembleDebug`.
- Verify the build finishes successfully.

### Manual Verification
- Deploy the app to the emulator.
- Confirm the app opens to the Login/Register screen without the "keeps stopping" error.

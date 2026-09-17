# Implementation Plan - Authentication Use Cases

This plan details the creation of four independent authentication use cases under the `domain/usecase/auth/` package. Each use case will encapsulate a single business rule, injecting `AuthRepository` and providing an `operator fun invoke` for a clean architecture delivery.

## Proposed Changes

### Domain Layer - Use Cases

#### [NEW] [RegisterUserUseCase.kt](file:///C:/Users/USUARIO/AndroidStudioProjects/Actividad_FinalAndroid/app/src/main/java/com/example/actividad_finalandroid/domain/usecase/auth/RegisterUserUseCase.kt)
- Contains an `invoke(email: String, password: String): Result<Unit>` method calling `authRepository.signUp`.

#### [NEW] [LoginUserUseCase.kt](file:///C:/Users/USUARIO/AndroidStudioProjects/Actividad_FinalAndroid/app/src/main/java/com/example/actividad_finalandroid/domain/usecase/auth/LoginUserUseCase.kt)
- Contains an `invoke(email: String, password: String): Result<Unit>` method calling `authRepository.signIn`.

#### [NEW] [LogoutUserUseCase.kt](file:///C:/Users/USUARIO/AndroidStudioProjects/Actividad_FinalAndroid/app/src/main/java/com/example/actividad_finalandroid/domain/usecase/auth/LogoutUserUseCase.kt)
- Contains an `invoke(): Unit` method calling `authRepository.signOut`.

#### [NEW] [GetCurrentUserUseCase.kt](file:///C:/Users/USUARIO/AndroidStudioProjects/Actividad_FinalAndroid/app/src/main/java/com/example/actividad_finalandroid/domain/usecase/auth/GetCurrentUserUseCase.kt)
- Contains an `invoke(): String?` method calling `authRepository.getCurrentUserUid`.

## Verification Plan

### Manual Verification
- Code analysis and syntax checking via the IDE.

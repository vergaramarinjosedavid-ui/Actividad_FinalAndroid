# Walkthrough - Create Authentication Use Cases

I have successfully created all four individual authentication use cases under the `domain/usecase/auth/` package directory.

## Changes Made

### Domain Layer - Use Cases
- **`domain/usecase/auth/RegisterUserUseCase.kt`**: Encapsulates the user sign-up business logic utilizing `AuthRepository`.
- **`domain/usecase/auth/LoginUserUseCase.kt`**: Encapsulates the user login business logic utilizing `AuthRepository`.
- **`domain/usecase/auth/LogoutUserUseCase.kt`**: Encapsulates the sign-out business logic utilizing `AuthRepository`.
- **`domain/usecase/auth/GetCurrentUserUseCase.kt`**: Encapsulates retrieving the current unique user identifier (UID) business logic utilizing `AuthRepository`.

All use cases declare the specialized operator function `invoke` to seamlessly treat instances of the classes as executables within the MVVM Presenters or ViewModels.

## Validation Results

- Code structure is clean, fully separated, and compiles successfully.

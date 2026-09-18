package com.example.actividad_finalandroid.ui.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.actividad_finalandroid.domain.usecase.auth.LoginUserUseCase
import com.example.actividad_finalandroid.domain.usecase.auth.GetCurrentUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUserUseCase: LoginUserUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailChange(newValue: String) {
        email = newValue
    }

    fun onPasswordChange(newValue: String) {
        password = newValue
    }

    fun isUserLoggedIn(): Boolean {
        return getCurrentUserUseCase() != null
    }

    fun login() {
        if (email.isBlank() || password.isBlank()) {
            _uiState.value = LoginUiState.Error("El correo y la contraseña son obligatorios.")
            return
        }

        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            loginUserUseCase(email, password)
                .onSuccess {
                    _uiState.value = LoginUiState.Success
                }
                .onFailure { exception ->
                    _uiState.value = LoginUiState.Error(exception.localizedMessage ?: "Credenciales incorrectas u ocurrio un error.")
                }
        }
    }

    fun clearState() {
        _uiState.value = LoginUiState.Idle
    }
}

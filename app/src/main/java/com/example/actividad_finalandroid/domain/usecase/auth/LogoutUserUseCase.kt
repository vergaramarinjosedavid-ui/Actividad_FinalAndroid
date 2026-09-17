package com.example.actividad_finalandroid.domain.usecase.auth

import com.example.actividad_finalandroid.domain.repository.AuthRepository

class LogoutUserUseCase(
    private val authRepository: AuthRepository
) {
    operator fun invoke() {
        authRepository.signOut()
    }
}

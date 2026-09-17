package com.example.actividad_finalandroid.domain.usecase.auth

import com.example.actividad_finalandroid.domain.repository.AuthRepository

class RegisterUserUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<Unit> {
        return authRepository.signUp(email, password)
    }
}

package com.example.actividad_finalandroid.domain.usecase.auth

import com.example.actividad_finalandroid.domain.repository.AuthRepository

class GetCurrentUserUseCase(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): String? {
        return authRepository.getCurrentUserUid()
    }
}

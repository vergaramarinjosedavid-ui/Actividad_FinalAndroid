package com.example.actividad_finalandroid.domain.repository

interface AuthRepository {
    suspend fun signUp(email: String, password: String): Result<Unit>
    suspend fun signIn(email: String, password: String): Result<Unit>
    fun signOut()
    fun getCurrentUserUid(): String?
    fun isUserLoggedIn(): Boolean
}

package com.example.actividad_finalandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.actividad_finalandroid.data.repository.AuthRepositoryImpl
import com.example.actividad_finalandroid.domain.usecase.auth.GetCurrentUserUseCase
import com.example.actividad_finalandroid.domain.usecase.auth.LoginUserUseCase
import com.example.actividad_finalandroid.domain.usecase.auth.LogoutUserUseCase
import com.example.actividad_finalandroid.domain.usecase.auth.RegisterUserUseCase
import com.example.actividad_finalandroid.navigation.NavGraph
import com.example.actividad_finalandroid.ui.theme.Actividad_FinalAndroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Manual dependency injection for setup demonstration
        val authRepository = AuthRepositoryImpl()
        val registerUserUseCase = RegisterUserUseCase(authRepository)
        val loginUserUseCase = LoginUserUseCase(authRepository)
        val logoutUserUseCase = LogoutUserUseCase(authRepository)
        val getCurrentUserUseCase = GetCurrentUserUseCase(authRepository)

        enableEdgeToEdge()
        setContent {
            Actividad_FinalAndroidTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavGraph(
                        registerUserUseCase = registerUserUseCase,
                        loginUserUseCase = loginUserUseCase,
                        logoutUserUseCase = logoutUserUseCase,
                        getCurrentUserUseCase = getCurrentUserUseCase,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
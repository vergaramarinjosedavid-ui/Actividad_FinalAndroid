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
import com.example.actividad_finalandroid.data.repository.TaskRepositoryImpl
import com.example.actividad_finalandroid.domain.usecase.auth.GetCurrentUserUseCase
import com.example.actividad_finalandroid.domain.usecase.auth.LoginUserUseCase
import com.example.actividad_finalandroid.domain.usecase.auth.LogoutUserUseCase
import com.example.actividad_finalandroid.domain.usecase.auth.RegisterUserUseCase
import com.example.actividad_finalandroid.domain.usecase.task.CreateTaskUseCase
import com.example.actividad_finalandroid.domain.usecase.task.DeleteTaskUseCase
import com.example.actividad_finalandroid.domain.usecase.task.GetTasksUseCase
import com.example.actividad_finalandroid.domain.usecase.task.UpdateTaskUseCase
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

        val taskRepository = TaskRepositoryImpl()
        val createTaskUseCase = CreateTaskUseCase(taskRepository)
        val getTasksUseCase = GetTasksUseCase(taskRepository)
        val updateTaskUseCase = UpdateTaskUseCase(taskRepository)
        val deleteTaskUseCase = DeleteTaskUseCase(taskRepository)

        // Room instances & Draft usecases setup
        val db = androidx.room.Room.databaseBuilder(
            applicationContext,
            com.example.actividad_finalandroid.data.local.AppDatabase::class.java,
            "taskmanager_db"
        ).build()
        val draftDao = db.taskDraftDao()
        val draftRepository = com.example.actividad_finalandroid.data.repository.DraftRepositoryImpl(draftDao)
        
        val getDraftsUseCase = com.example.actividad_finalandroid.domain.usecase.draft.GetDraftsUseCase(draftRepository)
        val saveDraftUseCase = com.example.actividad_finalandroid.domain.usecase.draft.SaveDraftUseCase(draftRepository)
        val deleteDraftUseCase = com.example.actividad_finalandroid.domain.usecase.draft.DeleteDraftUseCase(draftRepository)
        val publishDraftUseCase = com.example.actividad_finalandroid.domain.usecase.draft.PublishDraftUseCase(draftRepository, taskRepository)

        enableEdgeToEdge()
        setContent {
            Actividad_FinalAndroidTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavGraph(
                        registerUserUseCase = registerUserUseCase,
                        loginUserUseCase = loginUserUseCase,
                        logoutUserUseCase = logoutUserUseCase,
                        getCurrentUserUseCase = getCurrentUserUseCase,
                        createTaskUseCase = createTaskUseCase,
                        getTasksUseCase = getTasksUseCase,
                        updateTaskUseCase = updateTaskUseCase,
                        deleteTaskUseCase = deleteTaskUseCase,
                        getDraftsUseCase = getDraftsUseCase,
                        saveDraftUseCase = saveDraftUseCase,
                        deleteDraftUseCase = deleteDraftUseCase,
                        publishDraftUseCase = publishDraftUseCase,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
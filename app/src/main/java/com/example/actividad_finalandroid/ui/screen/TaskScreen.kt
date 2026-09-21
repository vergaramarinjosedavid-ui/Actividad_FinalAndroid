package com.example.actividad_finalandroid.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.actividad_finalandroid.data.local.entity.TaskDraftEntity
import com.example.actividad_finalandroid.domain.model.Task
import com.example.actividad_finalandroid.ui.state.TaskUiState
import com.example.actividad_finalandroid.ui.state.TaskViewModel

@Composable
fun TaskScreen(
    viewModel: TaskViewModel,
    onLogoutClick: () -> Unit,
    onNavigateToDrafts: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.taskUiState.collectAsState()
    val pendingDrafts by viewModel.pendingDraftsState.collectAsState()
    val isSyncing by viewModel.isSyncing.collectAsState()
    val syncMessage by viewModel.syncMessage.collectAsState()

    var newTitle by remember { mutableStateOf("") }
    var newDescription by remember { mutableStateOf("") }
    var formError by remember { mutableStateOf<String?>(null) }

    var showDeleteDialog by remember { mutableStateOf(false) }
    var taskToDeleteId by remember { mutableStateOf<String?>(null) }

    var showEditDialog by remember { mutableStateOf(false) }
    var taskToEdit by remember { mutableStateOf<Task?>(null) }
    var editTitle by remember { mutableStateOf("") }
    var editDescription by remember { mutableStateOf("") }
    var editFormError by remember { mutableStateOf<String?>(null) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
                taskToDeleteId = null
            },
            title = { Text("Confirmar eliminación") },
            text = { Text("¿Estás seguro de que deseas eliminar esta tarea? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        taskToDeleteId?.let { viewModel.removeTask(it) }
                        showDeleteDialog = false
                        taskToDeleteId = null
                    }
                ) {
                    Text("Eliminar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        taskToDeleteId = null
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (showEditDialog && taskToEdit != null) {
        AlertDialog(
            onDismissRequest = {
                showEditDialog = false
                taskToEdit = null
                editFormError = null
            },
            title = { Text("Editar Tarea") },
            text = {
                Column {
                    if (editFormError != null) {
                        Text(
                            text = editFormError!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                    OutlinedTextField(
                        value = editTitle,
                        onValueChange = {
                            editTitle = it
                            editFormError = null
                        },
                        label = { Text("Título *") },
                        isError = editFormError != null && editTitle.isBlank(),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = editDescription,
                        onValueChange = {
                            editDescription = it
                            editFormError = null
                        },
                        label = { Text("Descripción *") },
                        isError = editFormError != null && editDescription.isBlank(),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        taskToEdit?.let { task ->
                            if (editTitle.isBlank() || editDescription.isBlank()) {
                                editFormError = "Debes completar tanto el título como la descripción."
                            } else {
                                viewModel.updateTaskDetails(task, editTitle, editDescription)
                                showEditDialog = false
                                taskToEdit = null
                                editFormError = null
                            }
                        }
                    }
                ) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showEditDialog = false
                        taskToEdit = null
                        editFormError = null
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Top Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Mis Tareas",
                style = MaterialTheme.typography.headlineLarge
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                TextButton(onClick = onNavigateToDrafts) {
                    Text("Borradores")
                }
                Spacer(modifier = Modifier.width(4.dp))
                TextButton(onClick = onLogoutClick) {
                    Text(
                        text = "Salir",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Sync Message Banner
        syncMessage?.let { message ->
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.weight(1f)
                    )
                    TextButton(onClick = { viewModel.clearSyncMessage() }) {
                        Text("OK")
                    }
                }
            }
        }

        // Create Task Section
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Nueva Tarea Local",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                if (formError != null) {
                    Text(
                        text = formError!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                OutlinedTextField(
                    value = newTitle,
                    onValueChange = {
                        newTitle = it
                        formError = null
                    },
                    label = { Text("Título *") },
                    isError = formError != null && newTitle.isBlank(),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = newDescription,
                    onValueChange = {
                        newDescription = it
                        formError = null
                    },
                    label = { Text("Descripción *") },
                    isError = formError != null && newDescription.isBlank(),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Ambos campos son obligatorios",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(1f)
                    )
                    Button(
                        onClick = {
                            if (newTitle.isBlank() || newDescription.isBlank()) {
                                formError = "Debes completar tanto el título como la descripción."
                            } else {
                                viewModel.addTask(newTitle, newDescription)
                                newTitle = ""
                                newDescription = ""
                                formError = null
                            }
                        }
                    ) {
                        Text("Guardar Local")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Prominent "Subir a Firebase" Button
        ElevatedButton(
            onClick = { viewModel.uploadAllToFirebase() },
            enabled = !isSyncing && pendingDrafts.isNotEmpty(),
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (isSyncing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Subiendo a Firebase...")
                } else {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Subir a Firebase",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        if (pendingDrafts.isEmpty()) "Sin tareas pendientes por subir a Firebase"
                        else "Subir a Firebase (${pendingDrafts.size} pendientes)"
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Lists section (Pending Local Drafts & Cloud Tasks)
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            // Section 1: Pending Local Tasks
            if (pendingDrafts.isNotEmpty()) {
                item {
                    Text(
                        text = "Pendientes por subir a Firebase (${pendingDrafts.size})",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
                items(pendingDrafts, key = { "draft_${it.id}" }) { draft ->
                    PendingTaskItem(
                        draft = draft,
                        onUploadClick = { viewModel.uploadSingleDraftToFirebase(draft) }
                    )
                }
            }

            // Section 2: Cloud Tasks (Firebase Firestore)
            item {
                Text(
                    text = "Tareas Sincronizadas en Firebase",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                )
            }

            when (val state = uiState) {
                is TaskUiState.Loading -> {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                }
                is TaskUiState.Empty -> {
                    item {
                        Text(
                            text = "No hay tareas sincronizadas en Firebase.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth().padding(16.dp)
                        )
                    }
                }
                is TaskUiState.Error -> {
                    item {
                        Text(
                            text = state.message,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth().padding(16.dp)
                        )
                    }
                }
                is TaskUiState.Success -> {
                    items(state.tasks, key = { it.id }) { task ->
                        TaskItem(
                            task = task,
                            onCheckedChange = { viewModel.toggleTaskCompletion(task) },
                            onEditClick = {
                                taskToEdit = task
                                editTitle = task.title
                                editDescription = task.description
                                editFormError = null
                                showEditDialog = true
                            },
                            onDeleteClick = {
                                taskToDeleteId = task.id
                                showDeleteDialog = true
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PendingTaskItem(
    draft: TaskDraftEntity,
    onUploadClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Badge(containerColor = MaterialTheme.colorScheme.tertiary) {
                        Text("Pendiente", color = MaterialTheme.colorScheme.onTertiary)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = draft.title,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                if (draft.description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = draft.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
            IconButton(onClick = onUploadClick) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Subir a Firebase",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun TaskItem(
    task: Task,
    onCheckedChange: (Boolean) -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Checkbox(
                    checked = task.completed,
                    onCheckedChange = onCheckedChange
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.titleMedium,
                        textDecoration = if (task.completed) TextDecoration.LineThrough else TextDecoration.None
                    )
                    if (task.description.isNotBlank()) {
                        Text(
                            text = task.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textDecoration = if (task.completed) TextDecoration.LineThrough else TextDecoration.None
                        )
                    }
                }
            }
            Row {
                IconButton(onClick = onEditClick) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar Tarea",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                IconButton(onClick = onDeleteClick) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar Tarea",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

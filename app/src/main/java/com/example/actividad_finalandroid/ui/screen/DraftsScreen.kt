package com.example.actividad_finalandroid.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.actividad_finalandroid.data.local.entity.TaskDraftEntity
import com.example.actividad_finalandroid.ui.state.DraftActionStatus
import com.example.actividad_finalandroid.ui.state.DraftViewModel

@Composable
fun DraftsScreen(
    viewModel: DraftViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val drafts by viewModel.draftsState.collectAsState()
    val actionStatus by viewModel.actionStatus.collectAsState()

    var draftTitle by remember { mutableStateOf("") }
    var draftDescription by remember { mutableStateOf("") }
    var draftFormError by remember { mutableStateOf<String?>(null) }

    var showEditDialog by remember { mutableStateOf(false) }
    var draftToEdit by remember { mutableStateOf<TaskDraftEntity?>(null) }
    var editTitle by remember { mutableStateOf("") }
    var editDescription by remember { mutableStateOf("") }
    var editDraftFormError by remember { mutableStateOf<String?>(null) }

    if (showEditDialog && draftToEdit != null) {
        AlertDialog(
            onDismissRequest = {
                showEditDialog = false
                draftToEdit = null
                editDraftFormError = null
            },
            title = { Text("Editar Borrador") },
            text = {
                Column {
                    if (editDraftFormError != null) {
                        Text(
                            text = editDraftFormError!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                    OutlinedTextField(
                        value = editTitle,
                        onValueChange = {
                            editTitle = it
                            editDraftFormError = null
                        },
                        label = { Text("Título de Borrador *") },
                        isError = editDraftFormError != null && editTitle.isBlank(),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = editDescription,
                        onValueChange = {
                            editDescription = it
                            editDraftFormError = null
                        },
                        label = { Text("Descripción de Borrador *") },
                        isError = editDraftFormError != null && editDescription.isBlank(),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        draftToEdit?.let { draft ->
                            if (editTitle.isBlank() || editDescription.isBlank()) {
                                editDraftFormError = "Debes completar tanto el título como la descripción."
                            } else {
                                viewModel.updateDraft(draft, editTitle, editDescription)
                                showEditDialog = false
                                draftToEdit = null
                                editDraftFormError = null
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
                        draftToEdit = null
                        editDraftFormError = null
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
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Volver"
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Borradores Offline",
                style = MaterialTheme.typography.headlineMedium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Create Local Draft Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Guardar Borrador Fuera de Línea",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                if (draftFormError != null) {
                    Text(
                        text = draftFormError!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                OutlinedTextField(
                    value = draftTitle,
                    onValueChange = {
                        draftTitle = it
                        draftFormError = null
                    },
                    label = { Text("Título de Borrador *") },
                    isError = draftFormError != null && draftTitle.isBlank(),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = draftDescription,
                    onValueChange = {
                        draftDescription = it
                        draftFormError = null
                    },
                    label = { Text("Descripción de Borrador *") },
                    isError = draftFormError != null && draftDescription.isBlank(),
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
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.weight(1f)
                    )
                    Button(
                        onClick = {
                            if (draftTitle.isBlank() || draftDescription.isBlank()) {
                                draftFormError = "Debes completar tanto el título como la descripción."
                            } else {
                                viewModel.saveNewDraft(draftTitle, draftDescription)
                                draftTitle = ""
                                draftDescription = ""
                                draftFormError = null
                            }
                        }
                    ) {
                        Text("Guardar Local")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Actions status banners
        when (val status = actionStatus) {
            is DraftActionStatus.Loading -> {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(modifier = Modifier.size(32.dp))
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
            is DraftActionStatus.Error -> {
                Text(
                    text = status.message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            is DraftActionStatus.Success -> {
                Text(
                    text = "Operación realizada con éxito.",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            else -> {}
        }

        if (drafts.isNotEmpty()) {
            ElevatedButton(
                onClick = { viewModel.publishAllDrafts() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Subir Todo a Firebase"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Subir Todo a Firebase (${drafts.size})")
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        // List
        if (drafts.isEmpty()) {
            Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                Text(
                    text = "No tienes borradores locales guardados.",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth().weight(1f)
            ) {
                items(drafts, key = { it.id }) { draft ->
                    DraftItem(
                        draft = draft,
                        onEditClick = {
                            draftToEdit = draft
                            editTitle = draft.title
                            editDescription = draft.description
                            editDraftFormError = null
                            showEditDialog = true
                        },
                        onPublishClick = { viewModel.publish(draft) },
                        onDeleteClick = { viewModel.removeDraft(draft.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun DraftItem(
    draft: TaskDraftEntity,
    onEditClick: () -> Unit,
    onPublishClick: () -> Unit,
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
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = draft.title,
                    style = MaterialTheme.typography.titleMedium
                )
                if (draft.description.isNotBlank()) {
                    Text(
                        text = draft.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Row {
                IconButton(onClick = onEditClick) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar Borrador",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                IconButton(onClick = onPublishClick) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Publicar a la nube",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
                IconButton(onClick = onDeleteClick) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar Borrador",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

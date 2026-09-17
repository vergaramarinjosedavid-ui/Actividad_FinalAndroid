# Implementation Plan - Domain Repository Interfaces

This plan details the creation of the two domain repository interfaces: `TaskRepository` and `DraftRepository`, which define the business contract for task CRUD actions and draft handling, utilizing Kotlin Coroutines Flow.

## Proposed Changes

### Domain Layer - Repositories

#### [NEW] [TaskRepository.kt](file:///C:/Users/USUARIO/AndroidStudioProjects/Actividad_FinalAndroid/app/src/main/java/com/example/actividad_finalandroid/domain/repository/TaskRepository.kt)
Define an interface for Task operations:
- `getTasks(ownerId: String): Flow<List<Task>>`
- `getTaskById(id: String): Flow<Task?>`
- `insertTask(task: Task): Result<Unit>`
- `updateTask(task: Task): Result<Unit>`
- `deleteTask(id: String): Result<Unit>`

#### [NEW] [DraftRepository.kt](file:///C:/Users/USUARIO/AndroidStudioProjects/Actividad_FinalAndroid/app/src/main/java/com/example/actividad_finalandroid/domain/repository/DraftRepository.kt)
Define an interface for local draft operations:
- `getDrafts(ownerId: String): Flow<List<TaskDraftEntity>>`
- `saveDraft(draft: TaskDraftEntity): Long`
- `deleteDraft(id: Int): Unit`

## Verification Plan

### Automated Tests
- Build and verify compilation via `app:assembleDebug`.

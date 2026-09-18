# Implementation Plan - Task CRUD Operations (Repository & Use Cases)

This plan outlines the implementation of the Firestore-backed `TaskRepositoryImpl` and the four individual domain use cases to handle task business logic isolated by `ownerId`.

## Proposed Changes

### Data Layer - Repositories

#### [NEW] [TaskRepositoryImpl.kt](file:///C:/Users/USUARIO/AndroidStudioProjects/Actividad_FinalAndroid/app/src/main/java/com/example/actividad_finalandroid/data/repository/TaskRepositoryImpl.kt)
Implement the `TaskRepository` interface using Firestore:
- `getTasks(ownerId: String)`: `callbackFlow` snapshot listener on the `"tasks"` collection where `ownerId == ownerId`.
- `getTaskById(id: String)`: `callbackFlow` snapshot listener on a single task document.
- `insertTask(task: Task)`: Generates a document ID if blank, sets `createdAt` and `updatedAt`, and writes via `.set().await()`.
- `updateTask(task: Task)`: Updates fields and explicitly updates `updatedAt` to the current system time before writing.
- `deleteTask(id: String)`: Removes the document via `.delete().await()`.

### Domain Layer - Use Cases

#### [NEW] [CreateTaskUseCase.kt](file:///C:/Users/USUARIO/AndroidStudioProjects/Actividad_FinalAndroid/app/src/main/java/com/example/actividad_finalandroid/domain/usecase/task/CreateTaskUseCase.kt)
- `suspend operator fun invoke(task: Task): Result<Unit>`: Injects `TaskRepository` and invokes `insertTask(task)`.

#### [NEW] [GetTasksUseCase.kt](file:///C:/Users/USUARIO/AndroidStudioProjects/Actividad_FinalAndroid/app/src/main/java/com/example/actividad_finalandroid/domain/usecase/task/GetTasksUseCase.kt)
- `operator fun invoke(ownerId: String): Flow<List<Task>>`: Injects `TaskRepository` and invokes `getTasks(ownerId)`.

#### [NEW] [UpdateTaskUseCase.kt](file:///C:/Users/USUARIO/AndroidStudioProjects/Actividad_FinalAndroid/app/src/main/java/com/example/actividad_finalandroid/domain/usecase/task/UpdateTaskUseCase.kt)
- `suspend operator fun invoke(task: Task): Result<Unit>`: Injects `TaskRepository` and invokes `updateTask(task)`.

#### [NEW] [DeleteTaskUseCase.kt](file:///C:/Users/USUARIO/AndroidStudioProjects/Actividad_FinalAndroid/app/src/main/java/com/example/actividad_finalandroid/domain/usecase/task/DeleteTaskUseCase.kt)
- `suspend operator fun invoke(id: String): Result<Unit>`: Injects `TaskRepository` and invokes `deleteTask(id)`.

## Verification Plan

### Automated Verification
- Compile project with `app:assembleDebug` to verify no compilation errors.

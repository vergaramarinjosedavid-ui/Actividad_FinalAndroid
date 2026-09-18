# Implementation Plan - Task UI Architecture (MVVM & Compose)

This plan details creating the Task Presentation layer using Jetpack Compose and Material 3, wired to a dedicated `TaskViewModel`.

## User Review Required

> [!NOTE]
> I will hook this new `TaskScreen` inside our existing `NavGraph` replacing the simple placeholder text currently inside the `home` composable block, ensuring a cohesive user flow.

## Proposed Changes

### Presentation & State Layer

#### [NEW] [TaskState.kt](file:///C:/Users/USUARIO/AndroidStudioProjects/Actividad_FinalAndroid/app/src/main/java/com/example/actividad_finalandroid/ui/state/TaskState.kt)
Define the screen's interactive UI state:
- `sealed interface TaskUiState` modeling states: `Loading`, `Empty`, `Success(val tasks: List<Task>)`, and `Error(val message: String)`.

#### [NEW] [TaskViewModel.kt](file:///C:/Users/USUARIO/AndroidStudioProjects/Actividad_FinalAndroid/app/src/main/java/com/example/actividad_finalandroid/ui/state/TaskViewModel.kt)
Create the business presenter component:
- Dynamically queries `ownerId` upon instantiation via `GetCurrentUserUseCase`.
- Collects the reactive Firestore real-time stream using `GetTasksUseCase`.
- Exposes `taskUiState: StateFlow<TaskUiState>` via `.stateIn()`.
- Provides explicit trigger functions: `addTask(title: String, description: String)`, `toggleTaskCompletion(task: Task)`, and `removeTask(id: String)`.

#### [NEW] [TaskScreen.kt](file:///C:/Users/USUARIO/AndroidStudioProjects/Actividad_FinalAndroid/app/src/main/java/com/example/actividad_finalandroid/ui/screen/TaskScreen.kt)
Create the fully-featured Material 3 view screen:
- Incorporates a bottom sheet or localized input dialog to construct new tasks.
- Employs a structured `LazyColumn` listing active items as customizable Material 3 cards.
- Provides tactile icons or buttons to swipe/click to delete, or toggle check states.
- Shows descriptive illustrations or text fields for empty/loading/error indicators.

#### [MODIFY] [NavGraph.kt](file:///C:/Users/USUARIO/AndroidStudioProjects/Actividad_FinalAndroid/app/src/main/java/com/example/actividad_finalandroid/navigation/NavGraph.kt)
- Update the `Screen.Home.route` composable destination to instantiate `TaskViewModel` and display the `TaskScreen` instead of the primitive placeholder.

## Verification Plan

### Automated Verification
- Execute `app:assembleDebug` to confirm build completion.

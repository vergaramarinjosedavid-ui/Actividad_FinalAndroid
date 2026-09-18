# Implementation Plan - Draft Repository and Publish Logic

This plan outlines creating the draft repository implementation using Room and the four requested use cases, highlighting the secure transaction logic inside `PublishDraftUseCase`.

## Proposed Changes

### Data Layer - Repositories

#### [NEW] [DraftRepositoryImpl.kt](file:///C:/Users/USUARIO/AndroidStudioProjects/Actividad_FinalAndroid/app/src/main/java/com/example/actividad_finalandroid/data/repository/DraftRepositoryImpl.kt)
Implement the `DraftRepository` interface interacting with `TaskDraftDao`:
- `getDrafts(ownerId: String)`: Calls `taskDraftDao.getDrafts(ownerId)`.
- `saveDraft(draft: TaskDraftEntity)`: Calls `taskDraftDao.insertDraft(draft)`.
- `deleteDraft(id: Int)`: Calls `taskDraftDao.deleteDraftById(id)`.

### Domain Layer - Use Cases

#### [NEW] [GetDraftsUseCase.kt](file:///C:/Users/USUARIO/AndroidStudioProjects/Actividad_FinalAndroid/app/src/main/java/com/example/actividad_finalandroid/domain/usecase/draft/GetDraftsUseCase.kt)
- Exposes `operator fun invoke(ownerId: String): Flow<List<TaskDraftEntity>>`.

#### [NEW] [SaveDraftUseCase.kt](file:///C:/Users/USUARIO/AndroidStudioProjects/Actividad_FinalAndroid/app/src/main/java/com/example/actividad_finalandroid/domain/usecase/draft/SaveDraftUseCase.kt)
- Exposes `suspend operator fun invoke(draft: TaskDraftEntity): Long`.

#### [NEW] [DeleteDraftUseCase.kt](file:///C:/Users/USUARIO/AndroidStudioProjects/Actividad_FinalAndroid/app/src/main/java/com/example/actividad_finalandroid/domain/usecase/draft/DeleteDraftUseCase.kt)
- Exposes `suspend operator fun invoke(id: Int)`.

#### [NEW] [PublishDraftUseCase.kt](file:///C:/Users/USUARIO/AndroidStudioProjects/Actividad_FinalAndroid/app/src/main/java/com/example/actividad_finalandroid/domain/usecase/draft/PublishDraftUseCase.kt)
- Injects both `DraftRepository` and `TaskRepository`.
- Maps a `TaskDraftEntity` into a Firestore `Task` instance.
- Calls `taskRepository.insertTask(task)`.
- If successful (`onSuccess`), deletes the local draft from Room via `draftRepository.deleteDraft(draft.id)`.
- If an error occurs, it leaves the local draft intact and returns the failure state.

## Verification Plan

### Automated Verification
- Run `app:assembleDebug` to verify compilation.

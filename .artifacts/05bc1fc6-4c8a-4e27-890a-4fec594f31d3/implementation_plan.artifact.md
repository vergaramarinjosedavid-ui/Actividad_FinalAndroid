# Implementation Plan - Room Database & DAO Infrastructure

This plan outlines setting up the local persistence infrastructure using Room, including the DAO contract for managing task drafts isolated by user and the main Database class definition.

## Proposed Changes

### Local Data Layer

#### [NEW] [TaskDraftDao.kt](file:///C:/Users/USUARIO/AndroidStudioProjects/Actividad_FinalAndroid/app/src/main/java/com/example/actividad_finalandroid/data/local/TaskDraftDao.kt)
Create a Room DAO interface:
- `@Query("SELECT * FROM task_drafts WHERE ownerId = :ownerId ORDER BY savedAt DESC")` to retrieve drafts as a `Flow<List<TaskDraftEntity>>`.
- `@Insert(onConflict = OnConflictStrategy.REPLACE)` to save drafts.
- `@Query("DELETE FROM task_drafts WHERE id = :id")` or `@Delete` to remove drafts.

#### [NEW] [AppDatabase.kt](file:///C:/Users/USUARIO/AndroidStudioProjects/Actividad_FinalAndroid/app/src/main/java/com/example/actividad_finalandroid/data/local/AppDatabase.kt)
Create the main abstract class extending `RoomDatabase`:
- Annotated with `@Database(entities = [TaskDraftEntity::class], version = 1, exportSchema = false)`.
- Abstract method providing access to the `TaskDraftDao`.

## Verification Plan

### Automated Verification
- Run `app:assembleDebug` to verify that Room annotations are correctly processed and compile perfectly.

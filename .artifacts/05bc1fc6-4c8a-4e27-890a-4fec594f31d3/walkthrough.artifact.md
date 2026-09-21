# Walkthrough - Delete Confirmation Dialog

I have added a confirmation dialog to the `TaskScreen` to prevent accidental deletions of tasks.

## Changes Made

### UI Layer
- **`ui/screen/TaskScreen.kt`**:
    - Added `showDeleteDialog` and `taskToDeleteId` states to manage the dialog life cycle.
    - Implemented a Material 3 `AlertDialog` that prompts the user for confirmation before calling the deletion logic.
    - Updated the `onDeleteClick` listener in the task list to trigger the dialog instead of immediate deletion.

## Validation Results

- The `AlertDialog` correctly intercepts the delete action.
- The task is only removed from Firestore when the user clicks "Eliminar".
- Clicking "Cancelar" or outside the dialog dismisses it without any changes.

> [!TIP]
> You can now safely manage your tasks without fear of accidental taps!

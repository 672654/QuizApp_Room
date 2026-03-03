# QuizApp Room

App is using Room for local storage, ViewModels for state handling, and a ContentProvider for sharing data.

## ContentProvider
### Implementasjon
*   **Authority:** `com.example.quizapp_room.data.QuizContentProvider`
*   **Columns:** `name` (correct_answer) og `URI` (image-URI or resource-URI).
*   **Logic:** Provider get data from Room-database via `Cursor`-query in `QuizDao`.

### Testing with ADB
ContentProvider was tested with this command. 
Note: if adb is not in PATH, you need to provide the path to the adb manually.

```bash
adb shell content query --uri content://com.example.quizapp_room.data.QuizContentProvider
```

**result**

```text
Row: 0 name=Panda, URI=android.resource://com.example.quizapp_room/2130968601
Row: 1 name=Lion, URI=android.resource://com.example.quizapp_room/2130968587
Row: 2 name=Elephant, URI=android.resource://com.example.quizapp_room/2130968578
Row: 3 name=Sky, URI=content://media/picker/0/com.android.providers.media.photopicker/media/2757
```

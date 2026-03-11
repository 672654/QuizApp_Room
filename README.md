# QuizApp Room

QuizApp Room is an Android application designed to test your knowledge of images and names. The app allows users to participate in a quiz, manage a gallery of images (including adding and deleting items), and persists data locally.
See docs/LEARNING_GUIDE.md for info on concepts in andorid dev.

## Features
*   **Quiz Mode:** Test yourself by matching images with the correct names.
*   **Gallery Management:** Add your own images from your device or delete existing entries.
*   **Data Persistence:** All data is stored locally using the Room database.
*   **External Access:** Shares quiz data with other apps via a ContentProvider.

## Architecture
The app follows the **MVVM (Model-View-ViewModel)** architectural pattern to ensure a clean separation of concerns:

*   **Model:** Uses **Room** for local data storage. It includes Entities, DAOs (Data Access Objects), and a Repository to manage data operations.
*   **View:** Built with **Jetpack Compose**, providing a modern and reactive user interface.
*   **ViewModel:** Handles UI state and business logic. It communicates with the Repository and exposes data to the UI using **Kotlin Flows** and `State` objects.
*   **Coroutines & Flow:** Asynchronous operations (like database queries) are handled using `viewModelScope` and `Flow` for real-time UI updates.

## Project Structure
The code is organized into logical packages:

*   **`data/`**: Contains the core data layer.
    *   `QuizItem.kt`: The data model (Entity).
    *   `QuizDao.kt`: Database access methods.
    *   `QuizDatabase.kt`: The Room database configuration.
    *   `QuizRepository.kt`: Orchestrates data flow between the DAO and the ViewModel.
    *   `QuizContentProvider.kt`: Exposes data to external applications.
*   **`ui/views/`**: Contains the UI components and ViewModels.
    *   `quiz/`: Screens and ViewModel for the quiz logic.
    *   `gallery/`: Screens and ViewModel for managing the image collection.
*   **`utils/`**: Helper classes and utility functions (e.g., URI handling).

---

## ContentProvider
### Implementation
*   **Authority:** `com.example.quizapp_room.data.QuizContentProvider`
*   **Columns:** `name` (correct_answer) and `URI` (image-URI or resource-URI).
*   **Logic:** The provider retrieves data from the Room database via a `Cursor` query defined in `QuizDao`.

### Testing with ADB
The ContentProvider can be tested using the following command. 
Note: If `adb` is not in your system PATH, you need to provide the full path to the executable.

```bash
adb shell content query --uri content://com.example.quizapp_room.data.QuizContentProvider
```

**Example Output:**

```text
Row: 0 name=Panda, URI=android.resource://com.example.quizapp_room/2130968601
Row: 1 name=Lion, URI=android.resource://com.example.quizapp_room/2130968587
Row: 2 name=Elephant, URI=android.resource://com.example.quizapp_room/2130968578
Row: 3 name=Sky, URI=content://media/picker/0/com.android.providers.media.photopicker/media/2757
```

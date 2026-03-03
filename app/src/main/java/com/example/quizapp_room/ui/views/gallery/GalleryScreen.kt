package com.example.quizapp_room.ui.views.gallery


import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.quizapp_room.data.QuizItem
import com.example.quizapp_room.ui.components.GalleryCard
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.ActivityNavigatorExtras
import com.example.quizapp_room.utils.takePersistableUriPermission

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryScreen(
    navController: NavController,
    quizItems: List<QuizItem>,
    onAddButtonClick: (QuizItem) -> Unit,
    onSortButtonClick: (Boolean) -> Unit,
    onDeleteButtonClick: (QuizItem) -> Unit
){
    val context = LocalContext.current

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var showNameDialog by remember { mutableStateOf(false) }
    var quizItemName by remember { mutableStateOf("") }

    var sortAscending by remember { mutableStateOf(true) }


    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) {uri ->
        if (uri != null) {
            selectedImageUri = uri
            showNameDialog = true
        }
    }


    Scaffold(
        topBar ={
            TopAppBar(
                title = { Text(text = "Gallery") },
                actions = {
                    TextButton(
                        onClick = { onSortButtonClick(sortAscending)
                            sortAscending = !sortAscending
                        }
                    ){
                        Text( if (sortAscending) "Sort A-Z" else "Sort Z-A")
                    }
                }
            )
        },
        floatingActionButton ={
            FloatingActionButton(
                onClick = {
                    pickImageLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )}
            ){
                Text(text = "ADD")
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ){
    paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues),
            contentPadding = paddingValues
        ) {
            items(quizItems) { quizItem ->
                GalleryCard(
                    quizItem = quizItem,
                    onDeleteButtonClick = onDeleteButtonClick
                )
            }

        }
    }

    if (showNameDialog) {
        AlertDialog(
            onDismissRequest = {
                showNameDialog = false
                selectedImageUri = null
            },
            title = {
                Text(text = "Add Quiz Item")
            },
            text = {
                TextField(
                    value = quizItemName,
                    onValueChange = { quizItemName = it },
                    singleLine = true
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        selectedImageUri?.let { uri ->

                            //Use UriUtil function to give permission.
                            context.takePersistableUriPermission(uri)

                            val newItem = QuizItem(
                                id = 0,
                                answer = quizItemName,
                                imageUri = uri.toString(),
                                imageRes = null
                            )
                            onAddButtonClick(newItem)
                        }
                        quizItemName = ""
                        showNameDialog = false
                        selectedImageUri = null
                    }
                ) {
                    Text("ADD")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        quizItemName = ""
                        showNameDialog = false
                        selectedImageUri = null
                    }
                ) {
                    Text("CANCEL")
                }
            }

        )
    }

}
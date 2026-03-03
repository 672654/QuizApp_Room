package com.example.quizapp_room.ui.views.gallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.quizapp_room.data.QuizItem
import com.example.quizapp_room.data.QuizRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class GalleryViewModel(private val repository: QuizRepository) : ViewModel(){

    private val isAscending = MutableStateFlow(true)


    val allItems: StateFlow<List<QuizItem>> = repository.getAllQuizItems()
        .combine(
            isAscending
        ) { items, ascending ->
            if (ascending) {
                items.sortedBy { it.answer }
            } else {
                items.sortedByDescending { it.answer }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    fun addQuizItem(item: QuizItem) {
        viewModelScope.launch {
            repository.insertQuizItem(item)
        }
    }

    fun deleteQuizItem(id: Int){
        viewModelScope.launch {
            repository.deleteQuizItem(id)
        }
    }

    fun sortAscending(ascending: Boolean){
        if (ascending){
            isAscending.value = true
        }else{
            isAscending.value = false
        }
    }




}







// Factory for å lage ViewModel-en, siden den har en dependency (repository)
class GalleryViewModelFactory(private val repository: QuizRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GalleryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GalleryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
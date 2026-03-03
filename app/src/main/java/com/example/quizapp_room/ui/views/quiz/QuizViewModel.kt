package com.example.quizapp_room.ui.views.quiz

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.quizapp_room.data.QuizItem
import com.example.quizapp_room.data.QuizRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class QuizViewModel(private val repository: QuizRepository) : ViewModel() {

    private val allQuizItems = mutableStateListOf<QuizItem>()
    private val remainingQuizItems = mutableStateListOf<QuizItem>()

    var notEnoughQuizItems by mutableStateOf(false)

    var isAnswered by mutableStateOf(false)

    var quizAnswers = mutableStateListOf<String>()

    private val _currentQuizItem = MutableStateFlow<QuizItem?>(null)
    val currentQuizItem: StateFlow<QuizItem?> = _currentQuizItem.asStateFlow()

    var score by mutableStateOf(0)



    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            val items = repository.getAllQuizItems().first()
            if (items.count() < 3) {
                notEnoughQuizItems = true
            }
            allQuizItems.clear()
            allQuizItems.addAll(items)
            startNewQuiz()
        }
    }

    fun startNewQuiz() {
        notEnoughQuizItems = false
        remainingQuizItems.clear()
        remainingQuizItems.addAll(allQuizItems)
        score = 0
        quizAnswers.clear()
        isAnswered = false
        loadNextQuestion()
        Log.d("QuizViewModel", "Starting new quiz.")
    }

    fun loadNextQuestion() {
        if (remainingQuizItems.isEmpty()) {
            Log.d("QuizViewModel", "No more questions to load.")
            return
        }
        val nextQuestion = remainingQuizItems.random()
        remainingQuizItems.remove(nextQuestion)

        _currentQuizItem.value = nextQuestion
        isAnswered = false

        generateQuizAnswers(nextQuestion)


    }

    fun generateQuizAnswers(correctQuizItem: QuizItem) {
        val wrongAnswers = allQuizItems
            .map { it.answer }
            .filter { it != correctQuizItem.answer }
            .shuffled()
            .distinct()
            .take(2)

        quizAnswers.clear()
        quizAnswers.add(correctQuizItem.answer)
        quizAnswers.addAll(wrongAnswers)
        quizAnswers.shuffle()
    }

    fun checkAnswer(answer: String) {
        isAnswered = true

        if (answer == currentQuizItem.value?.answer){
            score++
        }


    }
}

class QuizViewModelFactory(private val repository: QuizRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuizViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return QuizViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

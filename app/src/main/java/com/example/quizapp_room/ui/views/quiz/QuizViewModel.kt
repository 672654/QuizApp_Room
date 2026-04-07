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
import com.example.quizapp_room.data.score.Score
import com.example.quizapp_room.data.score.ScoreRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class QuizUiState(
    val currentsQuizItem: QuizItem? = null,
    val quizAnswers: List<String> = emptyList(),
    val isAnswered: Boolean = false,
    val score: Int = 0,
    val notEnoughQuizItems: Boolean = false,
    val isQuizDone: Boolean = false,
    val maximumScorePossible: Int = 0
)

class QuizViewModel(
    private val repository: QuizRepository,
    private val scoreRepository: ScoreRepository
) : ViewModel() {

    // Make use of the data class to pass as parameter to composable = less coding.
    // private val _uiState = MutableStateFlow(QuizUiState())
    // val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    private val allQuizItems = mutableStateListOf<QuizItem>()
    private val remainingQuizItems = mutableStateListOf<QuizItem>()

    var notEnoughQuizItems by mutableStateOf(false)

    var isQuizDone by mutableStateOf(false)

    var maximumScorePossible by mutableStateOf(0)

    var isAnswered by mutableStateOf(false)

    var quizAnswers = mutableStateListOf<String>()

    private val _currentQuizItem = MutableStateFlow<QuizItem?>(null)
    val currentQuizItem: StateFlow<QuizItem?> = _currentQuizItem.asStateFlow()

    var score by mutableStateOf(0)

     var scoreBoard = mutableStateListOf<Score>()



    init {
        loadData()
    }

    /**
     * Load all quiz items from the database.
     * Use [collect] to automatically update the UI when the database changes.
     * This means the quiz will restart if quiz items are added/deleted from database.
     */
    private fun loadData() {
        viewModelScope.launch {
            repository.getAllQuizItems().collect() {
                items ->
                allQuizItems.clear()
                allQuizItems.addAll(items)
                if(items.size < 3) {
                    notEnoughQuizItems = true
                } else {
                    notEnoughQuizItems = false
                }
                startNewQuiz()
            }

        }
    }

    fun startNewQuiz() {
        remainingQuizItems.clear()
        remainingQuizItems.addAll(allQuizItems)
        score = 0
        quizAnswers.clear()
        isAnswered = false
        isQuizDone = false
        maximumScorePossible = remainingQuizItems.size
        loadNextQuestion()
        Log.d("QuizViewModel", "Starting new quiz.")
    }

    fun loadNextQuestion() {
        if (remainingQuizItems.isEmpty()) {
            Log.d("QuizViewModel", "No more questions to load.")
            isQuizDone = true
            viewModelScope.launch {
                scoreRepository.saveScore("Player", score)
            }
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

    fun getScores() {
        viewModelScope.launch {
            val result = scoreRepository.getScores()
            scoreBoard.clear()
            scoreBoard.addAll(result)
        }

    }
}

class QuizViewModelFactory(private val repository: QuizRepository, private val scoreRepository: ScoreRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuizViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return QuizViewModel(repository, scoreRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

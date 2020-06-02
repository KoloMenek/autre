package com.example.exo1.model

class Question(question: String, correctAnswer: String, answers: List<String>) {
    var question: String = question
    var correctAnswer: String = correctAnswer
    var answers: List<String> = answers

    fun checkAnswer(theAnswer: String): Boolean {
        return theAnswer == correctAnswer

    }
}
package com.example.exo1.model

class QuestionBucket(qList: List<Question>) {
    var questionList: List<Question> = listOf()
    var next: Int = 0
    var size: Int = qList.size

    init {
        questionList = qList

    }

    fun hasNextQuestion(): Boolean {
        return next < size
    }

    fun getNewQuestion(): Question {
        if (next < size) {
            var theQuestion: Question = questionList.get(next)
            next++
            return theQuestion
        } else {
            next = 0
            var theQuestion: Question = questionList.get(next)
            next++
            return theQuestion

        }
    }
    fun setQuestion(question: Int){
        next = question
    }
    fun getQuestion():Int{
        return next - 1
    }
}
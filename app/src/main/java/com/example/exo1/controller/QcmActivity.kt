package com.example.exo1.controller

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import android.util.Log
import android.view.MotionEvent
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.exo1.R
import com.example.exo1.model.Question
import com.example.exo1.model.QuestionBucket
import java.util.*

class QcmActivity : AppCompatActivity() {
    private lateinit var mQuestion: TextView
    private var thePoints: Int = 0
    private lateinit var mPoints: TextView
    private var qLeft: Int = 0
    private lateinit var mQuestionsLeft: TextView
    private lateinit var mBtn1: Button
    private lateinit var mBtn2: Button
    private lateinit var mBtn3: Button
    private lateinit var mBtn4: Button
    private lateinit var theQuestion: Question
    private var currentQuestionNum: Int = 0;
    private var touchEnabled: Boolean = true


    companion object {
        const val SCORE_DATA = "SCORE_DATA"
    }

    fun generateQuestionBucket(): QuestionBucket {
        val question1 = Question(
            "Dans le film d'animation L'Âge de glace, qu'est-ce qui échappe à l'écureuil Scrat ?",
            "Un gland",
            listOf("Un gland", "Une pierre", "un os", "une bille")

        )
        val question2 = Question(
            "Comment se nomme l'orque à sauver dans une saga cinématographique populaire ?",
            "Willy",
            listOf("Willy", "Tom", "Monica", "Jennifer")
        )
        val question3 = Question(
            "Dans le film Les Dents de la mer, quel animal provoque la terreur sur l'île d'Amity ?",
            "Un requin",
            listOf("Un requin", "Une orque", "Un Kraken", "Un piranha")
        )
        return QuestionBucket(listOf(question1, question2, question3))
    }
    var orderedB = listOf(0,1,2,3)

    private fun <T:Comparable<T>>shuffle(items:MutableList<T>):List<T>{
        val rg : Random = Random()
        for (i in 0 until items.size) {
            val randomPosition = rg.nextInt(items.size)
            val tmp : T = items[i]
            items[i] = items[randomPosition]
            items[randomPosition] = tmp
        }
        return items
    }
    fun <T> Iterable<T>.shuffle(): List<T> {
        val list = this.toMutableList().apply {  }
        Collections.shuffle(list)
        return list
    }
    private fun setQuestion(theQuestion: Question, qLeft: Int, thePoints: Int, shuffle: Boolean = true) {
        if(shuffle)
            orderedB = orderedB.shuffle()
        mQuestionsLeft.text = "Questions left : $qLeft"
        mPoints.text = "My Points : $thePoints"
        mQuestion.text = theQuestion.question
        mBtn1.text = theQuestion.answers[orderedB[0]]
        mBtn2.text = theQuestion.answers[orderedB[1]]
        mBtn3.text = theQuestion.answers[orderedB[2]]
        mBtn4.text = theQuestion.answers[orderedB[3]]

        if (qLeft == 0) {
            val intent = Intent()
            intent.putExtra(SCORE_DATA, thePoints)
            setResult(Activity.RESULT_OK, intent)

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Fin de la partie")
                .setMessage("Vous avez marqué $thePoints points")
                .setPositiveButton("OK") { dialog, which -> finish() }
                .create()
                .show()
        }
    }

    override fun onStart(){
        super.onStart()
        System.out.println("\nUtilisation de la fonction onStart() dans QCMActivity\n")
    }
    override fun onStop(){
        super.onStop()
        System.out.println("\nUtilisation de la fonction onStop() dans QCMActivity\n")
    }
    override fun onResume(){
        super.onResume()
        System.out.println("\nUtilisation de la fonction onResume() dans QCMActivity\n")
    }
    override fun onPause(){
        super.onPause()
        System.out.println("\nUtilisation de la fonction onPause() dans QCMActivity\n")
    }
    override fun onDestroy(){
        super.onDestroy()
        System.out.println("\nUtilisation de la fonction onDestroy() dans QCMActivity\n")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val questions: QuestionBucket = generateQuestionBucket()


        System.out.println("\nUtilisation de la fonction onCreate() dans QCMActivity\n")
        setContentView(R.layout.activity_qcm)

        mQuestion = findViewById(R.id.activity_qcm_question)
        mPoints = findViewById(R.id.activity_qcm_points)
        mQuestionsLeft = findViewById(R.id.activity_qcm_q_left)
        mBtn1 = findViewById(R.id.activity_qcm_answer1)
        mBtn2 = findViewById(R.id.activity_qcm_answer2)
        mBtn3 = findViewById(R.id.activity_qcm_answer3)
        mBtn4 = findViewById(R.id.activity_qcm_answer4)
        if(savedInstanceState != null){
            currentQuestionNum = savedInstanceState.getInt("qNum")
            qLeft = savedInstanceState.getInt("qLeft")
            Log.v("PB Qleft", qLeft.toString())
            thePoints = savedInstanceState.getInt("thePoints")
            orderedB = savedInstanceState.getIntegerArrayList("questionOrder")?.toList() ?: listOf(0,1,2,3)

            questions.setQuestion(currentQuestionNum)
            theQuestion = questions.getNewQuestion()
            setQuestion(theQuestion, qLeft, thePoints, false)
        }
        else{
            qLeft = questions.size
            theQuestion = questions.getNewQuestion()
            setQuestion(theQuestion, qLeft, thePoints)
        }





        fun correctAnswer(mBtn: Button){
            Toast.makeText(this, "Bien joué !", Toast.LENGTH_SHORT).show()
            thePoints++
            currentQuestionNum++
            qLeft--
            theQuestion = questions.getNewQuestion()
            mBtn.setBackgroundColor(Color.parseColor("#3cb043"))
            touchEnabled = false
            Handler().postDelayed(Runnable {
                mBtn.setBackgroundColor(Color.parseColor("#679289"))
                setQuestion(theQuestion, qLeft, thePoints)
                touchEnabled = true
            },2000)

        }
        fun wrongAnswer(mBtn: Button){
            Toast.makeText(this, "Fail...", Toast.LENGTH_SHORT).show()
            qLeft--
            currentQuestionNum++
            theQuestion = questions.getNewQuestion()
            mBtn.setBackgroundColor(Color.parseColor("#c72c2c"))
            touchEnabled = false
            Handler().postDelayed(Runnable {
                mBtn.setBackgroundColor(Color.parseColor("#679289"))
                setQuestion(theQuestion, qLeft, thePoints)
                touchEnabled = true
            },2000)

        }


        mBtn1.setOnClickListener {
            if (theQuestion.checkAnswer(mBtn1.text as String)) {
                correctAnswer(mBtn1)
            } else {
                wrongAnswer(mBtn1)
            }
        }
        mBtn2.setOnClickListener {
            if (theQuestion.checkAnswer(mBtn2.text as String)) {
                correctAnswer(mBtn2)
            } else {
                wrongAnswer(mBtn2)
            }
        }
        mBtn3.setOnClickListener {
            if (theQuestion.checkAnswer(mBtn3.text as String)) {
                correctAnswer(mBtn3)
            } else {
                wrongAnswer(mBtn3)
            }
        }
        mBtn4.setOnClickListener {
            if (theQuestion.checkAnswer(mBtn4.text as String)) {
                correctAnswer(mBtn4)
            } else {
                wrongAnswer(mBtn4)
            }
        }


    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return touchEnabled && super.dispatchTouchEvent(ev)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putIntegerArrayList("questionOrder", ArrayList<Int>(orderedB))
        outState.putInt("qLeft", qLeft)
        Log.v("PB Qleft", "Onsave $qLeft")
        outState.putInt("thePoints", thePoints)
        outState.putInt("qNum", currentQuestionNum)

        super.onSaveInstanceState(outState)
    }
}

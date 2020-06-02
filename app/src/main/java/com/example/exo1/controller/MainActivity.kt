package com.example.exo1.controller

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.exo1.R
import com.example.exo1.model.User


class MainActivity : AppCompatActivity() {
    private var user: User =
        User()
    private lateinit var mGreetingText: TextView
    private lateinit var mFinalName: TextView
    private lateinit var mNameInput: EditText
    private lateinit var mPlayButton: Button
    private var bestScore: Int = 0
    private var lastScore: Int = 0
    private val REQUEST_QCM: Int = 187

    override fun onStart(){
        super.onStart()
        System.out.println("\nUtilisation de la fonction onStart() dans MainActivity\n")
    }
    override fun onStop(){
        super.onStop()
        System.out.println("\nUtilisation de la fonction onStop() dans MainActivity\n")
    }
    override fun onResume(){
        super.onResume()
        System.out.println("\nUtilisation de la fonction onResume() dans MainActivity\n")
    }
    override fun onPause(){
        super.onPause()
        System.out.println("\nUtilisation de la fonction onPause() dans MainActivity\n")
    }
    override fun onDestroy(){
        super.onDestroy()
        System.out.println("\nUtilisation de la fonction onDestroy() dans MainActivity\n")
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        System.out.println("\nUtilisation de la fonction onCreate() dans MainActivity\n")
        setContentView(R.layout.activity_main)
        mGreetingText = findViewById<TextView>(R.id.activity_main_greeting_txt)
        mNameInput = findViewById<EditText>(R.id.activity_main_name_input)
        mPlayButton = findViewById<Button>(R.id.activity_main_play_btn)

        val lastUser: String? = getPreferences(Context.MODE_PRIVATE).getString("lastUser", null)
        if (lastUser != null && lastUser != "") {
            lastScore = getSharedPreferences(lastUser, Context.MODE_PRIVATE).getInt("lastScore", 0)
            bestScore = getSharedPreferences(lastUser, Context.MODE_PRIVATE).getInt("bestScore", 0)
            mGreetingText.text =
                "Bon retour $lastUser\n ton dernier score était de $lastScore\n Vas-tu battre ton record de $bestScore cette fois ?"
            mNameInput.setText(lastUser)
            mNameInput.setSelection(lastUser.length - 1)
            mPlayButton.isEnabled = true
            user.userName = lastUser
        }

        mNameInput.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                mPlayButton.isEnabled = true
                if (s.toString().isNotEmpty())
                    user.userName = s.toString()
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {

            }
        })

        mPlayButton.setOnClickListener {
            getPreferences(Activity.MODE_PRIVATE).edit().putString("lastUser", user.userName)
                .apply()
            Toast.makeText(this@MainActivity, "Commençons !", Toast.LENGTH_SHORT).show()
            val newActivity = Intent(this@MainActivity, QcmActivity::class.java)
            startActivityForResult(newActivity, REQUEST_QCM)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (REQUEST_QCM == requestCode && Activity.RESULT_OK == resultCode) {
            val playerScore: Int = data?.getIntExtra(QcmActivity.SCORE_DATA, 0) ?: 0
            val preferences = getSharedPreferences(user.userName, Context.MODE_PRIVATE)
            val editor = preferences.edit()
            editor.putInt("lastScore", playerScore)
            bestScore =
                getSharedPreferences(user.userName, Context.MODE_PRIVATE).getInt("bestScore", 0)
            if (bestScore < playerScore) {
                editor.putInt("bestScore", playerScore)
                bestScore = playerScore
            }
            editor.apply()
            mGreetingText.text =
                "Bon retour ${user.userName}\n ton dernier score était de $playerScore\n Vas-tu battre ton record de $bestScore cette fois ?"
        }
    }

}

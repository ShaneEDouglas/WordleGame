package com.example.wordlegame

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.cardview.widget.CardView
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Confetti
import nl.dionsegijn.konfetti.core.emitter.Emitter
import nl.dionsegijn.konfetti.xml.KonfettiView
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {

    private var guessNumber: Int = 1
    private var MAX_TURNS: Int = 4

    private lateinit var targetWord: String
    private lateinit var guessEditText: EditText
    private lateinit var submitButton: Button

    private lateinit var guessOneTextView: TextView
    private lateinit var guessOneCorrectnessTextView: TextView
    private lateinit var guessTwoTextView: TextView
    private lateinit var guessTwoCorrectnessTextView: TextView
    private lateinit var guessThreeTextView: TextView
    private lateinit var guessThreeCorrectnessTextView: TextView
    private lateinit var guessFourTextView: TextView
    private lateinit var guessFourCorrectnessTextView: TextView
    private lateinit var endGameCardView: CardView
    private lateinit var resetGameButton: Button
    private lateinit var endgamemessagetext: TextView
    private lateinit var targetwordtextview: TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //initialize the game with getting the random word
        targetWord = FourLetterWordList.getRandomFourLetterWord()
        //For debugging
        Log.i("Target Word", "Target word is: $targetWord")

        guessEditText = findViewById(R.id.guessEditText)

        //allow only 4 characters and no numbers

        val filter = InputFilter { source, start, end, dest, dstart, dend ->
            for (index in start until end) {
                if (!Character.isLetter(source[index])) {
                    return@InputFilter ""
                }
            }
            null
        }
        guessEditText.filters = arrayOf(filter, InputFilter.LengthFilter(4))

        guessEditText.filters = arrayOf(filter, InputFilter.LengthFilter(4))
        submitButton = findViewById(R.id.submitGuessButton)

        endGameCardView = findViewById(R.id.endGameCardView)
        resetGameButton = findViewById(R.id.resetGameButton)

        endgamemessagetext = findViewById(R.id.endGameMessageTextView)

        //initializing all the guess and correction textviews

        guessOneTextView =findViewById(R.id.guessOne)
        guessTwoTextView =findViewById(R.id.guessTwo)
        guessThreeTextView =findViewById(R.id.guessThree)
        guessFourTextView =findViewById(R.id.guessFour)

        guessOneCorrectnessTextView = findViewById(R.id.guessOneCorrectness)
        guessTwoCorrectnessTextView = findViewById(R.id.guessTwoCorrectness)
        guessThreeCorrectnessTextView = findViewById(R.id.guessThreeCorrectness)
        guessFourCorrectnessTextView = findViewById(R.id.guessFourCorrectness)

        //target word
        targetwordtextview = findViewById<TextView>(R.id.realTargetworldtextview)




        submitButton.setOnClickListener {
            val guess = guessEditText.text.toString().uppercase()

            if (guess.length == 4) {
                val correctness =  submitGuess(guess, targetWord)
                UpdateUi(guessNumber, guess, correctness)
                guessEditText.text.clear()

                if (guess == targetWord) {
                    winGame()
                } else if (guessNumber == MAX_TURNS) {
                    loseGame()
                } else {
                    guessNumber++
                }
            }

        }

        resetGameButton.setOnClickListener {
            resetGame()
        }

    }






    private fun UpdateUi (guessNumber: Int, guess: String, correctness: String ) {
        when (guessNumber) {
            1 -> {
                guessOneTextView.text = guess
                guessOneCorrectnessTextView.text = correctness
            }

            2 -> {
                guessTwoTextView.text = guess
                guessTwoCorrectnessTextView.text = correctness
            }

            3 -> {
                guessThreeTextView.text = guess
                guessThreeCorrectnessTextView.text = correctness
            }

            4 -> {
                guessFourTextView.text = guess
                guessFourCorrectnessTextView.text = correctness
            }
        }

    }
    private fun resetGame() {
        // Fade out animation
        endGameCardView.animate().alpha(0.0f).setDuration(500).withEndAction{
            endGameCardView.visibility = View.GONE
            endGameCardView.alpha = 1.0f

            resetGuessesandText()
        }.start()
        targetWord = FourLetterWordList.getRandomFourLetterWord()
        guessEditText.isEnabled = true
        submitButton.isEnabled = true

        //debugging
        Log.i("Target Word", "Target word is: $targetWord")
    }

    private fun resetGuessesandText() {
        guessNumber = 1
        MAX_TURNS = 4
        guessEditText.text.clear()

        guessOneTextView.text = "Guess 1"
        guessOneCorrectnessTextView.text = "Guess 1 Correction"
        guessTwoTextView.text = "Guess 2"
        guessTwoCorrectnessTextView.text = "Guess 2 Correction"
        guessThreeTextView.text = "Guess 3"
        guessThreeCorrectnessTextView.text = "Guess 3 Correction"
        guessFourTextView.text = "Guess 4"
        guessFourCorrectnessTextView.text = "Guess 4 Correction"


    }

    private fun loseGame() {
        //Win and lose card disable the submit button
        endGameCardView.visibility = View.VISIBLE
        endGameCardView.alpha = 0.0f
        endGameCardView.animate().alpha(1.0f).setDuration(500).start()
        endgamemessagetext.text = "You Lose!"

        guessEditText.isEnabled = false
        submitButton.isEnabled = false
        targetwordtextview.text = targetWord

    }

    private fun winGame() {

        val konfettiView = findViewById<KonfettiView>(R.id.konfettiView)
            konfettiView.start(
            Party(
                speed = 0f,
                maxSpeed = 30f,
                damping = 0.9f,
                spread = 360,
                colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
                emitter = Emitter(duration = 100, TimeUnit.SECONDS).max(100),
                position = Position.Relative(0.5, 0.3)
            )

        )
        endGameCardView.visibility = View.VISIBLE
        endGameCardView.alpha = 0.0f
        endGameCardView.animate().alpha(1.0f).setDuration(500).start()
        endgamemessagetext.text = "You Win!"

        guessEditText.isEnabled = false
        submitButton.isEnabled = false
        targetwordtextview.text = targetWord

    }
}


private fun submitGuess(guess: String, targetWord: String): String {

    var result = ""

    guess.forEachIndexed{ index, c ->
        result += when {
            c == targetWord[index] -> "O"
            c in targetWord -> "+"
            else -> "X"
        }
    }
    return result
}





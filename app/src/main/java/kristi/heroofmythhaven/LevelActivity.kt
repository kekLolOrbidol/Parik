package kristi.heroofmythhaven

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_level.*

// These enums are used by the gamemanager class to dictate game play
enum class MovementUserInput {
    LEFT,
    RIGHT,
    NOINPUT
}
enum class ActionUserInput {
    ATTACK,
    JUMP,
    NOTHING
}

class LevelActivity : AppCompatActivity() {
    private var level = 0
    private var playerType: String = "human"
    private lateinit var gameManager: GameManager
    private var questLevelNumber: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level)

        playNameLevel.text = intent.getStringExtra("Username")
        level = intent.getIntExtra("Level", 0)
        playerType = intent.getStringExtra("Character").toString()
        levelLevel.text = String.format(getString(R.string.levelTitle) + intent.getIntExtra("Level", 0).toString())
        goldLevel.text = String.format(getString(R.string.goldTitle) + intent.getIntExtra("Gold", 0).toString())
        questLevelNumber = intent.getIntExtra("QuestNumber", 1)
    }

    override fun onStart() {
        super.onStart()
        gameManager = GameManager(questLevelNumber, this@LevelActivity, level, playerType)
        gameView.setGameManager(gameManager)

        leftButton.setOnTouchListener {_, motionEvent ->
            when(motionEvent.action and MotionEvent.ACTION_MASK) {
                MotionEvent.ACTION_DOWN -> {gameView.setLeftUserInput(MovementUserInput.LEFT)}
                MotionEvent.ACTION_UP -> {gameView.setLeftUserInput(MovementUserInput.NOINPUT)}
            }
            true
        }
        rightButton.setOnTouchListener {_, motionEvent ->
            when(motionEvent.action and MotionEvent.ACTION_MASK) {
                MotionEvent.ACTION_DOWN -> {gameView.setLeftUserInput(MovementUserInput.RIGHT)}
                MotionEvent.ACTION_UP -> {gameView.setLeftUserInput(MovementUserInput.NOINPUT)}
            }
            true
        }
        attackButton.setOnTouchListener {_, motionEvent ->
            when(motionEvent.action and MotionEvent.ACTION_MASK) {
                MotionEvent.ACTION_DOWN -> {gameView.setRightUserInput(ActionUserInput.ATTACK)}
                MotionEvent.ACTION_UP -> {gameView.setRightUserInput(ActionUserInput.NOTHING)}
            }
            true
        }
        jumpButton.setOnTouchListener {_, motionEvent ->
            when(motionEvent.action and MotionEvent.ACTION_MASK) {
                MotionEvent.ACTION_DOWN -> {gameView.setRightUserInput(ActionUserInput.JUMP)}
                MotionEvent.ACTION_UP -> {gameView.setRightUserInput(ActionUserInput.NOTHING)}
            }
            true
        }
    }

    fun updateHP(hitpoints: Int) {
        hp.text = "HP: " + hitpoints.toString()
    }

    // Gets called when the pause button is pressed
    fun showPausePopup(view: View) {
        // Create the dialog
        val dialogs = Dialog(this)
        dialogs.setCancelable(false)
        dialogs.setContentView(R.layout.pause_popup)

        // Pause the game
        gameManager.pause()

        // Give the dialog resume and quit buttons
        val resumeBtn = dialogs.findViewById(R.id.resumeButton) as Button
        val quitBtn = dialogs.findViewById(R.id.quitButton) as TextView
        resumeBtn.setOnClickListener {
            gameManager.resume()
            dialogs.dismiss()
        }
        quitBtn.setOnClickListener {
            dialogs.dismiss()
            val levelResult = intent
            levelResult.putExtra("DidWin", false)
            setResult(RESULT_OK, levelResult) //return the final score to main activity
            finish() //finish and go back to main activity //kills this activity //not on back stack
        }

        dialogs.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogs.show()
    }

    // Gets called when the player beats the level or dies
    fun showEndPopup(didWin: Boolean) {
        val titleText: String
        if(didWin) {
            titleText = getString(R.string.you_win)
            val levelResult = intent
            levelResult.putExtra("DidWin", true)
            setResult(RESULT_OK, levelResult) //return the final score to main activity
        }
        else {
            titleText = getString(R.string.you_lose)
            val levelResult = intent
            levelResult.putExtra("DidWin", false)
            setResult(RESULT_OK, levelResult) //return the final score to main activity
        }

        gameManager.pause()

        // Create the dialog
        val dialogs = Dialog(this)
        dialogs.setCancelable(false)
        dialogs.setContentView(R.layout.end_popup)
        val endTitle = dialogs.findViewById(R.id.endTitle) as TextView
        endTitle.text = titleText

        // Give the dialog restart and quit buttons
        val restartBtn = dialogs.findViewById(R.id.restartButtonEnd) as Button
        val quitBtn = dialogs.findViewById(R.id.quitButtonEnd) as TextView
        restartBtn.setOnClickListener {
            //Restart the game by giving the gameView a new gameManager (in its initial state)
            dialogs.dismiss()
            gameManager = GameManager(questLevelNumber, this@LevelActivity, level, playerType)
            gameView.setGameManager(gameManager)
        }
        quitBtn.setOnClickListener {
            dialogs.dismiss()
            finish() //finish and go back to main activity //kills this activity //not on back stack
        }
        dialogs.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogs.show()
    }
}
